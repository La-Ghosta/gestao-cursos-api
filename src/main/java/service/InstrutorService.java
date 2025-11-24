package com.gestaocursos.gestaocursosapi.service;

import com.gestaocursos.gestaocursosapi.model.Instrutor;
import com.gestaocursos.gestaocursosapi.repository.InstrutorRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstrutorService {

    @Autowired
    private InstrutorRepository instrutorRepository;

    /**
     * Lista todos os instrutores cadastrados no banco de dados.
     * @return Lista de objetos Instrutor.
     */
    public List<Instrutor> findAll() {
        return instrutorRepository.findAll();
    }

    /**
     * Busca um instrutor pelo ID.
     * @param id O ID do instrutor a ser buscado.
     * @return O objeto Instrutor encontrado.
     * @throws EntityNotFoundException Se o ID não existir.
     */
    public Instrutor findById(Long id) {
        Optional<Instrutor> optional = instrutorRepository.findById(id);
        return optional.orElseThrow(() -> new EntityNotFoundException("Instrutor não encontrado com ID: " + id));
    }

    /**
     * Salva um novo instrutor no banco de dados.
     * @param instrutor O objeto Instrutor a ser salvo.
     * @return O instrutor salvo com ID gerado.
     */
    public Instrutor save(Instrutor instrutor) {
        return instrutorRepository.save(instrutor);
    }

    /**
     * Atualiza um instrutor existente pelo ID.
     * @param id O ID do instrutor a atualizar.
     * @param instrutorAtualizado Os novos dados do instrutor.
     * @return O instrutor atualizado.
     * @throws EntityNotFoundException Se o ID não existir.
     */
    public Instrutor update(Long id, Instrutor instrutorAtualizado) {
        Instrutor instrutorExistente = findById(id);
        instrutorExistente.setNome(instrutorAtualizado.getNome());
        instrutorExistente.setEmail(instrutorAtualizado.getEmail());
        instrutorExistente.setEspecialidade(instrutorAtualizado.getEspecialidade());
        return instrutorRepository.save(instrutorExistente);
    }

    /**
     * Deleta um instrutor pelo ID.
     * @param id O ID do instrutor a deletar.
     * @throws EntityNotFoundException Se o ID não existir.
     */
    public void delete(Long id) {
        if (!instrutorRepository.existsById(id)) {
            throw new EntityNotFoundException("Instrutor não encontrado com ID: " + id);
        }
        instrutorRepository.deleteById(id);
    }
}