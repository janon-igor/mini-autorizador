package com.example.mini.autorizador.controller;

import com.example.mini.autorizador.dto.CartaoDTO;
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
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CREATED).body(this.miniAutorizadorService.novoCartao(request).convertToDTO()));
    }

    @GetMapping(value = "/cartoes/{numeroCartao}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CartaoDTO> consultarCartao(@PathVariable String numeroCartao) {
        return this.miniAutorizadorService.findByNumeroCartao(numeroCartao)
                .map(e -> ResponseEntity.status(HttpStatus.OK).body(e.convertToDTO()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
