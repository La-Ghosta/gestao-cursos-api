package com.gestaocursos.gestaocursosapi.controller;

import com.gestaocursos.gestaocursosapi.model.Curso;
import com.gestaocursos.gestaocursosapi.service.CursoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@Tag(name = "Cursos", description = "Gerenciamento de Cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @GetMapping
    @Operation(summary = "Lista todos os cursos")
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um curso por ID")
    public ResponseEntity<Curso> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cursoService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cria um novo curso (Requer Instrutor)")
    public ResponseEntity<Curso> criar(@RequestBody Curso curso) {
        Curso salvo = cursoService.save(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um curso")
    public ResponseEntity<Curso> atualizar(@PathVariable Long id, @RequestBody Curso curso) {
        return ResponseEntity.ok(cursoService.update(id, curso));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um curso")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        cursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}