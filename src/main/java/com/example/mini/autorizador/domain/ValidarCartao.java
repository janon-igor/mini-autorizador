package com.example.mini.autorizador.domain;

import com.example.mini.autorizador.constant.StatusTramsacao;
import com.example.mini.autorizador.dto.CartaoDTO;
import com.example.mini.autorizador.model.Cartao;

public class ValidarCartao {

    public interface Validador<A, B> {
        StatusTramsacao validar(A a, B b);
    }

    public static class Senha implements Validador<CartaoDTO, Cartao> {
        @Override
        public StatusTramsacao validar(CartaoDTO cartaoDTO, Cartao cartao) {
            return !cartaoDTO.getSenha().equals(cartao.getSenha()) ? StatusTramsacao.SENHA_INVALIDA : null;
        }
    }

    public static class Saldo implements Validador<CartaoDTO, Cartao> {
        @Override
        public StatusTramsacao validar(CartaoDTO cartaoDTO, Cartao cartao) {
            return cartao.getSaldo() <= 0 || cartaoDTO.getSaldo() <= 0 || cartaoDTO.getSaldo() > cartao.getSaldo() ? StatusTramsacao.SALDO_INSUFICIENTE : null;
        }
    }
}
