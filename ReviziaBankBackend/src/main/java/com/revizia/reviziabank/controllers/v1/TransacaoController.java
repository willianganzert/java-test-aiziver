package com.revizia.reviziabank.controllers.v1;

import com.revizia.reviziabank.dtos.ExtratoDTO;
import com.revizia.reviziabank.dtos.TransacaoDTO;
import com.revizia.reviziabank.services.TransacaoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transacoes")
@Validated
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @PostMapping("/transferir")
    public ResponseEntity<Void> transferir(@Valid @RequestBody TransacaoDTO transferenciaDTO) {
        transacaoService.transferir(transferenciaDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/extrato/{numeroDaConta}")
    public ResponseEntity<List<ExtratoDTO>> obterExtrato(
            @Valid
            @NotBlank(message = "O campo numeroDaConta não pode estar vazio.")
            @Pattern(regexp = "^[0-9]{5,10}$", message = "O número da conta deve ser composto apenas por números e ter entre 5 e 10 dígitos.")
            @PathVariable String numeroDaConta) {

        List<ExtratoDTO> extratoList = transacaoService.obterExtrato(numeroDaConta);

        if (extratoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(extratoList);
        }
    }

}
