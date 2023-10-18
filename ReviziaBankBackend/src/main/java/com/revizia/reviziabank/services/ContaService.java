package com.revizia.reviziabank.services;

import com.revizia.reviziabank.dtos.ContaDTO;
import com.revizia.reviziabank.dtos.ContaUpdateDTO;
import com.revizia.reviziabank.exceptions.ContaAlreadyExistsException;
import com.revizia.reviziabank.exceptions.ContaNotFoundException;
import com.revizia.reviziabank.mappers.v1.ContaMapper;
import com.revizia.reviziabank.domain.models.Conta;
import com.revizia.reviziabank.domain.repositories.ContaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ContaMapper contaMapper;

    private static final Logger logger = LoggerFactory.getLogger(ContaService.class);

    @Transactional(readOnly = true)
    public List<ContaDTO> listarContas() {
        return contaRepository.findAll().stream()
                .map(contaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContaDTO obterConta(Long id) {
        return contaMapper.toDTO(contaRepository.findById(id).orElseThrow(() -> new ContaNotFoundException("Conta não encontrada")));
    }
    @Transactional
    public ContaDTO criarConta(ContaDTO contaDTO) {
        Optional<Conta> contaExistente = contaRepository.findByNumeroDaConta(contaDTO.getNumeroDaConta());
        if (contaExistente.isPresent()) {
            logger.error("Tentativa de criação de conta com número já existente: {}", contaDTO.getNumeroDaConta());
            throw new ContaAlreadyExistsException("Conta com esse número já existe.");
        }

        Conta contaToSave = contaMapper.toEntity(contaDTO);

        Conta contaCriada = contaRepository.save(contaToSave);

        logger.info("Conta criada com sucesso com o número {}", contaCriada.getNumeroDaConta());

        return contaMapper.toDTO(contaCriada);
    }

    @Transactional(readOnly = true)
    public Optional<Conta> findByNumero(String numeroDaConta) {
        Optional<Conta> conta = contaRepository.findByNumeroDaConta(numeroDaConta);
        if (!conta.isPresent()) {
            logger.warn("Conta com número {} não encontrada", numeroDaConta);
        }
        return conta;
    }

    @Transactional
    public ContaDTO atualizarConta(Long id, ContaUpdateDTO contaUpdateDTO) {
        Conta existingConta = contaRepository.findById(id)
                .orElseThrow(() -> new ContaNotFoundException("Conta não encontrada."));

        existingConta.setSaldo(contaUpdateDTO.getSaldo());

        Conta updatedConta = contaRepository.save(existingConta);

        logger.info("Conta com ID {} atualizada com sucesso", updatedConta.getId());

        return contaMapper.toDTO(updatedConta);
    }

    @Transactional(readOnly = true)
    public boolean existsByNumeroDaConta(String numeroDaConta) {
        return contaRepository.existsByNumeroDaConta(numeroDaConta);
    }


    @Transactional
    public void deletarConta(Long id) {
        contaRepository.deleteById(id);
        logger.info("Tentativa de deletar a conta ID {}. Se existia, foi deletado.", id);
    }


}