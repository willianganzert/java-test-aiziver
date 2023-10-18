package com.revizia.reviziabank.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class TransacaoDTO {

    @NotNull(message = "O campo contaOrigem não pode ser nulo.")
    @Pattern(regexp = "^[0-9]{5,10}$", message = "O número da conta deve ser composto apenas por números e ter entre 5 e 10 dígitos.")
    private String contaOrigem;

    @NotNull(message = "O campo contaDestino não pode ser nulo.")
    @Pattern(regexp = "^[0-9]{5,10}$", message = "O número da conta deve ser composto apenas por números e ter entre 5 e 10 dígitos.")
    private String contaDestino;

    @NotNull(message = "O campo valor não pode ser nulo.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior ou igual a 0.01.")
    private BigDecimal valor;


    public TransacaoDTO() {
    }

    public TransacaoDTO(String contaOrigem, String contaDestino, BigDecimal valor) {
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
        this.valor = valor;
    }

    public String getContaOrigem() {
        return contaOrigem;
    }

    public void setContaOrigem(String contaOrigem) {
        this.contaOrigem = contaOrigem;
    }

    public String getContaDestino() {
        return contaDestino;
    }

    public void setContaDestino(String contaDestino) {
        this.contaDestino = contaDestino;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}