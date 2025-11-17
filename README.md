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

#### Modo Desenvolvimento
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

#### Modo Produção
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