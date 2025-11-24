package com.gestaocursos.gestaocursosapi.service;

import com.gestaocursos.gestaocursosapi.model.Aluno;
import com.gestaocursos.gestaocursosapi.model.Curso;
import com.gestaocursos.gestaocursosapi.model.Instrutor;
import com.gestaocursos.gestaocursosapi.repository.AlunoRepository;
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
 * Testes unitários para AlunoService.
 * Valida todas as operações CRUD e tratamento de exceções.
 */
class AlunoServiceTest {

    @Mock
    private AlunoRepository alunoRepository;

    @InjectMocks
    private AlunoService alunoService;

    private Aluno aluno;
    private Curso curso;
    private Instrutor instrutor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Prepara dados de exemplo
        instrutor = new Instrutor("Ana Souza", "ana@test.com", "POO");
        instrutor.setId(1L);

        curso = new Curso("Spring Boot Básico", "Introdução ao Spring",
                LocalDate.of(2025, 2, 1), instrutor);
        curso.setId(1L);

        aluno = new Aluno("João Pedro", "joao@email.com", "MAT001");
        aluno.setId(1L);
        aluno.setCurso(curso);
    }

    // ===== TESTES DO findAll() =====

    @Test
    void findAll_DeveRetornarListaVazia_QuandoNaoHaAlunos() {
        // Arrange
        when(alunoRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Aluno> resultado = alunoService.findAll();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(alunoRepository, times(1)).findAll();
    }

    @Test
    void findAll_DeveRetornarListaDeAlunos_QuandoExistem() {
        // Arrange
        Aluno aluno2 = new Aluno("Maria Clara", "maria@email.com", "MAT002");
        aluno2.setId(2L);
        aluno2.setCurso(curso);
        when(alunoRepository.findAll()).thenReturn(Arrays.asList(aluno, aluno2));

        // Act
        List<Aluno> resultado = alunoService.findAll();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("João Pedro", resultado.get(0).getNome());
        assertEquals("Maria Clara", resultado.get(1).getNome());
        verify(alunoRepository, times(1)).findAll();
    }

    // ===== TESTES DO findById() =====

    @Test
    void findById_DeveRetornarAluno_QuandoIdExiste() {
        // Arrange
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        // Act
        Aluno resultado = alunoService.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("João Pedro", resultado.getNome());
        assertEquals("joao@email.com", resultado.getEmail());
        assertEquals("MAT001", resultado.getMatricula());
        assertNotNull(resultado.getCurso());
        verify(alunoRepository, times(1)).findById(1L);
    }

    @Test
    void findById_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(alunoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> alunoService.findById(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        assertTrue(exception.getMessage().contains("Aluno não encontrado"));
        verify(alunoRepository, times(1)).findById(999L);
    }

    // ===== TESTES DO save() =====

    @Test
    void save_DeveSalvarAluno_QuandoDadosValidos() {
        // Arrange
        Aluno novoAluno = new Aluno("Lucas Silva", "lucas@email.com", "MAT003");
        novoAluno.setCurso(curso);
        when(alunoRepository.save(novoAluno)).thenReturn(novoAluno);

        // Act
        Aluno resultado = alunoService.save(novoAluno);

        // Assert
        assertNotNull(resultado);
        assertEquals("Lucas Silva", resultado.getNome());
        verify(alunoRepository, times(1)).save(novoAluno);
    }

    @Test
    void save_DeveRetornarAlunoComId_QuandoSalvo() {
        // Arrange
        Aluno novoAluno = new Aluno("Sofia Oliveira", "sofia@email.com", "MAT004");
        novoAluno.setCurso(curso);
        Aluno alunoSalvo = new Aluno("Sofia Oliveira", "sofia@email.com", "MAT004");
        alunoSalvo.setId(15L);
        alunoSalvo.setCurso(curso);
        when(alunoRepository.save(novoAluno)).thenReturn(alunoSalvo);

        // Act
        Aluno resultado = alunoService.save(novoAluno);

        // Assert
        assertEquals(15L, resultado.getId());
        verify(alunoRepository, times(1)).save(novoAluno);
    }

    @Test
    void save_DeveManterCurso_QuandoAssociado() {
        // Arrange
        Aluno novoAluno = new Aluno("Pedro Santos", "pedro@email.com", "MAT005");
        novoAluno.setCurso(curso);
        when(alunoRepository.save(novoAluno)).thenReturn(novoAluno);

        // Act
        Aluno resultado = alunoService.save(novoAluno);

        // Assert
        assertNotNull(resultado.getCurso());
        assertEquals(1L, resultado.getCurso().getId());
        verify(alunoRepository, times(1)).save(novoAluno);
    }

    // ===== TESTES DO update() =====

    @Test
    void update_DeveAtualizarAluno_QuandoIdExiste() {
        // Arrange
        Aluno alunoAtualizado = new Aluno("João Pedro Atualizado", "joao_novo@email.com", "MAT001-NOVO");
        alunoAtualizado.setCurso(curso);
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        // Act
        Aluno resultado = alunoService.update(1L, alunoAtualizado);

        // Assert
        assertEquals("João Pedro Atualizado", resultado.getNome());
        assertEquals("joao_novo@email.com", resultado.getEmail());
        assertEquals("MAT001-NOVO", resultado.getMatricula());
        verify(alunoRepository, times(1)).findById(1L);
        verify(alunoRepository, times(1)).save(aluno);
    }

    @Test
    void update_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        Aluno alunoAtualizado = new Aluno("Nome Qualquer", "email@test.com", "MAT999");
        when(alunoRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> alunoService.update(999L, alunoAtualizado)
        );
        verify(alunoRepository, times(1)).findById(999L);
        verify(alunoRepository, never()).save(any());
    }

    @Test
    void update_DeveManterCursoOriginal_QuandoNovoCursoNull() {
        // Arrange
        Curso cursoOriginal = aluno.getCurso();
        Aluno alunoAtualizado = new Aluno("Novo Nome", "novo@email.com", "MAT777");
        alunoAtualizado.setCurso(null);
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        // Act
        Aluno resultado = alunoService.update(1L, alunoAtualizado);

        // Assert
        assertEquals(cursoOriginal, resultado.getCurso());
        verify(alunoRepository, times(1)).save(aluno);
    }

    @Test
    void update_DevePermitirTrocaDeCurso_QuandoNovoFornecido() {
        // Arrange
        Curso novoCurso = new Curso("JPA Avançado", "Desc", LocalDate.now(), instrutor);
        novoCurso.setId(2L);
        Aluno alunoAtualizado = new Aluno("João Pedro", "joao@email.com", "MAT001");
        alunoAtualizado.setCurso(novoCurso);
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);

        // Act
        Aluno resultado = alunoService.update(1L, alunoAtualizado);

        // Assert
        assertEquals(2L, resultado.getCurso().getId());
        verify(alunoRepository, times(1)).save(aluno);
    }

    // ===== TESTES DO delete() =====

    @Test
    void delete_DeveRemoverAluno_QuandoIdExiste() {
        // Arrange
        when(alunoRepository.existsById(1L)).thenReturn(true);

        // Act
        alunoService.delete(1L);

        // Assert
        verify(alunoRepository, times(1)).existsById(1L);
        verify(alunoRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(alunoRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> alunoService.delete(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(alunoRepository, times(1)).existsById(999L);
        verify(alunoRepository, never()).deleteById(any());
    }

    @Test
    void delete_NaoDeveChamarDeleteById_QuandoAlunoNaoExiste() {
        // Arrange
        when(alunoRepository.existsById(666L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> alunoService.delete(666L));
        verify(alunoRepository, never()).deleteById(666L);
    }
}