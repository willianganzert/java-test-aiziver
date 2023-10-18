package com.revizia.reviziabank.documentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revizia.reviziabank.controllers.v1.ContaController;
import com.revizia.reviziabank.dtos.ContaDTO;
import com.revizia.reviziabank.dtos.ContaUpdateDTO;
import com.revizia.reviziabank.exceptions.ContaAlreadyExistsException;
import com.revizia.reviziabank.exceptions.ContaNotFoundException;
import com.revizia.reviziabank.services.ContaService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ContaController.class)
public class ContaDocumentationTest extends ApiDocumentation{

    @MockBean
    private ContaService contaService;

    @BeforeEach
    public void setUp() {
        reset(contaService);
    }

    @Test
    public void listarContasSuccess() throws Exception {
        ContaDTO contaDTO = new ContaDTO(1L, "Joao Lopes", "12345", new BigDecimal("1000"));
        when(contaService.listarContas()).thenReturn(Collections.singletonList(contaDTO));

        this.mockMvc.perform(get("/api/v1/contas"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("listar-contas",
                        responseFields(
                                fieldWithPath("[].id").description("Identificador da conta"),
                                fieldWithPath("[].titular").description("Titular da conta"),
                                fieldWithPath("[].numeroDaConta").description("Número da conta"),
                                fieldWithPath("[].saldo").description("Saldo da conta")
                        )));
    }

    @Test
    public void listarContasEmpty() throws Exception {
        when(contaService.listarContas()).thenReturn(Collections.emptyList());

        this.mockMvc.perform(get("/api/v1/contas"))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("listar-contas-vazio"));
    }

    @Test
    public void listarContasServerError() throws Exception {
        when(contaService.listarContas()).thenThrow(new RuntimeException("Inesperado erro interno"));

        this.mockMvc.perform(get("/api/v1/contas"))
                .andExpect(status().isInternalServerError())
                .andDo(document("listar-contas-servererror",
                        responseFields(
                                fieldWithPath("status").description("Descrição do código HTTP de erro"),
                                fieldWithPath("message").description("Mensagem de erro detalhando o motivo da falha"),
                                fieldWithPath("errors").description("Lista de erros")
                        )));
    }

    @Test
    public void obterConta() throws Exception {
        ContaDTO contaDTO = new ContaDTO(1L, "Joao Lopes", "12345", new BigDecimal("1000"));
        when(contaService.obterConta(anyLong())).thenReturn(contaDTO);

        this.mockMvc.perform(get("/api/v1/contas/{id}", 1L))
                .andExpect(status().isOk())
                .andDo(document("obter-conta-success",
                        pathParameters(
                                parameterWithName("id").description("Identificador da conta para obter")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Identificador da conta"),
                                fieldWithPath("titular").description("Titular da conta"),
                                fieldWithPath("numeroDaConta").description("Número da conta"),
                                fieldWithPath("saldo").description("Saldo da conta")
                        )));
    }

    @Test
    public void obterContaNotFound() throws Exception {
        when(contaService.obterConta(99L)).thenThrow(new ContaNotFoundException("Conta não encontrada"));

        this.mockMvc.perform(get("/api/v1/contas/{id}", 99L))
                .andExpect(status().isNotFound())
                .andDo(document("obter-conta-notfound",
                        pathParameters(
                                parameterWithName("id").description("Identificador da conta para obter")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Descrição do código HTTP de erro"),
                                fieldWithPath("message").description("Mensagem de erro detalhando o motivo da falha"),
                                fieldWithPath("errors").description("Lista de erros")
                        )));
    }

    @Test
    public void obterContaInvalidID() throws Exception {
        this.mockMvc.perform(get("/api/v1/contas/{id}", "invalid_id"))
                .andExpect(status().isBadRequest())
                .andDo(document("obter-conta-badrequest",
                        pathParameters(
                                parameterWithName("id").description("Identificador da conta para obter")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Descrição do código HTTP de erro"),
                                fieldWithPath("message").description("Mensagem de erro detalhando o motivo da falha"),
                                fieldWithPath("errors").description("Lista de erros")
                        )));
    }

    @Test
    public void obterContaServerError() throws Exception {
        when(contaService.obterConta(anyLong())).thenThrow(new RuntimeException("Inesperado erro interno"));

        this.mockMvc.perform(get("/api/v1/contas/{id}", 1L))
                .andExpect(status().isInternalServerError())
                .andDo(document("obter-conta-servererror",
                        pathParameters(
                                parameterWithName("id").description("Identificador da conta para obter")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Descrição do código HTTP de erro"),
                                fieldWithPath("message").description("Mensagem de erro detalhando o motivo da falha"),
                                fieldWithPath("errors").description("Lista de erros")
                        )));
    }

