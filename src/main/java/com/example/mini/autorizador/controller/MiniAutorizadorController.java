package com.example.mini.autorizador.controller;

import com.example.mini.autorizador.dto.CartaoDTO;
import com.example.mini.autorizador.enums.StatusTramsacao;
import com.example.mini.autorizador.service.MiniAutorizadorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MiniAutorizadorController {

    @Autowired private MiniAutorizadorService miniAutorizadorService;

    @PostMapping(value = "/cartoes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CartaoDTO> novoCartao(@RequestBody CartaoDTO request) {

        return this.miniAutorizadorService.findByNumeroCartao(request.getNumeroCartao())
                .map(e -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.convertToDTO()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CREATED).body(this.miniAutorizadorService.saveOrUpdate(request).convertToDTO()));
    }

    @GetMapping(value = "/cartoes/{numeroCartao}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Double> consultarCartao(@PathVariable String numeroCartao) {
        return this.miniAutorizadorService.findByNumeroCartao(numeroCartao)
                .map(e -> ResponseEntity.status(HttpStatus.OK).body(e.getSaldo()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping(value = "/transacoes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusTramsacao> novaTransacao(@RequestBody CartaoDTO request) {

        return this.miniAutorizadorService.findByNumeroCartao(request.getNumeroCartao())
                .map(e -> {
                    if (!request.getSenha().equals(e.getSenha()))
                        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(StatusTramsacao.SENHA_INVALIDA);
                    else if (e.getSaldo() <= 0 || request.getSaldo() > e.getSaldo())
                        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(StatusTramsacao.SALDO_INSUFICIENTE);
                    else {
                        this.miniAutorizadorService.realizarTransacao(request.getSaldo(), e);
                        return ResponseEntity.status(HttpStatus.CREATED).body(StatusTramsacao.OK);
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(StatusTramsacao.CARTAO_INEXISTENTE));
    }
}
