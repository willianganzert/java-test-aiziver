package com.revizia.reviziabank.services;

import com.revizia.reviziabank.dtos.ExtratoDTO;
import com.revizia.reviziabank.dtos.TransacaoDTO;
import com.revizia.reviziabank.exceptions.ContaNotFoundException;
import com.revizia.reviziabank.exceptions.SaldoInsuficienteException;
import com.revizia.reviziabank.mappers.v1.TransacaoMapper;
import com.revizia.reviziabank.domain.models.Conta;
import com.revizia.reviziabank.domain.models.Transacao;
import com.revizia.reviziabank.domain.repositories.TransacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private ContaService contaService;

    @Autowired
    private TransacaoMapper transacaoMapper;

    private static final Logger logger = LoggerFactory.getLogger(TransacaoService.class);

    @Transactional
    public void transferir(TransacaoDTO transferenciaDTO) {
        Conta contaOrigem = contaService.findByNumero(transferenciaDTO.getContaOrigem())
                .orElseThrow(() -> new ContaNotFoundException("Conta de origem não encontrada"));
        Conta contaDestino = contaService.findByNumero(transferenciaDTO.getContaDestino())
                .orElseThrow(() -> new ContaNotFoundException("Conta de destino não encontrada"));

        if (contaOrigem.getSaldo().compareTo(transferenciaDTO.getValor()) < 0) {
            logger.error("Saldo insuficiente para a conta {}", contaOrigem.getNumeroDaConta());
            throw new SaldoInsuficienteException("Saldo insuficiente");
        }

        contaOrigem.debitar(transferenciaDTO.getValor());
        contaDestino.creditar(transferenciaDTO.getValor());

        Transacao transacao = new Transacao(contaOrigem, contaDestino, transferenciaDTO.getValor());
        transacaoRepository.save(transacao);

        logger.info("Transferência realizada com sucesso de {} para {}", contaOrigem.getNumeroDaConta(), contaDestino.getNumeroDaConta());
    }

    @Transactional(readOnly = true)
    public List<ExtratoDTO> obterExtrato(String numeroDaConta) {
        Conta conta = contaService.findByNumero(numeroDaConta)
                .orElseThrow(() -> new ContaNotFoundException("Conta não encontrada"));
        return transacaoRepository.findByContaOrigemOrContaDestinoOrderByDataHoraDesc(conta, conta)
                .stream()
                .map(transacaoMapper::toExtratoDTO)
                .collect(Collectors.toList());
    }
}
