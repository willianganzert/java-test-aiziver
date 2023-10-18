package com.revizia.reviziabank.dtos;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public class ContaDTO {

    private Long id;
    @NotBlank(message = "O nome do titular não pode estar em branco.")
    @Size(min = 3, max = 100, message = "O nome do titular deve ter entre 3 e 100 caracteres.")
    private String titular;

    @NotNull(message = "O saldo não pode ser nulo.")
    @DecimalMin(value = "0.0", inclusive = true, message = "O saldo não pode ser negativo.")
    private BigDecimal saldo;

    @NotBlank(message = "O número da conta não pode estar em branco.")
    @Pattern(regexp = "^[0-9]{5,10}$", message = "O número da conta deve ser composto apenas por números e ter entre 5 e 10 dígitos.")
    private String numeroDaConta;

    public ContaDTO(Long id, String titular, String numeroDaConta, BigDecimal saldo) {
        this.id = id;
        this.titular = titular;
        this.saldo = saldo;
        this.numeroDaConta = numeroDaConta;
    }

    public ContaDTO(String titular, String numeroDaConta, BigDecimal saldo) {
        this.titular = titular;
        this.saldo = saldo;
        this.numeroDaConta = numeroDaConta;
    }

    public ContaDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public String getNumeroDaConta() {
        return numeroDaConta;
    }

    public void setNumeroDaConta(String numeroDaConta) {
        this.numeroDaConta = numeroDaConta;
    }
}
