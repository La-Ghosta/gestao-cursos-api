package com.gestaocursos.gestaocursosapi.service;

import com.gestaocursos.gestaocursosapi.model.Instrutor;
import com.gestaocursos.gestaocursosapi.repository.InstrutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para InstrutorService.
 * Valida todas as operações CRUD e tratamento de exceções.
 */
class InstrutorServiceTest {

    @Mock
    private InstrutorRepository instrutorRepository;

    @InjectMocks
    private InstrutorService instrutorService;

    private Instrutor instrutor;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Prepara um instrutor de exemplo para os testes
        instrutor = new Instrutor("Ana Souza", "ana@test.com", "POO");
        instrutor.setId(1L);
    }

    // ===== TESTES DO findAll() =====

    @Test
    void findAll_DeveRetornarListaVazia_QuandoNaoHaInstrutores() {
        // Arrange
        when(instrutorRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Instrutor> resultado = instrutorService.findAll();

        // Assert
        assertTrue(resultado.isEmpty());
        verify(instrutorRepository, times(1)).findAll();
    }

    @Test
    void findAll_DeveRetornarListaDeInstrutores_QuandoExistem() {
        // Arrange
        Instrutor instrutor2 = new Instrutor("Carlos Lima", "carlos@test.com", "Databases");
        instrutor2.setId(2L);
        when(instrutorRepository.findAll()).thenReturn(Arrays.asList(instrutor, instrutor2));

        // Act
        List<Instrutor> resultado = instrutorService.findAll();

        // Assert
        assertEquals(2, resultado.size());
        assertEquals("Ana Souza", resultado.get(0).getNome());
        assertEquals("Carlos Lima", resultado.get(1).getNome());
        verify(instrutorRepository, times(1)).findAll();
    }

    // ===== TESTES DO findById() =====

    @Test
    void findById_DeveRetornarInstrutor_QuandoIdExiste() {
        // Arrange
        when(instrutorRepository.findById(1L)).thenReturn(Optional.of(instrutor));

        // Act
        Instrutor resultado = instrutorService.findById(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Ana Souza", resultado.getNome());
        assertEquals("ana@test.com", resultado.getEmail());
        verify(instrutorRepository, times(1)).findById(1L);
    }

    @Test
    void findById_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(instrutorRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> instrutorService.findById(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        assertTrue(exception.getMessage().contains("Instrutor não encontrado"));
        verify(instrutorRepository, times(1)).findById(999L);
    }

    // ===== TESTES DO save() =====

    @Test
    void save_DeveSalvarInstrutor_QuandoDadosValidos() {
        // Arrange
        Instrutor novoInstrutor = new Instrutor("João Silva", "joao@test.com", "Matemática");
        when(instrutorRepository.save(novoInstrutor)).thenReturn(novoInstrutor);

        // Act
        Instrutor resultado = instrutorService.save(novoInstrutor);

        // Assert
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(instrutorRepository, times(1)).save(novoInstrutor);
    }

    @Test
    void save_DeveRetornarInstrutorComId_QuandoSalvo() {
        // Arrange
        Instrutor novoInstrutor = new Instrutor("Maria Clara", "maria@test.com", "Física");
        Instrutor instrutorSalvo = new Instrutor("Maria Clara", "maria@test.com", "Física");
        instrutorSalvo.setId(5L);
        when(instrutorRepository.save(novoInstrutor)).thenReturn(instrutorSalvo);

        // Act
        Instrutor resultado = instrutorService.save(novoInstrutor);

        // Assert
        assertEquals(5L, resultado.getId());
        verify(instrutorRepository, times(1)).save(novoInstrutor);
    }

    // ===== TESTES DO update() =====

    @Test
    void update_DeveAtualizarInstrutor_QuandoIdExiste() {
        // Arrange
        Instrutor instrutorAtualizado = new Instrutor("Ana Souza Atualizada", "ana_novo@test.com", "POO Avançada");
        when(instrutorRepository.findById(1L)).thenReturn(Optional.of(instrutor));
        when(instrutorRepository.save(any(Instrutor.class))).thenReturn(instrutor);

        // Act
        Instrutor resultado = instrutorService.update(1L, instrutorAtualizado);

        // Assert
        assertEquals("Ana Souza Atualizada", resultado.getNome());
        assertEquals("ana_novo@test.com", resultado.getEmail());
        assertEquals("POO Avançada", resultado.getEspecialidade());
        verify(instrutorRepository, times(1)).findById(1L);
        verify(instrutorRepository, times(1)).save(instrutor);
    }

    @Test
    void update_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        Instrutor instrutorAtualizado = new Instrutor("Nome Qualquer", "email@test.com", "Especialidade");
        when(instrutorRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class,
                () -> instrutorService.update(999L, instrutorAtualizado)
        );
        verify(instrutorRepository, times(1)).findById(999L);
        verify(instrutorRepository, never()).save(any());
    }

    // ===== TESTES DO delete() =====

    @Test
    void delete_DeveRemoverInstrutor_QuandoIdExiste() {
        // Arrange
        when(instrutorRepository.existsById(1L)).thenReturn(true);

        // Act
        instrutorService.delete(1L);

        // Assert
        verify(instrutorRepository, times(1)).existsById(1L);
        verify(instrutorRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_DeveLancarExcecao_QuandoIdNaoExiste() {
        // Arrange
        when(instrutorRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> instrutorService.delete(999L)
        );

        assertTrue(exception.getMessage().contains("999"));
        verify(instrutorRepository, times(1)).existsById(999L);
        verify(instrutorRepository, never()).deleteById(any());
    }

    @Test
    void delete_NaoDeveChamarDeleteById_QuandoInstrutorNaoExiste() {
        // Arrange
        when(instrutorRepository.existsById(888L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> instrutorService.delete(888L));
        verify(instrutorRepository, never()).deleteById(888L);
    }
}