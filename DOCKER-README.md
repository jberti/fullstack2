# JTech TaskList - Setup com Docker

Este projeto inclui um setup completo com Docker Compose que configura automaticamente:
- ✅ Banco de dados PostgreSQL com schema inicial
- ✅ Backend Spring Boot
- ✅ Frontend Vue.js
- ✅ Rede interna para comunicação entre serviços

## 🚀 Execução Rápida

### Windows
```bash
# Executar script automatizado
docker-start.bat

# OU manualmente
docker-compose up --build -d
```

### Linux/Mac
```bash
# Executar diretamente
docker-compose up --build -d
```

## 📋 Pré-requisitos

- Docker Desktop instalado
- Docker Compose (geralmente incluído no Docker Desktop)

## 🔧 Serviços Incluídos

| Serviço | Porta | URL | Descrição |
|---------|-------|-----|-----------|
| Frontend | 5173 | http://localhost:5173 | Interface Vue.js |
| Backend | 8080 | http://localhost:8080 | API Spring Boot |
| Database | 5432 | localhost:5432 | PostgreSQL |
| Swagger UI | 8080 | http://localhost:8080/doc/tasklist/v1/api.html | Documentação interativa da API |
| API Docs | 8080 | http://localhost:8080/doc/tasklist/v3/api-documents | Especificação OpenAPI JSON |

## 📊 Comandos Úteis

```bash
# Iniciar todos os serviços
docker-compose up -d

# Iniciar com rebuild das imagens
docker-compose up --build -d

# Ver logs de todos os serviços
docker-compose logs -f

# Ver logs de um serviço específico
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f database

# Parar todos os serviços
docker-compose down

# Parar e remover volumes (limpar banco)
docker-compose down --volumes

# Status dos serviços
docker-compose ps

# Executar comando no container
docker-compose exec backend bash
docker-compose exec database psql -U postgres -d sansys_database
```

## 🗄️ Banco de Dados

O banco PostgreSQL é automaticamente inicializado com:
- Database: `sansys_database`
- Usuário: `postgres`
- Senha: `postgres`
- Schema: Criado automaticamente via script SQL

### Conectar ao banco diretamente:
```bash
docker-compose exec database psql -U postgres -d sansys_database
```

## 🔧 Configurações

As configurações podem ser alteradas no arquivo `.env.docker` ou diretamente no `docker-compose.yml`.

### Variáveis principais:
- `POSTGRES_DB`: Nome do banco
- `POSTGRES_USER`: Usuário do banco
- `POSTGRES_PASSWORD`: Senha do banco
- `VITE_API_BASE_URL`: URL da API para o frontend

## 🐛 Troubleshooting

### Porta já em uso
```bash
# Verificar o que está usando a porta
netstat -ano | findstr :8080
netstat -ano | findstr :5173
netstat -ano | findstr :5432

# Parar serviços conflitantes ou alterar portas no docker-compose.yml
```

### Problemas de build
```bash
# Limpar cache do Docker
docker system prune -a

# Rebuild forçado
docker-compose build --no-cache
```

### Banco não inicializa
```bash
# Verificar logs do banco
docker-compose logs database

# Remover volume e recriar
docker-compose down --volumes
docker-compose up -d
```

### Frontend não conecta com backend
- Verificar se `VITE_API_BASE_URL` está correto
- Verificar se o backend está rodando: `docker-compose logs backend`
- Verificar rede: `docker network ls`

## 📁 Estrutura dos Arquivos Docker

```
├── docker-compose.yml          # Configuração principal
├── Dockerfile.backend          # Build do Spring Boot
├── Dockerfile.frontend         # Build do Vue.js
├── .env.docker                 # Variáveis de ambiente
├── docker-start.bat           # Script Windows
└── DOCKER-README.md           # Esta documentação
├── install-docker.bat          # Script de instalação do Docker
└── .dockerignore              # Arquivos ignorados no build
```

## 🔄 Desenvolvimento

Para desenvolvimento com hot-reload:

1. O frontend já está configurado com volume mount para hot-reload
2. Para o backend, você pode usar:
```bash
# Parar apenas o backend
docker-compose stop backend

# Executar backend localmente para desenvolvimento
cd jtech-tasklist-backend
./gradlew bootRun
```

## 🚀 Deploy em Produção

Para produção, considere:
1. Usar imagens otimizadas (multi-stage builds)
2. Configurar variáveis de ambiente seguras
3. Usar volumes persistentes para dados
4. Configurar reverse proxy (nginx)
5. Implementar health checks
6. Configurar logs centralizados
#
# 📖 Documentação da API (Swagger)

A aplicação inclui documentação automática da API via Swagger/OpenAPI:

### URLs da Documentação:
- **Swagger UI**: http://localhost:8080/doc/tasklist/v1/api.html
- **OpenAPI JSON**: http://localhost:8080/doc/tasklist/v3/api-documents
- **Actuator**: http://localhost:8080/actuator

### Funcionalidades do Swagger:
- ✅ Documentação interativa de todos os endpoints
- ✅ Teste direto dos endpoints via interface web
- ✅ Autenticação JWT integrada
- ✅ Schemas de request/response detalhados
- ✅ Exemplos de uso para cada endpoint

### Como usar:
1. Acesse http://localhost:8080/doc/tasklist/v1/api.html
2. Para endpoints protegidos, primeiro faça login em `/api/v1/auth/login`
3. Copie o token JWT retornado
4. Clique em "Authorize" no Swagger UI
5. Cole o token no formato: `Bearer seu_token_aqui`
6. Agora você pode testar todos os endpoints protegidos

### Endpoints disponíveis:
- **Auth**: `/api/v1/auth/*` - Login, registro, refresh token
- **Tasks**: `/api/v1/tasks/*` - CRUD de tarefas
- **TaskLists**: `/api/v1/tasklists/*` - Gerenciamento de listas

### Script de teste:
Execute `test-swagger.bat` para verificar se a documentação está funcionando corretamente.