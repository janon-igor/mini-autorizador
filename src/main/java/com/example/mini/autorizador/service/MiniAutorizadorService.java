package com.example.mini.autorizador.service;

import com.example.mini.autorizador.dto.CartaoDTO;
import com.example.mini.autorizador.model.Cartao;
import com.example.mini.autorizador.repository.MiniAutorizadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MiniAutorizadorService {

    @Autowired private MiniAutorizadorRepository miniAutorizadorRepository;

    public Optional<Cartao> findByNumeroCartao(final String numeroCartao) {
        return this.miniAutorizadorRepository.findByNumeroCartao(numeroCartao);
    }

    public Cartao saveOrUpdate(final CartaoDTO request) {
        return this.saveOrUpdate(request.convertToEntity());
    }

    public Cartao saveOrUpdate(final Cartao cartao) {
        return miniAutorizadorRepository.save(cartao);
    }

    public Cartao realizarTransacao(final Double valor, final Cartao cartao) {
        cartao.setSaldo(cartao.getSaldo() - valor);
        return this.saveOrUpdate(cartao);
    }
}
