package com.gestaocursos.gestaocursosapi.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handler global para tratamento de exceções da API.
 * Retorna respostas HTTP padronizadas com mensagens amigáveis.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções de entidade não encontrada (404).
     * @param e A exceção lançada.
     * @return Resposta HTTP 404 com mensagem de erro.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    /**
     * Trata exceções gerais não previstas (500).
     * @param e A exceção lançada.
     * @return Resposta HTTP 500 com mensagem de erro.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(Exception e) {
        e.printStackTrace();  // Log no console para debug
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro interno: " + e.getMessage());
    }
}