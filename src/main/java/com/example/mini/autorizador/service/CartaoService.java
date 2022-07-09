package com.example.mini.autorizador.service;

import com.example.mini.autorizador.dto.CartaoDTO;
import com.example.mini.autorizador.model.Cartao;

import java.util.Optional;

public interface CartaoService {

    Optional<Cartao> consultar(final String numeroCartao);

    Cartao cadastrar(final CartaoDTO cartao);

    Optional<Double> consultarSaldo(final String numeroCartao);

    Optional<Integer> debitarSaldo(final Long id, final double valorDebito);
}
