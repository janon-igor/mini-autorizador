package com.example.mini.autorizador.service.impl;

import com.example.mini.autorizador.dao.CartaoRepository;
import com.example.mini.autorizador.dto.CartaoDTO;
import com.example.mini.autorizador.model.Cartao;
import com.example.mini.autorizador.service.CartaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
class CartaoServiceImpl implements CartaoService {

    @Autowired private CartaoRepository cartaoRepository;

    @Override
    public Optional<Cartao> consultar(final String numeroCartao) {
        return this.cartaoRepository.findByNumeroCartao(numeroCartao);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Cartao cadastrar(final CartaoDTO cartao) {
        return cartaoRepository.save(cartao.convertToEntity());
    }

    @Override
    public Optional<Double> consultarSaldo(final String numeroCartao) {
        return this.cartaoRepository.findSaldoByNumeroCartao(numeroCartao);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Optional<Integer> debitarSaldo(final Long id, final double valorDebito) {
        return this.cartaoRepository.debitar(id, valorDebito);
    }
}
