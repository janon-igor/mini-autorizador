package com.example.mini.autorizador.dao;

import com.example.mini.autorizador.model.Cartao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartaoRepository extends JpaRepository<Cartao, Long> {

    Optional<Cartao> findByNumeroCartao(String numeroCartao);

    @Query("SELECT c.saldo FROM Cartao c WHERE c.numeroCartao = :numeroCartao")
    Optional<Double> findSaldoByNumeroCartao(@Param(value = "numeroCartao") String numeroCartao);

    @Modifying
    @Query("UPDATE Cartao SET saldo = :valorDebito WHERE id = :id")
    void debitarValor(@Param(value = "id") long id, @Param(value = "valorDebito") double valorDebito);
}
