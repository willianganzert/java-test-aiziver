package com.revizia.reviziabank.mappers.v1;

import com.revizia.reviziabank.dtos.ContaDTO;
import com.revizia.reviziabank.dtos.ContaUpdateDTO;
import com.revizia.reviziabank.domain.models.Conta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ContaMapper {
    ContaDTO toDTO(Conta conta);
    Conta toEntity(ContaDTO contaDTO);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "titular" , ignore = true)
    @Mapping(target = "numeroDaConta" , ignore = true)
    void updateEntityFromDto(ContaUpdateDTO dto, @MappingTarget Conta entity);
}
