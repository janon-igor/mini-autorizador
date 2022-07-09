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
import java.util.List;
import java.util.stream.Collectors;

@RestController
class CartaoController {

    @Autowired private CartaoService cartaoService;

    @PostMapping(value = "/cartoes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartaoDTO> consultarCartao(@RequestBody CartaoDTO request) {

        return this.cartaoService.consultarCartao(request.getNumeroCartao())
                .map(e -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.convertToDTO()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CREATED).body(this.cartaoService.cadastrarCartao(request).convertToDTO()));
    }

    @GetMapping(value = "/cartoes/{numeroCartao}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Double> consultarSaldo(@PathVariable String numeroCartao) {
        return this.cartaoService.consultarSaldo(numeroCartao)
                .map(value -> ResponseEntity.status(HttpStatus.OK).body(value))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping(value = "/transacoes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusTramsacao> adicionarDebito(@RequestBody CartaoDTO cartaoDTO) {

        return this.cartaoService.consultarCartao(cartaoDTO.getNumeroCartao())
                .map(cartao -> Arrays.asList(new ValidarCartao.ValidadorSenha(), new ValidarCartao.ValidadorSaldo())
                        .stream().map(validador -> validador.validar(cartaoDTO, cartao)).collect(Collectors.toList())
                        .stream().filter(statusTramsacao -> ObjectUtils.isNotEmpty(statusTramsacao)).map(statusTramsacao -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(statusTramsacao))
                        .findAny().orElseGet(() -> {
                            this.cartaoService.adicionarDebito(cartao.getId(), cartaoDTO.getSaldo() - cartaoDTO.getSaldo());
                            return ResponseEntity.status(HttpStatus.CREATED).body(StatusTramsacao.OK);
                        }))
                .orElse(ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(StatusTramsacao.CARTAO_INEXISTENTE));
    }
}
