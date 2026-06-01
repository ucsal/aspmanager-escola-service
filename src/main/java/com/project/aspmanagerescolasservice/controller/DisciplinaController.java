package com.project.aspmanagerescolasservice.controller;

import com.project.aspmanagerescolasservice.dto.ErroApiResponse;
import com.project.aspmanagerescolasservice.dto.request.CreateDisciplinaRequest;
import com.project.aspmanagerescolasservice.dto.request.UpdateDisciplinaRequest;
import com.project.aspmanagerescolasservice.dto.response.DisciplinaResponse;
import com.project.aspmanagerescolasservice.service.EscolaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/disciplina")
@Tag(name = "Disciplinas", description = "Gestão de disciplinas acadêmicas")
public class DisciplinaController {

    private final EscolaService escolaService;

    public DisciplinaController(EscolaService escolaService) {
        this.escolaService = escolaService;
    }

    @PostMapping
    @Operation(operationId = "createDisciplina", summary = "Criar disciplina", description = "Cadastra uma nova disciplina vinculada a uma escola.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Disciplina criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para criação da disciplina", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    public ResponseEntity<DisciplinaResponse> criarDisciplina(
            @Valid @RequestBody CreateDisciplinaRequest createDisciplina,
            UriComponentsBuilder uriBuilder) {
        DisciplinaResponse disciplinaResponse = escolaService.criarDisciplina(createDisciplina);
        URI uri = disciplinaLocation(disciplinaResponse, uriBuilder);

        return ResponseEntity.created(uri).body(disciplinaResponse);
    }

    @GetMapping
    @Operation(operationId = "listDisciplinas", summary = "Listar disciplinas", description = "Retorna uma lista paginada de disciplinas cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consulta realizada com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    public ResponseEntity<Page<DisciplinaResponse>> buscarDisciplina(@ParameterObject Pageable filtros) {
        return ResponseEntity.ok(escolaService.buscarDisciplina(filtros));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getDisciplinaById", summary = "Buscar disciplina por ID", description = "Retorna os dados de uma disciplina específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina encontrada"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada", content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    public ResponseEntity<DisciplinaResponse> buscarDisciplina(
            @Parameter(description = "ID da disciplina", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(escolaService.buscarDisciplina(id));
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateDisciplinaById", summary = "Atualizar disciplina", description = "Atualiza os dados de uma disciplina existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Disciplina atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Disciplina ou escola não encontrada", content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    public ResponseEntity<DisciplinaResponse> atualizarDisciplina(
            @Parameter(description = "ID da disciplina", example = "1") @PathVariable Long id,
            @RequestBody @Valid UpdateDisciplinaRequest updateDisciplinaRequest) {
        return ResponseEntity.ok(escolaService.atualizarDisciplina(id, updateDisciplinaRequest));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteDisciplinaById", summary = "Excluir disciplina", description = "Exclui uma disciplina por identificador.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Disciplina excluída com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Disciplina não encontrada", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "409", description = "Disciplina com vínculo de integridade", content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    public ResponseEntity<Void> deletarDisciplina(
            @Parameter(description = "ID da disciplina", example = "1") @PathVariable Long id) {
        escolaService.deletarDisciplina(id);
        return ResponseEntity.noContent().build();
    }

    protected URI disciplinaLocation(DisciplinaResponse disciplina, UriComponentsBuilder uriBuilder) {
        return uriBuilder.path("/api/v1/disciplina/{id}").buildAndExpand(disciplina.id()).toUri();
    }
}
