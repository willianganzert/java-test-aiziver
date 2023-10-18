package com.revizia.reviziabank.domain.repositories;

import com.revizia.reviziabank.domain.models.Conta;
import com.revizia.reviziabank.domain.models.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByContaOrigemOrContaDestinoOrderByDataHoraDesc(Conta contaOrigem, Conta contaDestino);
}