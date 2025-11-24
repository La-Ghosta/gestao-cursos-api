package com.gestaocursos.gestaocursosapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instrutores")
public class Instrutor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "especialidade", nullable = false, length = 100)
    private String especialidade;

    // Relacionamento: Um Instrutor ministra vários Cursos (1:N)
    @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference  // Serializa Cursos sem loop infinito
    private List<Curso> cursos = new ArrayList<>();

    // Construtores
    public Instrutor() {}

    public Instrutor(String nome, String email, String especialidade) {
        this.nome = nome;
        this.email = email;
        this.especialidade = especialidade;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public List<Curso> getCursos() { return cursos; }
    public void setCursos(List<Curso> cursos) { this.cursos = cursos; }
}