g![Jtech Logo](http://www.jtech.com.br/wp-content/uploads/2015/06/logo.png)

# jtech-tasklist-backend

## What is

Sistema backend para gerenciamento de tarefas (TODO List) multi-usuário com autenticação JWT e arquitetura em camadas seguindo os princípios SOLID.

## Composite by

- **Java 21**: Linguagem de programação
- **Spring Boot 3.5.5**: Framework principal
- **Spring Security**: Autenticação e autorização
- **Spring Data JPA / Hibernate**: Persistência de dados
- **PostgreSQL**: Banco de dados relacional
- **JWT (JSON Web Tokens)**: Autenticação stateless
- **BCrypt**: Hash seguro de senhas
- **JUnit 5 + Mockito**: Testes unitários e de integração
- **SpringDoc OpenAPI**: Documentação da API (Swagger)

## Services

### Autenticação

- `POST /api/v1/auth/register` - Registro de novos usuários
- `POST /api/v1/auth/login` - Autenticação e geração de token JWT

### Gerenciamento de Tarefas

- `POST /api/v1/tasks` - Criar nova tarefa
- `GET /api/v1/tasks` - Listar todas as tarefas do usuário autenticado
- `GET /api/v1/tasks/{id}` - Buscar tarefa específica
- `PUT /api/v1/tasks/{id}` - Atualizar tarefa
- `DELETE /api/v1/tasks/{id}` - Excluir tarefa

## Helper

### Documentação da API

Acesse a documentação Swagger em: `http://localhost:8080/doc/tasklist/v1/api.html`

**Porta padrão:** A API roda na porta **8080** por padrão. Você pode alterar isso através da variável de ambiente `PORT` ou no arquivo `application.yml`.

## How to use

### Pré-requisitos

- Java 21+
- PostgreSQL 12+
- Gradle 7+

### Configuração do Banco de Dados

1. Crie um banco de dados PostgreSQL:
```sql
CREATE DATABASE tasklist_db;
```

2. Execute o script de migração:
```bash
psql -U postgres -d tasklist_db -f src/main/resources/db/migration/V1__create_tables.sql
```

### Variáveis de Ambiente

Configure as seguintes variáveis de ambiente ou ajuste o `application.yml`:

```bash
PORT=8080
DS_URL=localhost
DS_PORT=5432
DS_DATABASE=tasklist_db
DS_USER=postgres
DS_PASS=postgres
JWT_SECRET=your-secret-key-at-least-256-bits-long
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000
```

## How to run

### Desenvolvimento

**Opção 1: Executar dentro do diretório do backend**
```bash
cd jtech-tasklist-backend
./gradlew bootRun
```

**Opção 2: Executar da raiz do projeto**
```bash
./gradlew :jtech-tasklist-backend:bootRun
```

### Build

**Dentro do diretório do backend:**
```bash
cd jtech-tasklist-backend
./gradlew build
```

**Da raiz do projeto:**
```bash
./gradlew :jtech-tasklist-backend:build
```

### Executar Testes

**Dentro do diretório do backend:**
```bash
cd jtech-tasklist-backend
./gradlew test
```

**Da raiz do projeto:**
```bash
./gradlew :jtech-tasklist-backend:test
```

### Executar Testes com Cobertura

**Dentro do diretório do backend:**
```bash
cd jtech-tasklist-backend
./gradlew test jacocoTestReport
```

**Da raiz do projeto:**
```bash
./gradlew :jtech-tasklist-backend:test :jtech-tasklist-backend:jacocoTestReport
```

## Points to improve

### Arquitetura

A aplicação segue uma arquitetura em camadas (Clean Architecture / Hexagonal Architecture):

```
┌─────────────────────────────────────────┐
│         Adapters (Input)                │
│  - Controllers                          │
│  - Protocols (DTOs)                     │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│         Application Layer               │
│  - Use Cases                            │
│  - Domain Models                        │
│  - Ports (Interfaces)                   │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│         Adapters (Output)               │
│  - Repositories                         │
│  - Entities                             │
└─────────────────────────────────────────┘
```

### Princípios SOLID Aplicados

1. **Single Responsibility Principle (SRP)**: Cada classe tem uma única responsabilidade
   - `AuthUseCase`: Responsável apenas pela lógica de autenticação
   - `TaskUseCase`: Responsável apenas pela lógica de tarefas
   - `JwtTokenProvider`: Responsável apenas pela geração e validação de tokens

2. **Open/Closed Principle (OCP)**: Extensível sem modificar código existente
   - Uso de interfaces (Ports) permite adicionar novas implementações sem alterar use cases

3. **Liskov Substitution Principle (LSP)**: Implementações podem ser substituídas
   - Adapters implementam interfaces (Ports) e podem ser substituídos

4. **Interface Segregation Principle (ISP)**: Interfaces específicas e coesas
   - `AuthInputGateway` e `TaskInputGateway` separados por responsabilidade
   - `AuthOutputGateway` e `TaskOutputGateway` separados

5. **Dependency Inversion Principle (DIP)**: Dependências de abstrações
   - Use cases dependem de interfaces (Ports), não de implementações concretas
   - Injeção de dependência via Spring

### Estrutura de Pastas

```
src/main/java/br/com/jtech/tasklist/
├── adapters/
│   ├── input/
│   │   ├── controllers/          # Controllers REST
│   │   └── protocols/            # DTOs (Request/Response)
│   └── output/
│       ├── repositories/         # JPA Repositories
│       │   └── entities/         # Entidades JPA
│       └── *Adapter.java         # Implementações dos Ports de Output
├── application/
│   ├── core/
│   │   ├── domains/              # Modelos de domínio
│   │   └── usecases/             # Casos de uso (lógica de negócio)
│   └── ports/
│       ├── input/                # Interfaces de entrada (Use Cases)
│       └── output/               # Interfaces de saída (Repositories)
└── config/
    ├── infra/
    │   ├── exceptions/           # Exceções customizadas
    │   ├── security/             # Configuração de segurança
    │   └── utils/                # Utilitários
    └── usecases/                 # Configuração dos Use Cases (Beans)
```

### Decisões Técnicas

1. **Arquitetura em Camadas**: Separação clara de responsabilidades facilita manutenção e testes
2. **Injeção de Dependência**: Spring Framework gerencia dependências automaticamente
3. **JWT Stateless**: Autenticação sem necessidade de sessão no servidor
4. **BCrypt**: Hash seguro de senhas com salt automático
5. **Validação**: Spring Validation para validação de entrada
6. **Exception Handling**: Tratamento centralizado de exceções via `@RestControllerAdvice`
7. **Testes**: Cobertura completa com testes unitários (Mockito) e de integração (Spring Test)

### Melhorias Futuras

1. **Refresh Token**: Implementar endpoint para renovação de tokens
2. **Paginação**: Adicionar paginação nas listagens de tarefas
3. **Filtros**: Implementar filtros por status (completa/pendente)
4. **Cache**: Adicionar cache para melhorar performance
5. **Logging**: Implementar logging estruturado (Logback/Log4j2)
6. **Métricas**: Adicionar métricas com Micrometer
7. **Flyway/Liquibase**: Migração de banco de dados automatizada
8. **Docker**: Containerização da aplicação
9. **CI/CD**: Pipeline de integração e deploy contínuo
10. **Documentação**: Expandir documentação da API com exemplos
