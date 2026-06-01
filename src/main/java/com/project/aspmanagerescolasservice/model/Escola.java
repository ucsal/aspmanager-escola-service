package com.project.aspmanagerescolasservice.model;

import com.project.aspmanagerescolasservice.model.enums.StatusRegistro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "escolas")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Escola {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(name = "status_registro", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusRegistro statusRegistro = StatusRegistro.ATIVO;
    @ManyToOne
    @JoinColumn(name = "id_instituicao", nullable = false)
    private InstituicaoEnsino instituicao;
    @Column(name = "id_professor_coordenador")
    private Long coordenadorId;
    @OneToMany(mappedBy = "escola")
    private List<Disciplina> disciplinas;

}