    @Test
    public void criarConta() throws Exception {
        ContaDTO requestContaDTO = new ContaDTO("Joao Lopes", "12345", new BigDecimal("1000"));
        ContaDTO responseContaDTO = new ContaDTO(1L, "Maria Lopes", "12345", new BigDecimal("1000"));
        when(contaService.criarConta(any(ContaDTO.class))).thenReturn(responseContaDTO);

        this.mockMvc.perform(post("/api/v1/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(requestContaDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", Matchers.containsString("/api/v1/contas/1")))
                .andDo(document("criar-conta",
                        requestFields(
                                fieldWithPath("titular").description("Titular da conta"),
                                fieldWithPath("numeroDaConta").description("Número da conta"),
                                fieldWithPath("saldo").description("Saldo da conta")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Identificador da conta"),
                                fieldWithPath("titular").description("Titular da conta"),
                                fieldWithPath("numeroDaConta").description("Número da conta"),
                                fieldWithPath("saldo").description("Saldo da conta")
                        )));
    }
    @Test
    public void criarContaContaJaExistente() throws Exception {
        ContaDTO requestContaDTO = new ContaDTO("Joao Lopes", "12345", new BigDecimal("1000"));

        when(contaService.criarConta(any(ContaDTO.class))).thenThrow(new ContaAlreadyExistsException("Conta com esse número já existe."));

        this.mockMvc.perform(post("/api/v1/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(requestContaDTO)))
                .andExpect(status().isConflict())  // Espera o status HTTP 409 Conflict
                .andExpect(jsonPath("$.message", Matchers.is("Conta com esse número já existe.")))
                .andDo(document("criar-conta-conflict",
                        responseFields(
                                fieldWithPath("status").description("Status do erro"),
                                fieldWithPath("message").description("Mensagem de erro"),
                                fieldWithPath("errors").description("Lista de erros")
                        )));
    }
    @Test
    public void criarContaBadRequest() throws Exception {
        // Não configurado um mock para o service, então o service real será chamado (e vai retornar erros de validação)
        ContaDTO requestContaDTO = new ContaDTO(null, null, null); 
        this.mockMvc.perform(post("/api/v1/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(requestContaDTO)))
                .andExpect(status().isBadRequest())
                .andDo(document("criar-conta-badrequest",
                        responseFields(
                                fieldWithPath("status").description("Status do erro"),
                                fieldWithPath("message").description("Mensagem de erro"),
                                fieldWithPath("errors").description("Lista de erros")
                        )));
    }

    @Test
    public void criarContaServerError() throws Exception {
        when(contaService.criarConta(any(ContaDTO.class))).thenThrow(new RuntimeException("Erro interno"));

        ContaDTO requestContaDTO = new ContaDTO("Joao Lopes", "12345", new BigDecimal("1000"));
        this.mockMvc.perform(post("/api/v1/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsString(requestContaDTO)))
                .andExpect(status().isInternalServerError())
                .andDo(document("criar-conta-servererror",
                        responseFields(
                                fieldWithPath("status").description("Status do erro"),
                                fieldWithPath("message").description("Mensagem de erro"),
                                fieldWithPath("errors").description("Lista de erros")
                        )));
    }



    @Test
    public void atualizarConta() throws Exception {
        ContaUpdateDTO contaUpdateDTO = new ContaUpdateDTO(new BigDecimal("2000"));
        ContaDTO responseContaDTO = new ContaDTO(1L, "Joao Lopes Updated", "12345", new BigDecimal("2000"));
        when(contaService.atualizarConta(anyLong(), any(ContaUpdateDTO.class))).thenReturn(responseContaDTO);

        this.mockMvc.perform(patch("/api/v1/contas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contaUpdateDTO)))
                .andExpect(status().isOk())
                .andDo(document("atualizar-conta",
                        pathParameters(
                                parameterWithName("id").description("Identificador da conta para atualizar")
                        ),
                        requestFields(
                                fieldWithPath("saldo").description("Saldo da conta atualizado")
                        ),
                        responseFields(
                                fieldWithPath("id").description("Identificador da conta"),
                                fieldWithPath("titular").description("Titular da conta"),
                                fieldWithPath("numeroDaConta").description("Número da conta"),
                                fieldWithPath("saldo").description("Saldo da conta")
                        )));
    }

    @Test
    public void atualizarContaBadRequest() throws Exception {
        ContaUpdateDTO contaUpdateDTO = new ContaUpdateDTO(null);  // saldo nulo para simular uma bad request

        this.mockMvc.perform(patch("/api/v1/contas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contaUpdateDTO)))
                .andExpect(status().isBadRequest())
                .andDo(document("atualizar-conta-badrequest",
                        pathParameters(
                                parameterWithName("id").description("Identificador da conta para atualizar")
                        ),
                        requestFields(
                                fieldWithPath("saldo").description("Saldo da conta atualizado").type(JsonFieldType.NUMBER).optional().ignored()
                        ),
                        responseFields(
                                fieldWithPath("status").description("Status do erro"),
                                fieldWithPath("message").description("Mensagem de erro"),
                                fieldWithPath("errors").description("Lista de erros")
                        )));
    }

    @Test
    public void atualizarContaNotFound() throws Exception {
        ContaUpdateDTO contaUpdateDTO = new ContaUpdateDTO(new BigDecimal("2000"));
        when(contaService.atualizarConta(anyLong(), any(ContaUpdateDTO.class))).thenThrow(new ContaNotFoundException("Conta não encontrada"));

        this.mockMvc.perform(patch("/api/v1/contas/{id}", 999L)  // ID que não existe
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contaUpdateDTO)))
                .andExpect(status().isNotFound())
                .andDo(document("atualizar-conta-notfound",
                        pathParameters(
                                parameterWithName("id").description("Identificador da conta para atualizar")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Status do erro"),
                                fieldWithPath("message").description("Mensagem de erro"),
                                fieldWithPath("errors").description("Lista de erros")
                        )));
    }

    @Test
    public void atualizarContaInternalServerError() throws Exception {
        ContaUpdateDTO contaUpdateDTO = new ContaUpdateDTO(new BigDecimal("2000"));
        when(contaService.atualizarConta(anyLong(), any(ContaUpdateDTO.class))).thenThrow(new RuntimeException("Erro inesperado"));

        this.mockMvc.perform(patch("/api/v1/contas/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contaUpdateDTO)))
                .andExpect(status().isInternalServerError())
                .andDo(document("atualizar-conta-servererror",
                        pathParameters(
                                parameterWithName("id").description("Identificador da conta para atualizar")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Status do erro"),
                                fieldWithPath("message").description("Mensagem de erro"),
                                fieldWithPath("errors").description("Lista de erros")
                        )));
    }



    @Test
    public void deletarConta() throws Exception {
        doNothing().when(contaService).deletarConta(anyLong());

        this.mockMvc.perform(delete("/api/v1/contas/{id}", 1L))
                .andExpect(status().isNoContent())
                .andDo(document("deletar-conta",
                        pathParameters(
                                parameterWithName("id").description("Identificador da conta para deletar")
                        )));
    }

    @Test
    public void deletarContaBadRequest() throws Exception {
        this.mockMvc.perform(delete("/api/v1/contas/{id}", -1L))
                .andExpect(status().isBadRequest())
                .andDo(document("deletar-conta-badrequest",
                        pathParameters(
                                parameterWithName("id").description("Identificador inválido da conta para deletar")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Status do erro"),
                                fieldWithPath("message").description("Mensagem de erro"),
                                fieldWithPath("errors").description("Lista de erros")
                        )));
    }

    @Test
    public void deletarContaNotFound() throws Exception {
        doNothing().when(contaService).deletarConta(999L);

        this.mockMvc.perform(delete("/api/v1/contas/{id}", 999L))
                .andExpect(status().isNoContent())
                .andDo(document("deletar-conta-notfound",
                        pathParameters(
                                parameterWithName("id").description("Identificador da conta não encontrado")
                        )));
    }

    @Test
    public void deletarContaInternalServerError() throws Exception {
        doThrow(new RuntimeException("Erro interno")).when(contaService).deletarConta(1L);

        this.mockMvc.perform(delete("/api/v1/contas/{id}", 1L))
                .andExpect(status().isInternalServerError())
                .andDo(document("deletar-conta-servererror",
                        pathParameters(
                                parameterWithName("id").description("Identificador da conta para deletar")
                        ),
                        responseFields(
                                fieldWithPath("status").description("Status do erro"),
                                fieldWithPath("message").description("Mensagem de erro"),
                                fieldWithPath("errors").description("Lista de erros")
                        )));
    }
}
