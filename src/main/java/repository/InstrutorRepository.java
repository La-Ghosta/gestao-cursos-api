package com.gestaocursos.gestaocursosapi.repository;

import com.gestaocursos.gestaocursosapi.model.Instrutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrutorRepository extends JpaRepository<Instrutor, Long> {
    // O Spring cria o código sozinho em tempo de execução!
}