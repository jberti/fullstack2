# JTech TaskList - Sistema TODO Multi-usuário

Sistema completo de gerenciamento de tarefas com autenticação JWT, desenvolvido com Vue.js 3 e Spring Boot.

### Contextualização e Objetivo

A **JTech** busca desenvolvedores frontend experientes capazes de construir aplicações robustas e escaláveis com arquitetura bem definida. Este desafio avalia sua competência em gerenciamento de estado complexo, arquitetura modular e implementação de sistemas multi-usuário.

**Objetivo:** Desenvolver uma aplicação frontend sofisticada que simule um sistema TODO List multi-usuário, demonstrando expertise em arquitetura de componentes, gerenciamento de estado avançado e boas práticas de desenvolvimento.

## Especificações Técnicas

### Requisitos Funcionais

#### Sistema de Autenticação Simulada

1. **Interface de Login**: Tela de autenticação com validação de campos não vazios
2. **Autenticação Mock**: Qualquer combinação válida de usuário/senha redireciona para a aplicação
3. **Persistência de Sessão**: Manter dados do usuário logado no estado global da aplicação

#### Gerenciamento Avançado de Listas

1. **Múltiplas Listas de Tarefas**: Usuário pode criar listas categorizadas (ex: "Trabalho", "Estudos", "Pessoal")
2. **CRUD Completo de Listas**:
   * Criar novas listas com nomes personalizados
   * Renomear listas existentes com validação
   * Excluir listas com confirmação e verificação de dependências
3. **Navegação entre Listas**: Interface intuitiva para alternar entre diferentes listas

#### Sistema Completo de Tarefas

1. **Gerenciamento por Lista**: Cada lista mantém suas próprias tarefas independentemente
2. **CRUD de Tarefas**: Adicionar, editar, remover e marcar tarefas como concluídas dentro de cada lista
3. **Validações Avançadas**: Prevenção de duplicatas, validação de campos obrigatórios

#### Persistência e Navegação

1. **Estado Persistente**: Todo o estado (usuário, listas, tarefas) gerenciado pelo Pinia e persistido
2. **Roteamento**: Vue Router para separar autenticação da aplicação principal
3. **Guards de Rota**: Proteção de rotas para usuários não autenticados


### Stack Tecnológica Obrigatória

* **Framework**: Vue 3 (Composition API)
* **Roteamento**: Vue Router 4
* **Gerenciamento de Estado**: Pinia
* **UI Framework**: Material Design (Vuetify ou biblioteca equivalente)
* **Testes**: Vitest para testes unitários abrangentes
* **TypeScript**: Fortemente recomendado para tipagem robusta

# BACKEND

## Especificações Técnicas

### Requisitos Funcionais

#### Sistema de Autenticação Segura

1. **Registro de Usuários**:
   * Endpoint `POST /auth/register` para cadastro com nome, email e senha
   * Implementação de hash seguro de senhas utilizando bcrypt
   * Validação de unicidade de email
2. **Autenticação JWT**:
   * Endpoint `POST /auth/login` para autenticação e geração de token JWT
   * Implementação de refresh token para segurança aprimorada

#### Gerenciamento de Tarefas com Segurança

1. **CRUD Completo de Tarefas**:
   * `POST /tasks`: Criar tarefa associada ao usuário autenticado
   * `GET /tasks`: Listar exclusivamente tarefas do usuário logado
   * `GET /tasks/{id}`: Buscar tarefa específica com validação de propriedade
   * `PUT /tasks/{id}`: Atualizar tarefa com controle de acesso
   * `DELETE /tasks/{id}`: Remover tarefa com validação de proprietário
2. **Autorização Robusta**: Todas as rotas protegidas por JWT com validação de propriedade dos recursos

### Requisitos Não Funcionais

#### Arquitetura e Design Patterns

1. **Princípios SOLID**: Implementação rigorosa dos cinco princípios em todas as camadas
2. **Arquitetura em Camadas**: Estrutura bem definida (Controller → Service → Repository → Domain)
3. **Injeção de Dependência**: Utilização adequada do Spring Framework para IoC
4. **Exception Handling**: Sistema robusto de tratamento centralizado de exceções

#### Qualidade e Testabilidade

1. **Testes Unitários**: Cobertura completa da camada de serviço com cenários de sucesso e falha
2. **Testes de Integração**: Validação end-to-end dos endpoints com Spring Test
3. **Mocks e Stubs**: Utilização adequada de Mockito para isolamento de dependências

### Stack Tecnológica Obrigatória

