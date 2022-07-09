package com.example.mini.autorizador.service;

import com.example.mini.autorizador.dto.CartaoDTO;
import com.example.mini.autorizador.model.Cartao;

import java.util.Optional;

public interface CartaoService {

    Optional<Cartao> consultarCartao(final String numeroCartao);

    Cartao cadastrarCartao(final CartaoDTO cartao);

    Optional<Double> consultarSaldo(final String numeroCartao);

    void adicionarDebito(final Long id, final double valorDebito);
}
