package com.project.aspmanagerescolasservice.mapper;

import com.project.aspmanagerescolasservice.dto.request.CreateEscolaRequest;
import com.project.aspmanagerescolasservice.dto.request.UpdateEscolaRequest;
import com.project.aspmanagerescolasservice.dto.response.EscolaResponse;
import com.project.aspmanagerescolasservice.model.Disciplina;
import com.project.aspmanagerescolasservice.model.Escola;
import com.project.aspmanagerescolasservice.model.InstituicaoEnsino;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EscolaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statusRegistro", ignore = true)
    @Mapping(target = "disciplinas", ignore = true)
    @Mapping(target = "instituicao", source = "idInstituicao", qualifiedByName = "instituicaoFromId")
    @Mapping(target = "coordenadorId", source = "idCoordenador")
    Escola toEntity(CreateEscolaRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "disciplinas", ignore = true)
    @Mapping(target = "instituicao", source = "idInstituicao", qualifiedByName = "instituicaoFromId")
    @Mapping(target = "coordenadorId", source = "idCoordenador")
    void updateEntity(UpdateEscolaRequest request, @MappingTarget Escola escola);

    @Mapping(target = "idInstituicao", source = "instituicao", qualifiedByName = "instituicaoToId")
    @Mapping(target = "idCoordenador", source = "coordenadorId")
    @Mapping(target = "idsDisciplinas", source = "disciplinas", qualifiedByName = "disciplinasToIds")
    EscolaResponse toResponse(Escola escola);

    @Named("instituicaoFromId")
    default InstituicaoEnsino instituicaoFromId(Long idInstituicao) {
        if (idInstituicao == null) {
            return null;
        }

        InstituicaoEnsino instituicao = new InstituicaoEnsino();
        instituicao.setId(idInstituicao);
        return instituicao;
    }

    @Named("instituicaoToId")
    default Long instituicaoToId(InstituicaoEnsino instituicao) {
        return instituicao == null ? null : instituicao.getId();
    }

    @Named("disciplinasToIds")
    default List<Long> disciplinasToIds(List<Disciplina> disciplinas) {
        if (disciplinas == null || disciplinas.isEmpty()) {
            return Collections.emptyList();
        }

        return disciplinas.stream().map(Disciplina::getId).toList();
    }
}
