package com.revizia.reviziabank.domain.repositories;

import com.revizia.reviziabank.domain.models.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByNumeroDaConta(String numeroDaConta);
    boolean existsByNumeroDaConta(String numeroDaConta);
}
