package com.gestaocursos.gestaocursosapi.service;

import com.gestaocursos.gestaocursosapi.model.Curso;
import com.gestaocursos.gestaocursosapi.repository.CursoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    /**
     * Lista todos os cursos cadastrados no banco de dados.
     * @return Lista de objetos Curso.
     */
    public List<Curso> findAll() {
        return cursoRepository.findAll();
    }

    /**
     * Busca um curso pelo ID.
     * @param id O ID do curso a ser buscado.
     * @return O objeto Curso encontrado.
     * @throws EntityNotFoundException Se o ID não existir.
     */
    public Curso findById(Long id) {
        Optional<Curso> optional = cursoRepository.findById(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("Curso não encontrado com ID: " + id));
    }

    /**
     * Salva um novo curso no banco de dados.
     * Requer que o instrutor associado já exista.
     * @param curso O objeto Curso a ser salvo.
     * @return O curso salvo com ID gerado.
     */
    public Curso save(Curso curso) {
        return cursoRepository.save(curso);
    }

    /**
     * Atualiza um curso existente pelo ID.
     * @param id O ID do curso a atualizar.
     * @param cursoAtualizado Os novos dados do curso.
     * @return O curso atualizado.
     * @throws EntityNotFoundException Se o ID não existir.
     */
    public Curso update(Long id, Curso cursoAtualizado) {
        Curso cursoExistente = findById(id);
        cursoExistente.setNome(cursoAtualizado.getNome());
        cursoExistente.setDescricao(cursoAtualizado.getDescricao());
        cursoExistente.setDataInicio(cursoAtualizado.getDataInicio());

        // Atualiza instrutor só se fornecido (evita nullificar acidentalmente)
        if (cursoAtualizado.getInstrutor() != null) {
            cursoExistente.setInstrutor(cursoAtualizado.getInstrutor());
        }

        return cursoRepository.save(cursoExistente);
    }

    /**
     * Deleta um curso pelo ID.
     * @param id O ID do curso a deletar.
     * @throws EntityNotFoundException Se o ID não existir.
     */
    public void delete(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new EntityNotFoundException("Curso não encontrado com ID: " + id);
        }
        cursoRepository.deleteById(id);
    }
}