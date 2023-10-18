package com.revizia.reviziabank.controllers.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revizia.reviziabank.dtos.TransacaoDTO;
import com.revizia.reviziabank.services.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransacaoController.class)
public class TransacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransacaoService transacaoService;

    @BeforeEach
    public void setUp() {
        reset(transacaoService);
    }

    @Test
    public void testTransferir() throws Exception {
        TransacaoDTO dto = new TransacaoDTO();
        dto.setContaOrigem("123456");
        dto.setContaDestino("654321");
        dto.setValor(BigDecimal.TEN);
        mockMvc.perform(post("/api/v1/transacoes/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testObterExtrato() throws Exception {
        String numeroDaConta = "123456";
        when(transacaoService.obterExtrato(any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/transacoes/extrato/" + numeroDaConta)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testObterExtratoInvalidNumeroDaConta() throws Exception {
        String invalidNumeroDaConta = "12a34";

        mockMvc.perform(get("/api/v1/transacoes/extrato/" + invalidNumeroDaConta)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTransferirWithValidationError() throws Exception {
        TransacaoDTO invalidDto = new TransacaoDTO();

        mockMvc.perform(post("/api/v1/transacoes/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testObterExtratoWithShortNumeroDaConta() throws Exception {
        String shortNumeroDaConta = "1234";

        mockMvc.perform(get("/api/v1/transacoes/extrato/" + shortNumeroDaConta)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testObterExtratoWithLongNumeroDaConta() throws Exception {
        String longNumeroDaConta = "12345678901";

        mockMvc.perform(get("/api/v1/transacoes/extrato/" + longNumeroDaConta)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testTransferirServiceError() throws Exception {
        TransacaoDTO dto = new TransacaoDTO();
        dto.setContaOrigem("123456");
        dto.setContaDestino("654321");
        dto.setValor(BigDecimal.TEN);
        doThrow(RuntimeException.class).when(transacaoService).transferir(any(TransacaoDTO.class));

        mockMvc.perform(post("/api/v1/transacoes/transferir")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void testObterExtratoServiceError() throws Exception {
        String numeroDaConta = "123456";
        when(transacaoService.obterExtrato(any())).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/api/v1/transacoes/extrato/" + numeroDaConta)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
