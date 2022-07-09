package com.example.mini.autorizador.model;

import com.example.mini.autorizador.dto.CartaoDTO;
import lombok.*;
import org.springframework.util.ObjectUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


/**
 * The persistent class for the afastamentos database table.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cartoes", schema = "miniautorizador")
public class Cartao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(name = "numero_cartao", nullable = false, length = 16)
    private String numeroCartao;

    @Column(name = "senha", nullable = false, length = 6)
    private String senha;

    @Column(name = "saldo", nullable = false)
    private Double saldo;

    @Version
    private Long version;

    public CartaoDTO convertToDTO() {
        return CartaoDTO.builder()
                //.id(this.id)
                .numeroCartao(this.numeroCartao)
                .senha(this.senha)
                //.saldo(this.saldo)
                .build();
    }

    @PrePersist
    public void prePersist() {
        if (ObjectUtils.isEmpty(this.saldo)) {
            this.saldo = 500.0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cartao)) return false;
        Cartao cartao = (Cartao) o;
        return Objects.equals(getId(), cartao.getId()) && Objects.equals(getNumeroCartao(), cartao.getNumeroCartao()) && Objects.equals(getSenha(), cartao.getSenha()) && Objects.equals(getSaldo(), cartao.getSaldo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNumeroCartao(), getSenha(), getSaldo());
    }
}