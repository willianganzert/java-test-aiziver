package com.revizia.reviziabank.controllers.v1;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revizia.reviziabank.dtos.ContaDTO;
import com.revizia.reviziabank.dtos.ContaUpdateDTO;
import com.revizia.reviziabank.exceptions.ContaNotFoundException;
import com.revizia.reviziabank.services.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContaController.class)
public class ContaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContaService contaService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        reset(contaService);
    }

    @Test
    public void testListarContasSuccess() throws Exception {
        ContaDTO dto = new ContaDTO();
        dto.setId(1L);
        dto.setNumeroDaConta("123456");
        dto.setSaldo(BigDecimal.TEN);
        dto.setTitular("Joao Lopes");
        when(contaService.listarContas()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/v1/contas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testListarContasEmpty() throws Exception {
        when(contaService.listarContas()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v1/contas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testObterContaSuccess() throws Exception {
        ContaDTO dto = new ContaDTO();
        dto.setId(1L);
        dto.setNumeroDaConta("123456");
        dto.setSaldo(BigDecimal.TEN);
        dto.setTitular("Joao Lopes");
        when(contaService.obterConta(anyLong())).thenReturn(dto);

        mockMvc.perform(get("/api/v1/contas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testObterContaNotFound() throws Exception {
        when(contaService.obterConta(anyLong())).thenThrow(new ContaNotFoundException("Conta não encontrada"));

        mockMvc.perform(get("/api/v1/contas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testCriarContaValidationFailure() throws Exception {
        ContaDTO invalidDto = new ContaDTO();

        mockMvc.perform(post("/api/v1/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAtualizarContaValidationFailure() throws Exception {
        ContaUpdateDTO invalidDto = new ContaUpdateDTO();

        mockMvc.perform(patch("/api/v1/contas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testDeletarContaInvalidId() throws Exception {
        mockMvc.perform(delete("/api/v1/contas/0")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCriarContaSuccess() throws Exception {
        ContaDTO requestDto = new ContaDTO();
        requestDto.setNumeroDaConta("123456");
        requestDto.setSaldo(BigDecimal.TEN);
        requestDto.setTitular("Joao Lopes");
        ContaDTO responseDto = new ContaDTO();
        responseDto.setId(1L);
        responseDto.setNumeroDaConta("123456");
        responseDto.setSaldo(BigDecimal.TEN);
        responseDto.setTitular("Joao Lopes");
        when(contaService.criarConta(any(ContaDTO.class))).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAtualizarContaSuccess() throws Exception {
        ContaUpdateDTO requestDto = new ContaUpdateDTO();
        requestDto.setSaldo(BigDecimal.TEN);
        ContaDTO responseDto = new ContaDTO();
        responseDto.setId(1L);
        responseDto.setNumeroDaConta("123456");
        responseDto.setSaldo(BigDecimal.TEN);
        responseDto.setTitular("Joao Lopes");
        when(contaService.atualizarConta(anyLong(), any(ContaUpdateDTO.class))).thenReturn(responseDto);

        mockMvc.perform(patch("/api/v1/contas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(requestDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletarContaSuccess() throws Exception {
        doNothing().when(contaService).deletarConta(anyLong());

        mockMvc.perform(delete("/api/v1/contas/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}

