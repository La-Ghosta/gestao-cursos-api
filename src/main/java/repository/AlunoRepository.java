package com.gestaocursos.gestaocursosapi.repository;

import com.gestaocursos.gestaocursosapi.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}