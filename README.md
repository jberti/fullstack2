# JTech TaskList

Sistema de gerenciamento de tarefas desenvolvido com Spring Boot (backend) e Vue.js (frontend).

## 🚀 Como Executar com Docker

### Pré-requisitos
- **Docker** >= 20.10
- **Docker Compose** >= 2.0
- **Git**


O script oferece duas opções:
- **1) Desenvolvimento**: Usa `docker-compose.yml` (recomendado para desenvolvimento)
- **2) Produção**: Usa `docker-compose.prod.yml` (otimizado para produção)


### Modo Desenvolvimento

```bash
# Clone o repositório
git clone https://github.com/jberti/fullstack2.git
cd jtech-tasklist

# Iniciar serviços
docker-compose up --build -d

# Verificar status
docker-compose ps

# Ver logs (opcional)
docker-compose logs -f
```

### Modo Produção

```bash
# Clone o repositório
git clone https://github.com/jberti/fullstack2.git
cd jtech-tasklist

# Iniciar em modo produção
docker-compose -f docker-compose.prod.yml up --build -d

# Verificar status
docker-compose -f docker-compose.prod.yml ps
```

### URLs de Acesso

| Ambiente | Frontend | Backend | Swagger | Database |
|----------|----------|---------|---------|----------|
| **Desenvolvimento** | http://localhost:5173 | http://localhost:8080 | http://localhost:8080/swagger-ui/index.html | localhost:5432 |
| **Produção** | http://localhost:80 | http://localhost:8080 | http://localhost:8080/swagger-ui/index.html | localhost:5432 |

### Credenciais de Teste

| Usuário | Email | Senha | Descrição |
|---------|-------|-------|-----------|
| **Admin** | admin@j-tech.com.br | admin123 | Usuário administrador com dados completos |
| **Demo** | demo@j-tech.com.br | demo123 | Usuário de demonstração |

### Comandos Úteis

```bash
# Parar todos os serviços
docker-compose down

# Ver logs em tempo real
docker-compose logs -f

# Ver logs de um serviço específico
docker-compose logs -f backend
docker-compose logs -f frontend

# Reconstruir e reiniciar
docker-compose up --build -d

# Limpar volumes (CUIDADO: apaga dados do banco)
docker-compose down -v
```

## 🛠️ Stack Tecnológica

### Backend
- **Java 21** - Versão LTS mais recente
- **Spring Boot 3.5.5** - Framework principal
- **Spring Security** - Autenticação e autorização
- **Spring Data JPA** - Persistência de dados
- **PostgreSQL 15** - Banco de dados
- **JWT** - Autenticação stateless

### Frontend
- **Vue.js 3** - Framework reativo
- **TypeScript** - Type safety
- **Vuetify 3** - Material Design components
- **Pinia** - State management
- **Axios** - Cliente HTTP

### DevOps
- **Docker** - Containerização
- **Docker Compose** - Orquestração
- **Nginx** - Proxy reverso (produção)

## 📋 Funcionalidades

- ✅ Autenticação JWT
- ✅ Gerenciamento de usuários
- ✅ Criação e edição de listas de tarefas
- ✅ Criação e edição de tarefas
- ✅ Interface responsiva moderna
- ✅ Temas claro/escuro
- ✅ Listas personalizáveis com cores
- ✅ Filtros e organização de tarefas

---

**Desenvolvido com ❤️ pela equipe J
# 2. Execute o script de inicialização
# Windows
./docker-start.bat

# Linux/macOS
chmod +x docker-start.sh
./docker-start.sh
```

### Opção 2: Execução Manual

```bash
# 1. Clone o repositório
git clone <repository-url>
cd jtech-tasklist

# 2. Construir e iniciar os serviços
docker-compose up --build -d

# 3. Verificar status dos serviços
docker-compose ps

# 4. Visualizar logs (opcional)
docker-compose logs -f
```

### Opção 3: Desenvolvimento Local (Sem Docker)

#### Backend
```bash
cd jtech-tasklist-backend

# Instalar dependências e executar
./gradlew bootRun

# Ou com perfil específico
./gradlew bootRun --args='--spring.profiles.active=dev'
```

#### Frontend
```bash
cd jtech-tasklist-frontend

# Instalar dependências
npm install

