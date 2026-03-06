\# GestãoCursos API — Sistema de Gerenciamento de Cursos



API RESTful desenvolvida em \*\*Java 21 com Spring Boot\*\*, voltada ao gerenciamento de alunos, instrutores e cursos. Projeto acadêmico da disciplina de Programação Orientada a Objetos (POO).



\## Sobre o Projeto



Sistema completo com \*\*15 endpoints\*\*, \*\*3 entidades JPA\*\* com relacionamentos complexos, integração com \*\*PostgreSQL\*\* e documentação interativa via \*\*Swagger\*\*.



O desenvolvimento foi realizado de forma majoritariamente individual — estudei Java e Spring Boot do zero através de curso na Udemy e apliquei diretamente na construção do projeto.



\## Tecnologias Utilizadas



\- \*\*Linguagem:\*\* Java 21

\- \*\*Framework:\*\* Spring Boot 3.3.5

\- \*\*Persistência:\*\* Spring Data JPA + Hibernate

\- \*\*Banco de Dados:\*\* PostgreSQL

\- \*\*Gerenciador de Dependências:\*\* Maven

\- \*\*Documentação:\*\* Springdoc OpenAPI (Swagger)

\- \*\*Testes:\*\* JUnit 5 + Mockito



\## Arquitetura



O projeto segue a arquitetura em camadas do Spring Boot:



```

Controller → Service → Repository → Model

```



Inclui tratamento global de exceções com `@RestControllerAdvice`.



\## Entidades e Relacionamentos



| Relacionamento     | Tipo        |

| ------------------ | ----------- |

| Instrutor → Cursos | One-to-Many |

| Curso → Alunos     | One-to-Many |

| Aluno → Curso      | Many-to-One |



Utilização de `@JsonManagedReference` e `@JsonBackReference` para evitar loops na serialização JSON.



\## Endpoints (15 no total)



Cada entidade possui CRUD completo:



| Recurso     | Base URL           |

| ----------- | ------------------ |

| Instrutores | `/api/instrutores` |

| Cursos      | `/api/cursos`      |

| Alunos      | `/api/alunos`      |



Operações: `GET /` · `GET /{id}` · `POST /` · `PUT /{id}` · `DELETE /{id}`



\## Boas Práticas Aplicadas



\- Princípios \*\*SOLID\*\*

\- \*\*Clean Code\*\*

\- Testes unitários em todas as camadas de Service

\- Tratamento padronizado de exceções (404, 500)

\- Documentação completa via Swagger



\## Como Executar



1\. Clone o repositório

2\. Configure o PostgreSQL local (banco: `gestao\_cursos`)

3\. Ajuste as credenciais em `application.properties`

4\. Execute com `mvn spring-boot:run`

5\. Acesse a documentação em `http://localhost:8080/swagger-ui.html`



\## Autor



\*\*Guilherme Holanda\*\* — Estudante de Engenharia de Software