* **Linguagem**: Java 17+
* **Framework**: Spring Boot, Spring Security, Spring Validation
* **Persistência**: Spring Data JPA com Hibernate
* **Banco de Dados**: PostgreSQL
* **Segurança**: JWT, BCrypt
* **Testes**: JUnit 5, Mockito, Spring Boot Test

## Critérios de Avaliação

* **Aplicação de SOLID**: Demonstração clara e justificada dos princípios SOLID (critério principal)
* **Qualidade Arquitetural**: Design limpo, modular com separação clara de responsabilidades
* **Cobertura de Testes**: Suite robusta e significativa de testes unitários e de integração
* **Implementação de Segurança**: Autenticação e autorização corretamente implementadas
* **Domínio da Stack**: Utilização avançada e adequada do ecossistema Spring
* **Domínio da Stack**: Utilização avançada das ferramentas do ecossistema Vue.js
* **Modelagem de Dados**: Relacionamento bem definido entre entidades User e Task
* **Documentação Técnica**: README detalhado com justificativas arquiteturais

## Expectativa de Entrega

* **Prazo**: Até 3 dias corridos a partir do recebimento.
* **Formato**: Repositório Git com código-fonte completo e documentação detalhada.

### Estrutura Obrigatória do `README.md`

1. **Visão Geral da Arquitetura**: Descrição detalhada da estrutura e decisões arquiteturais
2. **Stack Tecnológica**: Lista completa com justificativas para cada escolha
3. **Como Rodar Localmente**: Instruções passo a passo para setup e execução
4. **Como Rodar os Testes**: Comandos para executar suite completa de testes
5. **Estrutura de Pastas Detalhada**: Mapeamento completo da organização modular do código
6. **Decisões Técnicas Aprofundadas**: Justificativas detalhadas sobre escolhas arquiteturais, padrões e bibliotecas
7. **Melhorias e Roadmap**: Propostas técnicas para evolução e escalabilidade da aplicação

---

## 📝 Para Avaliadores

### Execução Imediata
```bash
# Clone o repositório
git clone <repository-url>
cd <repository-name>

# Execute em um comando
docker-compose up --build
```

### Arquivos de Apoio para Avaliação
- `SETUP-AVALIADOR.md` - Instruções detalhadas de setup
- `CHECKLIST-AVALIADOR.md` - Checklist completo de funcionalidades  
- `TROUBLESHOOTING.md` - Guia de solução de problemas
- `validate-setup.sh` - Script de validação de pré-requisitos
- `start.sh` - Script de inicialização automatizada

### Tempo Estimado de Avaliação
- **Setup**: 5-7 minutos
- **Avaliação funcional**: 15-20 minutos
- **Análise de código**: 10-15 minutos
- **Total**: 30-45 minutos

**Boa sorte! A JTech espera uma solução que demonstre maturidade em desenvolvimento fullstack e visão arquitetural.**

## 🚀 Execução Rápida para Avaliação

### Pré-requisitos
- Docker e Docker Compose instalados
- Portas 5432, 8080 e 5173 disponíveis

### Comando único para executar tudo:
```bash
docker-compose up --build
```

**⏱️ Tempo estimado**: 5-7 minutos do clone até a aplicação rodando

### URLs da aplicação:
- **Frontend**: http://localhost:5173
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

### 🔍 Validação e Debug
```bash
# Verificar pré-requisitos antes de executar
./validate-setup.sh

# Ver status dos containers
docker-compose ps

# Ver logs em tempo real
docker-compose logs -f

# Debug específico do backend
./debug-backend.sh
```

### 🚨 Se o Backend Não Responder
```bash
# Teste rápido do Dockerfile
./test-dockerfile.sh

# Verificar se o container está rodando
docker-compose ps

# Ver logs do backend
docker-compose logs backend

# Rebuild se necessário
docker-compose build --no-cache backend
docker-compose up backend
```

### 🛑 Para parar a aplicação
```bash
docker-compose down
```

## 📁 Estrutura do Projeto

```
├── jtech-tasklist-frontend/     # Vue.js 3 + Vuetify
├── jtech-tasklist-backend/      # Spring Boot + PostgreSQL
├── docker-compose.yml           # Orquestração completa
├── Dockerfile.frontend          # Build do frontend
├── Dockerfile.backend           # Build do backend
└── README.md                    # Esta documentação
```

## 🛠️ Desenvolvimento Local

### Backend (Spring Boot)
```bash
cd jtech-tasklist-backend
./gradlew bootRun
```

### Frontend (Vue.js)
```bash
cd jtech-tasklist-frontend
npm install
npm run dev
```

## 🧪 Testes

### Backend
```bash
cd jtech-tasklist-backend
./gradlew test
```

### Frontend
```bash
cd jtech-tasklist-frontend
npm run test:unit
```