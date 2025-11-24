package com.gestaocursos.gestaocursosapi.service;

import com.gestaocursos.gestaocursosapi.model.Curso;
import com.gestaocursos.gestaocursosapi.model.Instrutor;
import com.gestaocursos.gestaocursosapi.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para CursoService.
 * Valida todas as operações CRUD e tratamento de exceções.
 */
class CursoServiceTest {

    @Mock
    private CursoRepository cursoRepository;

    @InjectMocks
    private CursoService cursoService;

    private Curso curso;
    private Instrutor instrutor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Prepara um instrutor e curso de exemplo
        instrutor = new Instrutor("Ana Souza", "ana@test.com", "POO");
        instrutor.setId(1L);

        curso = new Curso("Spring Boot Básico", "Introdução ao Spring",
                LocalDate.of(2025, 2, 1), instrutor);
        curso.setId(1L);
    }

    // ===== TESTES DO findAll() =====

    @Test
    void findAll_DeveRetornarListaVazia_QuandoNaoHaCursos() {
        // Arrange
        when(cursoRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Curso> resultado = cursoService.findAll();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(cursoRepository, times(1)).findAll();
    }

    @Test
    void findAll_DeveRetornarListaDeCursos_QuandoExistem() {
        // Arrange
        Curso curso2 = new Curso("JPA Avançado", "Persistência com JPA",
                LocalDate.of(2025, 3, 1), instrutor);
        curso2.setId(2L);
        when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso, curso2));

        // Act
        List<Curso> resultado = cursoService.findAll();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Spring Boot Básico", resultado.get(0).getNome());
        assertEquals("JPA Avançado", resultado.get(1).getNome());
        verify(cursoRepository, times(1)).findAll();
    }

    // ===== TESTES DO findById() =====

    @Test
    void findById_DeveRetornarCurso_QuandoIdExiste() {
        // Arrange
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        // Act
        Curso resultado = cursoService.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Spring Boot Básico", resultado.getNome());
        assertEquals("Introdução ao Spring", resultado.getDescricao());
        assertNotNull(resultado.getInstrutor());
        verify(cursoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> cursoService.findById(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        assertTrue(exception.getMessage().contains("Curso não encontrado"));
        verify(cursoRepository, times(1)).findById(999L);
    }

    // ===== TESTES DO save() =====

    @Test
    void save_DeveSalvarCurso_QuandoDadosValidos() {
        // Arrange
        Curso novoCurso = new Curso("REST APIs", "Desenvolvimento de APIs",
                LocalDate.of(2025, 4, 1), instrutor);
        when(cursoRepository.save(novoCurso)).thenReturn(novoCurso);

        // Act
        Curso resultado = cursoService.save(novoCurso);

        // Assert
        assertNotNull(resultado);
        assertEquals("REST APIs", resultado.getNome());
        verify(cursoRepository, times(1)).save(novoCurso);
    }

    @Test
    void save_DeveRetornarCursoComId_QuandoSalvo() {
        // Arrange
        Curso novoCurso = new Curso("Microservices", "Arquitetura de microsserviços",
                LocalDate.of(2025, 5, 1), instrutor);
        Curso cursoSalvo = new Curso("Microservices", "Arquitetura de microsserviços",
                LocalDate.of(2025, 5, 1), instrutor);
        cursoSalvo.setId(10L);
        when(cursoRepository.save(novoCurso)).thenReturn(cursoSalvo);

        // Act
        Curso resultado = cursoService.save(novoCurso);

        // Assert
        assertEquals(10L, resultado.getId());
        verify(cursoRepository, times(1)).save(novoCurso);
    }

    @Test
    void save_DeveManterInstrutor_QuandoAssociado() {
        // Arrange
        Curso novoCurso = new Curso("Docker", "Containerização",
                LocalDate.of(2025, 6, 1), instrutor);
        when(cursoRepository.save(novoCurso)).thenReturn(novoCurso);

        // Act
        Curso resultado = cursoService.save(novoCurso);

        // Assert
        assertNotNull(resultado.getInstrutor());
        assertEquals(1L, resultado.getInstrutor().getId());
        verify(cursoRepository, times(1)).save(novoCurso);
    }

    // ===== TESTES DO update() =====

    @Test
    void update_DeveAtualizarCurso_QuandoIdExiste() {
        // Arrange
        Curso cursoAtualizado = new Curso("Spring Boot Avançado", "Conteúdo avançado",
                LocalDate.of(2025, 3, 15), instrutor);
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        // Act
        Curso resultado = cursoService.update(1L, cursoAtualizado);

        // Assert
        assertEquals("Spring Boot Avançado", resultado.getNome());
        assertEquals("Conteúdo avançado", resultado.getDescricao());
        assertEquals(LocalDate.of(2025, 3, 15), resultado.getDataInicio());
        verify(cursoRepository, times(1)).findById(1L);
        verify(cursoRepository, times(1)).save(curso);
    }

    @Test
    void update_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        Curso cursoAtualizado = new Curso("Nome Qualquer", "Desc", LocalDate.now(), instrutor);
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> cursoService.update(999L, cursoAtualizado)
        );
        verify(cursoRepository, times(1)).findById(999L);
        verify(cursoRepository, never()).save(any());
    }

    @Test
    void update_DeveManterInstrutorOriginal_QuandoNovoInstrutorNull() {
        // Arrange
        Instrutor instrutorOriginal = curso.getInstrutor();
        Curso cursoAtualizado = new Curso("Novo Nome", "Nova Desc", LocalDate.now(), null);
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        // Act
        Curso resultado = cursoService.update(1L, cursoAtualizado);

        // Assert
        assertEquals(instrutorOriginal, resultado.getInstrutor());
        verify(cursoRepository, times(1)).save(curso);
    }

    // ===== TESTES DO delete() =====

    @Test
    void delete_DeveRemoverCurso_QuandoIdExiste() {
        // Arrange
        when(cursoRepository.existsById(1L)).thenReturn(true);

        // Act
        cursoService.delete(1L);

        // Assert
        verify(cursoRepository, times(1)).existsById(1L);
        verify(cursoRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(cursoRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> cursoService.delete(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(cursoRepository, times(1)).existsById(999L);
        verify(cursoRepository, never()).deleteById(any());
    }

    @Test
    void delete_NaoDeveChamarDeleteById_QuandoCursoNaoExiste() {
        // Arrange
        when(cursoRepository.existsById(777L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> cursoService.delete(777L));
        verify(cursoRepository, never()).deleteById(777L);
    }
}