package com.example.mini.autorizador.dto;

import com.example.mini.autorizador.model.Cartao;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartaoDTO {

    private Long id;
    private String numeroCartao;
    private String senha;
    private Double saldo;

    public Cartao convertToEntity() {
        return Cartao.builder().id(this.id).numeroCartao(this.numeroCartao).senha(this.senha).saldo(this.saldo).build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartaoDTO)) return false;
        CartaoDTO cartaoDTO = (CartaoDTO) o;
        return getId().equals(cartaoDTO.getId()) && getNumeroCartao().equals(cartaoDTO.getNumeroCartao()) && getSenha().equals(cartaoDTO.getSenha()) && getSaldo().equals(cartaoDTO.getSaldo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNumeroCartao(), getSenha(), getSaldo());
    }
}
