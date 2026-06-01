package com.project.aspmanagerescolasservice.service;

import com.project.aspmanagerescolasservice.dto.request.CreateInstituicaoEnsinoRequest;
import com.project.aspmanagerescolasservice.dto.request.UpdateInstituicaoEnsinoRequest;
import com.project.aspmanagerescolasservice.dto.response.InstituicaoEnsinoResponse;
import com.project.aspmanagerescolasservice.mapper.InstituicaoEnsinoMapper;
import com.project.aspmanagerescolasservice.model.InstituicaoEnsino;
import com.project.aspmanagerescolasservice.repository.InstituicaoEnsinoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class InstituicaoService {

    private final InstituicaoEnsinoRepository instituicoes;
    private final InstituicaoEnsinoMapper instituicaoEnsinoMapper;

    public InstituicaoService(InstituicaoEnsinoRepository instituicoes, InstituicaoEnsinoMapper instituicaoEnsinoMapper) {
        this.instituicoes = instituicoes;
        this.instituicaoEnsinoMapper = instituicaoEnsinoMapper;
    }

    @Transactional
    public InstituicaoEnsinoResponse criar(CreateInstituicaoEnsinoRequest createInstituicaoEnsinoRequest) {

        InstituicaoEnsino instituicao = instituicaoEnsinoMapper.toEntity(createInstituicaoEnsinoRequest);
        return instituicaoEnsinoMapper.toResponse(instituicoes.save(instituicao));
    }

    public Page<InstituicaoEnsinoResponse> buscarTodos(Pageable filtros) {
        return instituicoes.findAll(filtros).map(instituicaoEnsinoMapper::toResponse);
    }

    public InstituicaoEnsinoResponse buscar(Long id) {

        InstituicaoEnsino instituicao = instituicoes.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instituição de Ensino não encontrada!"));

        return instituicaoEnsinoMapper.toResponse(instituicao);
    }

    @Transactional
    public InstituicaoEnsinoResponse atualizar(Long id, UpdateInstituicaoEnsinoRequest updateInstituicaoEnsinoRequest) {

        InstituicaoEnsino instituicao = instituicoes.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instituição de Ensino não encontrada!"));

        instituicaoEnsinoMapper.updateEntity(updateInstituicaoEnsinoRequest, instituicao);

        return instituicaoEnsinoMapper.toResponse(instituicao);
    }

    @Transactional
    public void deletar(Long id) {

        try {
            instituicoes.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("A Instituição de Ensino está associada a alguma Escola!");
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Instituição de Ensino não encontrada!");
        }
    }
}
