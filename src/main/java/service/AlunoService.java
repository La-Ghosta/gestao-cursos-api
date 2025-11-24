package com.gestaocursos.gestaocursosapi.service;

import com.gestaocursos.gestaocursosapi.model.Aluno;
import com.gestaocursos.gestaocursosapi.repository.AlunoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    /**
     * Lista todos os alunos cadastrados no banco de dados.
     * @return Lista de objetos Aluno.
     */
    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    /**
     * Busca um aluno pelo ID.
     * @param id O ID do aluno a ser buscado.
     * @return O objeto Aluno encontrado.
     * @throws EntityNotFoundException Se o ID não existir.
     */
    public Aluno findById(Long id) {
        Optional<Aluno> optional = alunoRepository.findById(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("Aluno não encontrado com ID: " + id));
    }

    /**
     * Matricula um novo aluno em um curso.
     * Requer que o curso associado já exista.
     * @param aluno O objeto Aluno a ser salvo.
     * @return O aluno salvo com ID gerado.
     */
    public Aluno save(Aluno aluno) {
        return alunoRepository.save(aluno);
    }

    /**
     * Atualiza os dados de um aluno existente pelo ID.
     * @param id O ID do aluno a atualizar.
     * @param alunoAtualizado Os novos dados do aluno.
     * @return O aluno atualizado.
     * @throws EntityNotFoundException Se o ID não existir.
     */
    public Aluno update(Long id, Aluno alunoAtualizado) {
        Aluno alunoExistente = findById(id);
        alunoExistente.setNome(alunoAtualizado.getNome());
        alunoExistente.setEmail(alunoAtualizado.getEmail());
        alunoExistente.setMatricula(alunoAtualizado.getMatricula());

        // Atualiza curso só se fornecido (evita nullificar)
        if (alunoAtualizado.getCurso() != null) {
            alunoExistente.setCurso(alunoAtualizado.getCurso());
        }

        return alunoRepository.save(alunoExistente);
    }

    /**
     * Remove um aluno do sistema pelo ID.
     * @param id O ID do aluno a deletar.
     * @throws EntityNotFoundException Se o ID não existir.
     */
    public void delete(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new EntityNotFoundException("Aluno não encontrado com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }
}