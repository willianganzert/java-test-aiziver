package com.revizia.reviziabank.controllers.v1;

import com.revizia.reviziabank.dtos.ContaDTO;
import com.revizia.reviziabank.dtos.ContaUpdateDTO;
import com.revizia.reviziabank.services.ContaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contas")
@Validated
public class ContaController {

    @Autowired
    private ContaService contaService;

    @GetMapping
    public ResponseEntity<List<ContaDTO>> listarContas() {
        List<ContaDTO> contas = contaService.listarContas();
        if (contas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contas);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ContaDTO> obterConta(@Valid @PathVariable  @Min(1) Long id) {
        return ResponseEntity.ok(contaService.obterConta(id));
    }

    @PostMapping
    public ResponseEntity<ContaDTO> criarConta(@Valid @RequestBody ContaDTO contaDTO) {
        ContaDTO createdContaDTO = contaService.criarConta(contaDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdContaDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(createdContaDTO);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ContaDTO> atualizarConta(@Valid @PathVariable  @Min(1)  Long id, @Valid @RequestBody ContaUpdateDTO contaUpdateDTO) {
        return ResponseEntity.ok(contaService.atualizarConta(id, contaUpdateDTO));
    }

    @GetMapping("/exists/{numeroDaConta}")
    public ResponseEntity<Boolean> contaExists(@PathVariable String numeroDaConta) {
        boolean exists = contaService.existsByNumeroDaConta(numeroDaConta);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarConta(@Valid @PathVariable @Min(1) Long id) {
        contaService.deletarConta(id);
        return ResponseEntity.noContent().build();
    }
}