# Executar em modo desenvolvimento
npm run dev
```

### URLs de Acesso

| Serviço | URL | Descrição |
|---------|-----|-----------|
| **Frontend** | http://localhost:5173 | Interface principal da aplicação |
| **Backend API** | http://localhost:8080 | API REST |
| **Swagger UI** | http://localhost:8080/swagger-ui/index.html | Documentação interativa da API |
| **Actuator** | http://localhost:8080/actuator | Endpoints de monitoramento |
| **Database** | localhost:5432 | PostgreSQL (usuário: postgres, senha: postgres) |

### Credenciais de Teste

| Usuário | Email | Senha | Descrição |
|---------|-------|-------|-----------|
| **Admin** | admin@j-tech.com.br | admin123 | Usuário administrador com dados completos |
| **Demo** | demo@j-tech.com.br | demo123 | Usuário de demonstração |

---

## 🧪 Como Rodar os Testes

### Testes do Backend

```bash
cd jtech-tasklist-backend

# Executar todos os testes
./gradlew test

# Executar testes com relatório de cobertura
./gradlew test jacocoTestReport

# Executar apenas testes unitários
./gradlew test --tests "*Test"

# Executar apenas testes de integração
./gradlew test --tests "*IntegrationTest"

# Executar testes específicos
./gradlew test --tests "AuthServiceTest"

# Executar testes em modo contínuo
./gradlew test --continuous
```

### Testes do Frontend

```bash
cd jtech-tasklist-frontend

# Executar todos os testes
npm run test:unit

# Executar testes em modo watch
npm run test:unit -- --watch

# Executar testes com cobertura
npm run test:unit -- --coverage

# Executar testes específicos
npm run test:unit -- AuthStore.test.ts
```

### Relatórios de Teste

- **Backend**: `jtech-tasklist-backend/build/reports/tests/test/index.html`
- **Cobertura Backend**: `jtech-tasklist-backend/build/reports/jacoco/test/html/index.html`
- **Frontend**: `jtech-tasklist-frontend/coverage/index.html`

### Testes E2E (Futuro)

```bash
# Cypress (planejado)
npm run test:e2e

