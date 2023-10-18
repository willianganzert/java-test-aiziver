package com.revizia.reviziabank.documentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revizia.reviziabank.controllers.v1.TransacaoController;
import com.revizia.reviziabank.dtos.ExtratoDTO;
import com.revizia.reviziabank.dtos.TransacaoDTO;
import com.revizia.reviziabank.exceptions.ContaNotFoundException;
import com.revizia.reviziabank.exceptions.SaldoInsuficienteException;
import com.revizia.reviziabank.services.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransacaoController.class)
public class TransacaoDocumentationTest extends ApiDocumentation {

    @MockBean
    private TransacaoService transacaoService;

    @BeforeEach
    public void setUp() {
        reset(transacaoService);
    }

    @Test
    public void transferirSuccess() throws Exception {
        TransacaoDTO requestTransacaoDTO = new TransacaoDTO();
        requestTransacaoDTO.setContaOrigem("12345678");
        requestTransacaoDTO.setContaDestino("87654321");
        requestTransacaoDTO.setValor(new BigDecimal("100.00"));

        this.mockMvc.perform(post("/api/v1/transacoes/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestTransacaoDTO)))
                .andExpect(status().isOk())
                .andDo(document("transferir-success",
                        requestFields(
                                fieldWithPath("contaOrigem").description("Número da conta de origem"),
                                fieldWithPath("contaDestino").description("Número da conta de destino"),
                                fieldWithPath("valor").description("Valor da transação")
                        )));
    }

    @Test
    public void transferirContaNotFound() throws Exception {
        TransacaoDTO requestTransacaoDTO = new TransacaoDTO();
        requestTransacaoDTO.setContaOrigem("12345678");
        requestTransacaoDTO.setContaDestino("87654321");
        requestTransacaoDTO.setValor(new BigDecimal("100.00"));

        doThrow(new ContaNotFoundException("Conta de origem ou destino não encontrada")).when(transacaoService).transferir(any());

        this.mockMvc.perform(post("/api/v1/transacoes/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestTransacaoDTO)))
                .andExpect(status().isNotFound())
                .andDo(document("transferir-conta-not-found",
                        requestFields(
                                fieldWithPath("contaOrigem").description("Número da conta de origem"),
                                fieldWithPath("contaDestino").description("Número da conta de destino"),
                                fieldWithPath("valor").description("Valor da transação")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Status of the error"),
                                fieldWithPath("message").description("Error message"),
                                fieldWithPath("errors").description("List of error details")
                        )));
    }

    @Test
    public void transferirWithInvalidData() throws Exception {
        TransacaoDTO requestTransacaoDTO = new TransacaoDTO();

        this.mockMvc.perform(post("/api/v1/transacoes/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestTransacaoDTO)))
                .andExpect(status().isBadRequest())
                .andDo(document("transferir-badrequest",
                        requestFields(
                                fieldWithPath("contaOrigem").description("Número da conta de origem. Requerido e deve ser um número de conta válido."),
                                fieldWithPath("contaDestino").description("Número da conta de destino. Requerido e deve ser um número de conta válido."),
                                fieldWithPath("valor").description("Valor da transação. Requerido e deve ser um valor válido.")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Status of the error"),
                                fieldWithPath("message").description("Error message"),
                                fieldWithPath("errors").description("List of error details")
                        )));
    }

    @Test
    public void transferirSaldoInsuficiente() throws Exception {
        TransacaoDTO requestTransacaoDTO = new TransacaoDTO();
        requestTransacaoDTO.setContaOrigem("12345678");
        requestTransacaoDTO.setContaDestino("87654321");
        requestTransacaoDTO.setValor(new BigDecimal("100.00"));

        // Mock insufficient balance scenario
        doThrow(new SaldoInsuficienteException("Saldo insuficiente")).when(transacaoService).transferir(any());

        this.mockMvc.perform(post("/api/v1/transacoes/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestTransacaoDTO)))
                .andExpect(status().isBadRequest())
                .andDo(document("transferir-saldo-insuficiente",
                        requestFields(
                                fieldWithPath("contaOrigem").description("Número da conta de origem"),
                                fieldWithPath("contaDestino").description("Número da conta de destino"),
                                fieldWithPath("valor").description("Valor da transação")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Status of the error"),
                                fieldWithPath("message").description("Error message"),
                                fieldWithPath("errors").description("List of error details")
                        )));
    }

    @Test
    public void transferirServerError() throws Exception {
        TransacaoDTO requestTransacaoDTO = new TransacaoDTO();
        requestTransacaoDTO.setContaOrigem("12345678");
        requestTransacaoDTO.setContaDestino("87654321");
        requestTransacaoDTO.setValor(new BigDecimal("100.00"));

        // Mock a generic runtime exception
        doThrow(new RuntimeException("Erro desconhecido")).when(transacaoService).transferir(any());

        this.mockMvc.perform(post("/api/v1/transacoes/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestTransacaoDTO)))
                .andExpect(status().isInternalServerError())
                .andDo(document("transferir-servererror",
                        requestFields(
                                fieldWithPath("contaOrigem").description("Número da conta de origem"),
                                fieldWithPath("contaDestino").description("Número da conta de destino"),
                                fieldWithPath("valor").description("Valor da transação")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Status of the error"),
                                fieldWithPath("message").description("Error message"),
                                fieldWithPath("errors").description("List of error details")
                        )));
    }

    @Test
    public void obterExtratoSuccess() throws Exception {
        String numeroDaConta = "12345678";

        ExtratoDTO extrato1 = new ExtratoDTO();
        extrato1.setTransacaoId(1L);
        extrato1.setContaOrigem("123");
        extrato1.setContaDestino("456");
        extrato1.setValor(new BigDecimal("100.00"));
        extrato1.setDataHora(LocalDateTime.now());

        ExtratoDTO extrato2 = new ExtratoDTO();
        extrato2.setTransacaoId(2L);
        extrato2.setContaOrigem("789");
        extrato2.setContaDestino("012");
        extrato2.setValor(new BigDecimal("150.00"));
        extrato2.setDataHora(LocalDateTime.now().minusHours(1));

        when(transacaoService.obterExtrato(numeroDaConta)).thenReturn(Arrays.asList(extrato1, extrato2));


        this.mockMvc.perform(get("/api/v1/transacoes/extrato/{numeroDaConta}", numeroDaConta))
                .andExpect(status().isOk())
                .andDo(document("obter-extrato-success",
                        pathParameters(
                                parameterWithName("numeroDaConta").description("Número da conta para obter o extrato")
                        ),
                        responseFields(
                                fieldWithPath("[].transacaoId").description("Identificador da transaçao"),
                                fieldWithPath("[].contaOrigem").description("Número da conta de origem"),
                                fieldWithPath("[].contaDestino").description("Número da conta de destino"),
                                fieldWithPath("[].valor").description("Valor da transação"),
                                fieldWithPath("[].dataHora").description("Data e hora da transação")
                        )));
    }

    @Test
    public void obterExtratoVazio() throws Exception {
        String numeroDaConta = "12345678";

        when(transacaoService.obterExtrato(numeroDaConta)).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/api/v1/transacoes/extrato/{numeroDaConta}", numeroDaConta))
                .andExpect(status().isNoContent())
                .andDo(document("obter-extrato-vazio",
                        pathParameters(
                                parameterWithName("numeroDaConta").description("Número da conta para obter o extrato")
                        )));
    }

    @Test
    public void listaContasInternalServerError() throws Exception {
        String numeroDaConta = "12345678";

        when(transacaoService.obterExtrato(numeroDaConta)).thenThrow(new RuntimeException("Internal server error"));

        this.mockMvc.perform(get("/api/v1/transacoes/extrato/{numeroDaConta}", numeroDaConta))
                .andExpect(status().isInternalServerError())
                .andDo(document("obter-extrato-servererror",
                        pathParameters(
                                parameterWithName("numeroDaConta").description("Número da conta para obter o extrato")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Status of the error"),
                                fieldWithPath("message").description("Error message"),
                                fieldWithPath("errors").description("List of error details")
                        )));
    }

}
