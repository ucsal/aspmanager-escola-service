package com.project.aspmanagerescolasservice.service;

import com.project.aspmanagerescolasservice.dto.request.CreateDisciplinaRequest;
import com.project.aspmanagerescolasservice.dto.request.CreateEscolaRequest;
import com.project.aspmanagerescolasservice.dto.request.UpdateDisciplinaRequest;
import com.project.aspmanagerescolasservice.dto.request.UpdateEscolaRequest;
import com.project.aspmanagerescolasservice.dto.response.DisciplinaResponse;
import com.project.aspmanagerescolasservice.dto.response.EscolaResponse;
import com.project.aspmanagerescolasservice.mapper.EscolaMapper;
import com.project.aspmanagerescolasservice.model.Disciplina;
import com.project.aspmanagerescolasservice.model.Escola;
import com.project.aspmanagerescolasservice.repository.DisciplinaRepository;
import com.project.aspmanagerescolasservice.repository.EscolaRepository;

import com.project.aspmanagerescolasservice.model.InstituicaoEnsino;
import com.project.aspmanagerescolasservice.repository.InstituicaoEnsinoRepository;
import com.project.aspmanagerescolasservice.model.enums.StatusRegistro;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EscolaService {

    private final EscolaRepository escolas;
    private final DisciplinaRepository disciplinas;
    private final InstituicaoEnsinoRepository instituicoes;
    private final EscolaMapper escolaMapper;

    public EscolaService(EscolaRepository escolas, DisciplinaRepository disciplinas,
                         InstituicaoEnsinoRepository instituicoes, EscolaMapper escolaMapper) {
        this.escolas = escolas;
        this.disciplinas = disciplinas;
        this.instituicoes = instituicoes;
        this.escolaMapper = escolaMapper;
    }

    @Transactional
    public EscolaResponse criar(CreateEscolaRequest createEscolaRequest) {

        InstituicaoEnsino instituicao = instituicoes.findById(createEscolaRequest.idInstituicao()).
                orElseThrow(() -> new EntityNotFoundException(("Instituição de ensino não encontrada!")));

        List<Long> idsDisciplinas = createEscolaRequest.idsDisciplinas();
        List<Disciplina> disciplinas = new ArrayList<>();

        if (!idsDisciplinas.isEmpty()) {

            for (Long idDisciplina : idsDisciplinas) {

                Optional<Disciplina> disciplina = this.disciplinas.findById(idDisciplina);
                disciplina.ifPresent(disciplinas::add);

            }
        }

        Escola escola = escolaMapper.toEntity(createEscolaRequest);
        escola.setInstituicao(instituicao);
        escola.setDisciplinas(disciplinas);
        escola.setCoordenadorId(createEscolaRequest.idCoordenador());

        return escolaMapper.toResponse(escolas.save(escola));
    }

    public Page<EscolaResponse> buscarTodos(Pageable filtros) {
        return escolas.findAll(filtros).map(escolaMapper::toResponse);
    }

    public EscolaResponse buscar(Long id) {

        Escola escola = escolas.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        return escolaMapper.toResponse(escola);
    }

    @Transactional
    public EscolaResponse atualizar(Long id, UpdateEscolaRequest updateEscolaRequest) {

        Escola escola = escolas.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        InstituicaoEnsino instituicao = instituicoes.findById(updateEscolaRequest.idInstituicao()).
                orElseThrow(() -> new EntityNotFoundException(("Instituição de ensino não encontrada!")));

        escolaMapper.updateEntity(updateEscolaRequest, escola);
        escola.setCoordenadorId(updateEscolaRequest.idCoordenador());

        return escolaMapper.toResponse(escola);
    }

    @Transactional
    public void deletar(Long id) {
        Escola escola = escolas.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        boolean possuiHistorico = disciplinas.existsByEscola_Id(id);
        if (possuiHistorico) {
            escola.setStatusRegistro(StatusRegistro.INATIVO);
            return;
        }

        escolas.delete(escola);
    }

    @Transactional
    public DisciplinaResponse criarDisciplina(CreateDisciplinaRequest createDisciplinaRequest) {

        Escola escola = escolas.findById(createDisciplinaRequest.idEscola()).
                orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        Disciplina disciplina = Disciplina.builder().
                nome(createDisciplinaRequest.nome()).
                descricao(createDisciplinaRequest.descricao()).
                escola(escola).
                build();

        disciplinas.save(disciplina);

        return new DisciplinaResponse(disciplina.getId(), disciplina.getNome(),
                disciplina.getDescricao(), disciplina.getEscola().getId());
    }

    public Page<DisciplinaResponse> buscarDisciplina(Pageable filtros) {
        return disciplinas.findAll(filtros).map(disciplina -> new DisciplinaResponse(disciplina.getId(),
                disciplina.getNome(), disciplina.getDescricao(), disciplina.getEscola().getId()));
    }

    public DisciplinaResponse buscarDisciplina(Long id) {

        Disciplina disciplina = disciplinas.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada!"));

        return new DisciplinaResponse(disciplina.getId(), disciplina.getNome(),
                disciplina.getDescricao(), disciplina.getEscola().getId());
    }

    @Transactional
    public DisciplinaResponse atualizarDisciplina(Long id, UpdateDisciplinaRequest updateDisciplinaRequest) {

        Disciplina disciplina = disciplinas.findById(id).
                orElseThrow(() -> new EntityNotFoundException("Disciplina não encontrada!"));

        Escola escola = escolas.findById(updateDisciplinaRequest.idEscola()).
                orElseThrow(() -> new EntityNotFoundException("Escola não encontrada!"));

        disciplina.setNome(updateDisciplinaRequest.nome());
        disciplina.setDescricao(updateDisciplinaRequest.descricao());
        disciplina.setEscola(escola);

        return new DisciplinaResponse(disciplina.getId(), disciplina.getNome(),
                disciplina.getDescricao(), disciplina.getEscola().getId());
    }

    @Transactional
    public void deletarDisciplina(Long id) {
        try {
            disciplinas.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("A Instituição de Ensino está associada a alguma Escola!");
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Disciplina não encontrada!");
        }

    }
}