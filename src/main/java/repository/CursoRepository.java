package com.gestaocursos.gestaocursosapi.repository;

import com.gestaocursos.gestaocursosapi.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
}