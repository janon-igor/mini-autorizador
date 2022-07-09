package com.example.mini.autorizador.web;

import com.example.mini.autorizador.constant.StatusTramsacao;
import com.example.mini.autorizador.domain.ValidarCartao;
import com.example.mini.autorizador.dto.CartaoDTO;
import com.example.mini.autorizador.service.CartaoService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
class CartaoController {

    @Autowired private CartaoService cartaoService;

    @PostMapping(value = "/cartoes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartaoDTO> consultar(@RequestBody CartaoDTO request) {
        return this.cartaoService.consultar(request.getNumeroCartao())
                .map(e -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.convertToDTO()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CREATED).body(this.cartaoService.cadastrar(request).convertToDTO()));
    }

    @GetMapping(value = "/cartoes/{numeroCartao}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Double> consultarSaldo(@PathVariable String numeroCartao) {
        return this.cartaoService.consultarSaldo(numeroCartao)
                .map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping(value = "/transacoes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusTramsacao> debitarSaldo(@RequestBody CartaoDTO cartaoDTO) {

        return this.cartaoService.consultar(cartaoDTO.getNumeroCartao())
                .map(cartao -> Arrays.asList(new ValidarCartao.Senha(), new ValidarCartao.Saldo())
                        .stream().map(v -> v.validar(cartaoDTO, cartao)).collect(Collectors.toList())
                        .stream().filter(st -> ObjectUtils.isNotEmpty(st)).map(st -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(st))
                        .findAny().orElseGet(() -> this.cartaoService.debitarSaldo(cartao.getId(), cartao.getSaldo() - cartaoDTO.getSaldo())
                                .map(v -> ResponseEntity.status(HttpStatus.CREATED).body(StatusTramsacao.OK)).get()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(StatusTramsacao.CARTAO_INEXISTENTE));
    }
}
