package com.project.aspmanagerescolasservice.controller;

import com.project.aspmanagerescolasservice.dto.ErroApiResponse;
import com.project.aspmanagerescolasservice.dto.request.CreateEscolaRequest;
import com.project.aspmanagerescolasservice.dto.request.UpdateEscolaRequest;
import com.project.aspmanagerescolasservice.dto.response.EscolaResponse;
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
@RequestMapping("/api/v1/escola")
@Tag(name = "Escolas", description = "Gestão de escolas acadêmicas")
public class EscolaController {

    private final EscolaService escolaService;

    public EscolaController(EscolaService escolaService) {
        this.escolaService = escolaService;
    }

    @PostMapping
    @Operation(operationId = "createEscola", summary = "Criar escola", description = "Cadastra uma nova escola.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Escola criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content(schema = @Schema(implementation = ErroApiResponse.class))),
            @ApiResponse(responseCode = "404", description = "Instituição não encontrada", content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    public ResponseEntity<EscolaResponse> criar(
            @Valid @RequestBody CreateEscolaRequest request, UriComponentsBuilder uriBuilder) {

        EscolaResponse response = escolaService.criar(request);
        URI uri = location(response, uriBuilder);

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @Operation(operationId = "listEscolas", summary = "Listar escolas", description = "Retorna uma lista paginada de escolas.")
    public ResponseEntity<Page<EscolaResponse>> buscarTodos(
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(escolaService.buscarTodos(pageable));
    }

    @GetMapping("/{id}")
    @Operation(operationId = "getEscolaById", summary = "Buscar escola por ID", description = "Retorna os dados de uma escola específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escola encontrada"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada", content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    public ResponseEntity<EscolaResponse> buscar(
            @Parameter(description = "ID da escola", example = "1")
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(escolaService.buscar(id));
    }

    @PutMapping("/{id}")
    @Operation(operationId = "updateEscola", summary = "Atualizar escola", description = "Atualiza os dados de uma escola.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Escola atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada", content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    public ResponseEntity<EscolaResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UpdateEscolaRequest request
    ) {

        return ResponseEntity.ok(escolaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(operationId = "deleteEscola", summary = "Excluir escola", description = "Exclui ou inativa uma escola.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Escola removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Escola não encontrada", content = @Content(schema = @Schema(implementation = ErroApiResponse.class)))
    })
    public ResponseEntity<Void> deletar(
            @Parameter(description = "ID da escola", example = "1")
            @PathVariable Long id
    ) {

        escolaService.deletar(id);

        return ResponseEntity.noContent().build();
    }

    protected URI location(EscolaResponse escola, UriComponentsBuilder uriBuilder) {
        return uriBuilder.path("/api/v1/escola/{id}").buildAndExpand(escola.id()).toUri();
    }
}