# Playwright (alternativa)
npm run test:playwright
```

---

## 📁 Estrutura de Pastas Detalhada

### Estrutura Geral do Projeto

```
jtech-tasklist/
├── 📁 jtech-tasklist-backend/          # Backend Spring Boot
├── 📁 jtech-tasklist-frontend/         # Frontend Vue.js
├── 📁 logs/                            # Logs da aplicação
├── 🐳 docker-compose.yml               # Orquestração de containers
├── 🐳 docker-compose.prod.yml          # Configuração de produção
├── 🐳 Dockerfile.backend               # Container do backend
├── 🐳 Dockerfile.frontend              # Container do frontend
├── 🚀 docker-start.sh                  # Script de inicialização Linux/macOS
├── 🚀 docker-start.bat                 # Script de inicialização Windows
└── 📖 README.md                        # Documentação principal
```

### Backend (Spring Boot)

```
jtech-tasklist-backend/
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/br/com/jtech/tasklist/
│   │   │   ├── 📁 config/              # Configurações
│   │   │   │   ├── SecurityConfig.java        # Configuração de segurança
│   │   │   │   └── JwtAuthenticationFilter.java # Filtro JWT
│   │   │   ├── 📁 controller/          # Controllers REST
│   │   │   │   ├── AuthController.java        # Autenticação
│   │   │   │   ├── TaskController.java        # Gerenciamento de tarefas
│   │   │   │   └── TasklistController.java    # Gerenciamento de listas
│   │   │   ├── 📁 dto/                 # Data Transfer Objects
│   │   │   │   ├── LoginRequest.java          # Request de login
│   │   │   │   ├── LoginResponse.java         # Response de login
│   │   │   │   ├── TaskRequest.java           # Request de tarefa
│   │   │   │   ├── TaskResponse.java          # Response de tarefa
│   │   │   │   ├── TasklistRequest.java       # Request de lista
│   │   │   │   └── TasklistResponse.java      # Response de lista
│   │   │   ├── 📁 entity/              # Entidades JPA
│   │   │   │   ├── User.java                  # Usuário
│   │   │   │   ├── Tasklist.java              # Lista de tarefas
│   │   │   │   └── Task.java                  # Tarefa
│   │   │   ├── 📁 exception/           # Tratamento de exceções
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   └── InvalidCredentialsException.java
│   │   │   ├── 📁 repository/          # Repositórios JPA
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── TasklistRepository.java
│   │   │   │   └── TaskRepository.java
│   │   │   ├── 📁 service/             # Serviços de negócio
│   │   │   │   ├── AuthService.java           # Autenticação
│   │   │   │   ├── JwtService.java            # Gerenciamento JWT
│   │   │   │   ├── TaskService.java           # Lógica de tarefas
│   │   │   │   └── TasklistService.java       # Lógica de listas
│   │   │   └── StartTasklist.java      # Classe principal
│   │   └── 📁 resources/
│   │       ├── 📁 db/migration/        # Scripts Flyway
│   │       │   ├── V1__create_tables.sql
│   │       │   └── V2__insert_initial_data.sql
│   │       └── application.yml         # Configurações da aplicação
│   └── 📁 test/                        # Testes
│       └── 📁 java/br/com/jtech/tasklist/
│           ├── 📁 controller/          # Testes de controller
│           ├── 📁 service/             # Testes de service
│           └── 📁 repository/          # Testes de repository
├── build.gradle                       # Configuração Gradle
└── gradle.properties                  # Propriedades do projeto
```

### Frontend (Vue.js)

```
jtech-tasklist-frontend/
├── 📁 src/
│   ├── 📁 assets/                      # Recursos estáticos
│   │   ├── base.css                   # Estilos base
│   │   └── main.css                   # Estilos principais
│   ├── 📁 components/                  # Componentes Vue
│   │   ├── LoadingSpinner.vue         # Spinner de carregamento
│   │   ├── TaskDialog.vue             # Modal de tarefa
│   │   ├── TaskList.vue               # Lista de tarefas
│   │   ├── TasklistDialog.vue         # Modal de lista
│   │   └── TasklistSidebar.vue        # Sidebar de listas
│   ├── 📁 composables/                 # Composables Vue
│   │   └── useNotifications.ts        # Notificações
│   ├── 📁 plugins/                     # Plugins
│   │   └── vuetify.ts                 # Configuração Vuetify
│   ├── 📁 router/                      # Roteamento
│   │   └── index.ts                   # Configuração de rotas
│   ├── 📁 services/                    # Serviços de API
│   │   ├── api.ts                     # Cliente HTTP base
│   │   ├── authService.ts             # Serviço de autenticação
│   │   ├── taskService.ts             # Serviço de tarefas
│   │   └── tasklistService.ts         # Serviço de listas
│   ├── 📁 stores/                      # Stores Pinia
│   │   ├── auth.ts                    # Store de autenticação
│   │   ├── tasks.ts                   # Store de tarefas
│   │   └── tasklists.ts               # Store de listas
│   ├── 📁 views/                       # Views/Páginas
│   │   ├── LoginView.vue              # Página de login
│   │   ├── RegisterView.vue           # Página de registro
│   │   └── TasksView.vue              # Página principal
│   ├── App.vue                        # Componente raiz
│   └── main.ts                        # Ponto de entrada
├── 📁 public/                          # Arquivos públicos
├── package.json                       # Dependências npm
├── vite.config.ts                     # Configuração Vite
├── tsconfig.json                      # Configuração TypeScript
└── vitest.config.ts                   # Configuração de testes
```

---

## 🎯 Decisões Técnicas Aprofundadas

### 1. Arquitetura e Padrões

#### Clean Architecture
**Decisão**: Implementação de Clean Architecture com separação clara de responsabilidades.

**Justificativa**:
- **Testabilidade**: Cada camada pode ser testada independentemente
- **Manutenibilidade**: Mudanças em uma camada não afetam outras
- **Flexibilidade**: Facilita troca de tecnologias (ex: banco de dados)
- **Escalabilidade**: Permite crescimento organizado do código

#### Repository Pattern
**Decisão**: Uso do Spring Data JPA com interfaces de repositório.

**Justificativa**:
- **Abstração**: Separa lógica de negócio da persistência
- **Testabilidade**: Facilita mocking em testes unitários
- **Produtividade**: Queries derivadas automáticas
- **Flexibilidade**: Permite implementações customizadas

#### DTO Pattern
**Decisão**: Uso de DTOs para transferência de dados entre camadas.

**Justificativa**:
- **Segurança**: Evita exposição de dados internos
- **Versionamento**: Facilita evolução da API
- **Validação**: Centraliza regras de validação
- **Performance**: Controla dados transferidos

### 2. Segurança

#### JWT (JSON Web Tokens)
**Decisão**: Autenticação stateless com JWT.

**Justificativa**:
- **Escalabilidade**: Não requer armazenamento de sessão no servidor
- **Performance**: Reduz consultas ao banco para validação
- **Flexibilidade**: Funciona bem em arquiteturas distribuídas
- **Padrão**: Amplamente adotado na indústria

#### BCrypt para Hashing
**Decisão**: Uso do BCrypt para hash de senhas.

**Justificativa**:
- **Segurança**: Algoritmo adaptativo resistente a ataques
- **Salt automático**: Previne ataques de rainbow table
- **Configurável**: Permite ajuste do fator de custo
- **Padrão**: Recomendado por especialistas em segurança

#### CORS Configuration
**Decisão**: Configuração permissiva para desenvolvimento, restritiva para produção.

**Justificativa**:
- **Desenvolvimento**: Facilita integração frontend/backend
- **Segurança**: Previne ataques cross-origin em produção
- **Flexibilidade**: Configuração por ambiente

### 3. Banco de Dados

#### PostgreSQL
**Decisão**: PostgreSQL como banco principal.

**Justificativa**:
- **Robustez**: ACID compliant, transações confiáveis
- **Performance**: Otimizações avançadas, índices eficientes
- **Recursos**: JSON support, full-text search, extensões
- **Escalabilidade**: Suporte a particionamento e replicação

#### Flyway Migrations
**Decisão**: Controle de versão do banco com Flyway.

**Justificativa**:
- **Versionamento**: Controle de mudanças no schema
- **Automação**: Deploy automatizado de mudanças
- **Rollback**: Possibilidade de reverter mudanças
- **Colaboração**: Sincronização entre desenvolvedores

#### JPA/Hibernate
**Decisão**: ORM com JPA/Hibernate.

**Justificativa**:
- **Produtividade**: Reduz código boilerplate
- **Portabilidade**: Abstração do banco de dados
- **Cache**: Cache de primeiro e segundo nível
- **Lazy Loading**: Otimização de consultas

### 4. Frontend

#### Vue.js 3 com Composition API
**Decisão**: Vue.js 3 com Composition API.

**Justificativa**:
- **Reatividade**: Sistema reativo otimizado
- **TypeScript**: Suporte nativo melhorado
- **Performance**: Tree-shaking e otimizações
- **Flexibilidade**: Composition API mais flexível

#### Pinia para State Management
**Decisão**: Pinia em vez de Vuex.

**Justificativa**:
- **TypeScript**: Suporte nativo completo
- **DevTools**: Integração superior
- **Simplicidade**: API mais intuitiva
- **Performance**: Otimizações automáticas

#### Vuetify para UI
**Decisão**: Vuetify como biblioteca de componentes.

**Justificativa**:
- **Material Design**: Design system consistente
- **Acessibilidade**: Componentes acessíveis por padrão
- **Responsividade**: Grid system flexível
- **Produtividade**: Componentes prontos para uso

### 5. Build e Deploy

#### Docker Multi-stage
**Decisão**: Dockerfiles multi-stage para otimização.

**Justificativa**:
- **Tamanho**: Imagens finais menores
- **Segurança**: Apenas artefatos necessários
- **Performance**: Builds mais rápidos com cache
- **Flexibilidade**: Diferentes targets (dev/prod)

#### Vite como Build Tool
**Decisão**: Vite em vez de Webpack.

**Justificativa**:
- **Performance**: HMR instantâneo
- **Simplicidade**: Configuração mínima
- **Otimizações**: Tree-shaking automático
- **Futuro**: Baseado em padrões web modernos

---

## 🚀 Melhorias e Roadmap

### Curto Prazo (1-3 meses)

#### 1. Testes e Qualidade
- [ ] **Cobertura de Testes**: Aumentar para 90%+
  - Testes unitários para todos os services
  - Testes de integração para controllers
  - Testes E2E com Cypress/Playwright
- [ ] **Análise Estática**: SonarQube integration
- [ ] **Performance Testing**: JMeter/Artillery
- [ ] **Security Testing**: OWASP ZAP integration

#### 2. Funcionalidades Core
- [ ] **Notificações Push**: WebSocket integration
- [ ] **Anexos em Tarefas**: Upload de arquivos
- [ ] **Comentários**: Sistema de comentários em tarefas
- [ ] **Tags/Labels**: Categorização avançada
- [ ] **Filtros Avançados**: Busca e filtros complexos

#### 3. UX/UI Melhorias
- [ ] **PWA**: Progressive Web App
- [ ] **Offline Support**: Cache e sincronização
- [ ] **Dark/Light Theme**: Tema persistente
- [ ] **Drag & Drop**: Reordenação de tarefas
- [ ] **Keyboard Shortcuts**: Atalhos de teclado

### Médio Prazo (3-6 meses)

#### 1. Escalabilidade
- [ ] **Microserviços**: Separação em serviços menores
  - Auth Service
  - Task Service  
  - Notification Service
- [ ] **Message Queue**: Redis/RabbitMQ para processamento assíncrono
- [ ] **Cache Distribuído**: Redis para cache de sessões
- [ ] **Load Balancer**: Nginx/HAProxy para distribuição

#### 2. Observabilidade
- [ ] **Logging Estruturado**: ELK Stack (Elasticsearch, Logstash, Kibana)
- [ ] **Métricas**: Prometheus + Grafana
- [ ] **Tracing**: Jaeger/Zipkin para distributed tracing
- [ ] **Health Checks**: Endpoints de saúde avançados

#### 3. Colaboração
- [ ] **Compartilhamento**: Listas compartilhadas entre usuários
- [ ] **Permissões**: Sistema de roles e permissões
- [ ] **Atividades**: Log de atividades e auditoria
- [ ] **Integração**: Slack/Teams/Discord webhooks

### Longo Prazo (6+ meses)

#### 1. Inteligência Artificial
- [ ] **Sugestões Inteligentes**: ML para sugerir tarefas
- [ ] **Análise de Produtividade**: Insights baseados em dados
- [ ] **Processamento de Linguagem Natural**: Criação de tarefas por voz/texto
- [ ] **Automação**: Regras automáticas baseadas em padrões

#### 2. Integrações
- [ ] **APIs Externas**: 
  - Google Calendar
  - Microsoft Outlook
  - Trello/Asana
  - GitHub Issues
- [ ] **Mobile Apps**: React Native/Flutter
- [ ] **Desktop Apps**: Electron/Tauri
- [ ] **Browser Extensions**: Chrome/Firefox

#### 3. Enterprise Features
- [ ] **Multi-tenancy**: Suporte a múltiplas organizações
- [ ] **SSO**: Single Sign-On (SAML/OAuth2)
- [ ] **Compliance**: GDPR/LGPD compliance
- [ ] **Backup/Recovery**: Estratégias de backup automático

### Melhorias Técnicas

#### 1. Performance
- [ ] **Database Optimization**:
  - Índices otimizados
  - Particionamento de tabelas
  - Read replicas
- [ ] **Frontend Optimization**:
  - Code splitting avançado
  - Service Workers
  - Image optimization
- [ ] **CDN**: CloudFlare/AWS CloudFront

#### 2. Segurança
- [ ] **OAuth2/OpenID Connect**: Integração com provedores externos
- [ ] **Rate Limiting**: Proteção contra abuse
- [ ] **Audit Logging**: Log completo de ações
- [ ] **Encryption**: Criptografia de dados sensíveis

#### 3. DevOps
- [ ] **CI/CD Pipeline**: GitHub Actions/GitLab CI
- [ ] **Infrastructure as Code**: Terraform/Pulumi
- [ ] **Kubernetes**: Orquestração em produção
- [ ] **Monitoring**: Alertas proativos

### Métricas de Sucesso

#### Técnicas
- **Performance**: < 200ms response time
- **Availability**: 99.9% uptime
- **Test Coverage**: > 90%
- **Security**: Zero vulnerabilidades críticas

#### Negócio
- **User Engagement**: > 80% monthly active users
- **Task Completion**: > 70% task completion rate
- **User Satisfaction**: > 4.5/5 rating
- **Growth**: 20% monthly user growth

---

## 📞 Suporte e Contato

### Documentação Adicional
- **API Documentation**: http://localhost:8080/swagger-ui/index.html
- **Frontend Storybook**: (Planejado)
- **Architecture Decision Records**: `/docs/adr/`

### Contribuição
1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

### Licença
Este projeto está licenciado sob a MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.

---