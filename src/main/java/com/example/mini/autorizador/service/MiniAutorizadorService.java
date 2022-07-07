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

    public Cartao novoCartao(final CartaoDTO request) {
        return miniAutorizadorRepository.save(request.convertToEntity());
    }
}
