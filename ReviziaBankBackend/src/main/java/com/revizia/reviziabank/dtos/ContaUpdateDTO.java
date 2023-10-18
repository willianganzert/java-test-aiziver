package com.revizia.reviziabank.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ContaUpdateDTO {
    @NotNull(message = "O saldo não pode ser nulo.")
    private BigDecimal saldo;

    public ContaUpdateDTO() {
    }

    public ContaUpdateDTO(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }
}
