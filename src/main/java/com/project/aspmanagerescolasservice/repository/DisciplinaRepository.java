package com.project.aspmanagerescolasservice.repository;

import com.project.aspmanagerescolasservice.model.Disciplina;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DisciplinaRepository extends JpaRepository<Disciplina, Long> {
	boolean existsByEscola_Id(Long escolaId);
}
