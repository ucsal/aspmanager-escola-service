package com.project.aspmanagerescolasservice.controller;

import com.project.aspmanagerescolasservice.dto.request.CreateInstituicaoEnsinoRequest;
import com.project.aspmanagerescolasservice.dto.request.UpdateInstituicaoEnsinoRequest;
import com.project.aspmanagerescolasservice.dto.response.InstituicaoEnsinoResponse;
import com.project.aspmanagerescolasservice.service.InstituicaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/v1/instituicao")
@Tag(name = "Instituições", description = "Gestão de Instituições de Ensino Superior (IES)")
public class InstituicaoController  {
    private final InstituicaoService instituicaoService;

    public InstituicaoController(InstituicaoService instituicaoService) {
        this.instituicaoService = instituicaoService;
    }

    @PostMapping
    @Operation(summary = "Criar instituição")
    public ResponseEntity<InstituicaoEnsinoResponse> criar(
            @Valid @RequestBody CreateInstituicaoEnsinoRequest request,
            UriComponentsBuilder uriBuilder
    ) {

        InstituicaoEnsinoResponse response = instituicaoService.criar(request);

        URI uri = location(response, uriBuilder);

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar instituições")
    public ResponseEntity<Page<InstituicaoEnsinoResponse>> buscarTodos(
            @ParameterObject Pageable pageable
    ) {

        return ResponseEntity.ok(instituicaoService.buscarTodos(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar instituição por ID")
    public ResponseEntity<InstituicaoEnsinoResponse> buscar(
            @Parameter(description = "ID da instituição")
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(instituicaoService.buscar(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar instituição")
    public ResponseEntity<InstituicaoEnsinoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UpdateInstituicaoEnsinoRequest request
    ) {

        return ResponseEntity.ok(instituicaoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar instituição")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {

        instituicaoService.deletar(id);

        return ResponseEntity.noContent().build();
    }

    protected URI location(
            InstituicaoEnsinoResponse instituicaoEnsino,
            UriComponentsBuilder uriBuilder
    ) {

        return uriBuilder
                .path("/api/v1/instituicao/{id}")
                .buildAndExpand(instituicaoEnsino.id())
                .toUri();
    }
}
