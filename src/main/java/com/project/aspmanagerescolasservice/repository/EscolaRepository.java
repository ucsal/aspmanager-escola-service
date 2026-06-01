package com.project.aspmanagerescolasservice.repository;

import com.project.aspmanagerescolasservice.model.Escola;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EscolaRepository extends JpaRepository<Escola, Long> {
    Long id(Long id);
}
