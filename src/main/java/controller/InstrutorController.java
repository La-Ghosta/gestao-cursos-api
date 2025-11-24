package com.gestaocursos.gestaocursosapi.controller;

import com.gestaocursos.gestaocursosapi.model.Instrutor;
import com.gestaocursos.gestaocursosapi.service.InstrutorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instrutores")
@Tag(name = "Instrutores", description = "Gerenciamento de Instrutores")  // Agrupa no Swagger UI
public class InstrutorController {

    @Autowired
    private InstrutorService instrutorService;

    @GetMapping
    @Operation(summary = "Lista todos os instrutores")  // Descrição no Swagger
    public ResponseEntity<List<Instrutor>> listar() {
        return ResponseEntity.ok(instrutorService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um instrutor por ID")
    public ResponseEntity<Instrutor> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(instrutorService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cria um novo instrutor")
    public ResponseEntity<Instrutor> criar(@RequestBody Instrutor instrutor) {
        Instrutor salvo = instrutorService.save(instrutor);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um instrutor existente")
    public ResponseEntity<Instrutor> atualizar(@PathVariable Long id, @RequestBody Instrutor instrutor) {
        return ResponseEntity.ok(instrutorService.update(id, instrutor));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um instrutor")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        instrutorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}