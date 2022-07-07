package com.example.mini.autorizador.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


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
    private Integer id;

    @Column(name = "numero_cartao", nullable = false, length = 16)
    private String numeroCartao;

    @Column(name = "senha", nullable = false, length = 6)
    private String senha;

    @Column(name = "saldo", nullable = false)
    private Double saldo;
}