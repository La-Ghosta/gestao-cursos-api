package com.gestaocursos.gestaocursosapi.controller;

import com.gestaocursos.gestaocursosapi.model.Aluno;
import com.gestaocursos.gestaocursosapi.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
@Tag(name = "Alunos", description = "Gerenciamento de Alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    @Operation(summary = "Lista todos os alunos")
    public ResponseEntity<List<Aluno>> listar() {
        return ResponseEntity.ok(alunoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um aluno por ID")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(alunoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Matricula um aluno em um curso")
    public ResponseEntity<Aluno> criar(@RequestBody Aluno aluno) {
        Aluno salvo = alunoService.save(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza dados do aluno")
    public ResponseEntity<Aluno> atualizar(@PathVariable Long id, @RequestBody Aluno aluno) {
        return ResponseEntity.ok(alunoService.update(id, aluno));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um aluno")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        alunoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}