package com.revizia.reviziabank.mappers.v1;

import com.revizia.reviziabank.dtos.ExtratoDTO;
import com.revizia.reviziabank.dtos.TransacaoDTO;
import com.revizia.reviziabank.domain.models.Transacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {
    @Mapping(source = "contaOrigem.numeroDaConta", target = "contaOrigem")
    @Mapping(source = "contaDestino.numeroDaConta", target = "contaDestino")
    TransacaoDTO toDTO(Transacao transacao);
    @Mapping(source = "contaOrigem.numeroDaConta", target = "contaOrigem")
    @Mapping(source = "contaDestino.numeroDaConta", target = "contaDestino")
    @Mapping(source = "id", target = "transacaoId")
    ExtratoDTO toExtratoDTO(Transacao transacao);
    @Mapping(source = "contaOrigem", target = "contaOrigem.numeroDaConta")
    @Mapping(source = "contaDestino", target = "contaDestino.numeroDaConta")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataHora", ignore = true)
    Transacao toEntity(TransacaoDTO transacaoDTO);
}
