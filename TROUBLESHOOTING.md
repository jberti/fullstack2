# 🔧 Guia de Solução de Problemas

## Problemas Comuns e Soluções

### 1. Erro de CORS no Frontend
**Problema**: Frontend não consegue acessar a API do backend
**Solução**: ✅ Já corrigido - Configuração de CORS adicionada no SecurityConfig.java

### 2. Backend não inicia (ERR_CONNECTION_REFUSED)
**Problema**: Container `tasklist-backend` não está rodando
**Diagnóstico**:
```bash
# Verificar status dos containers
docker-compose ps

# Ver logs do backend
docker-compose logs backend

# Script de debug automático
./debug-backend.sh
```

**Soluções**:
```bash
# Solução 1: Rebuild do backend
docker-compose up --build backend

# Solução 2: Limpar e reconstruir tudo
docker-compose down -v
docker-compose up --build

# Solução 3: Build sem cache
docker-compose build --no-cache backend
docker-compose up backend
```

### 3. Banco de dados não conecta
**Problema**: Backend não consegue conectar ao PostgreSQL
**Verificações**:
- Container do banco está rodando: `docker-compose ps`
- Logs do banco: `docker-compose logs database`
- Configurações no `.env` estão corretas

### 4. Frontend não carrega
**Problema**: Página em branco ou erro 404
**Soluções**:
- Verificar se o Vite está configurado para `host: '0.0.0.0'` ✅ Já corrigido
- Verificar logs: `docker-compose logs frontend`
- Acessar diretamente: http://localhost:5173

### 5. JWT Token inválido
**Problema**: Erro de autenticação
**Verificações**:
- JWT_SECRET está configurado corretamente
- Token não expirou
- Headers de autorização estão sendo enviados

### 6. Problemas de Build do Backend
**Problema**: Gradle build falha
**Soluções**:
```bash
# Limpar build
cd jtech-tasklist-backend
./gradlew clean build

# Ou via Docker
docker-compose build --no-cache backend
```

### 7. Dependências duplicadas
**Problema**: Conflitos de dependências no Gradle
**Solução**: ✅ Já corrigido - Dependências organizadas e duplicatas removidas

## Comandos Úteis

### Docker
```bash
# Ver logs de todos os serviços
docker-compose logs -f

# Ver logs de um serviço específico
docker-compose logs -f backend

# Reiniciar um serviço
docker-compose restart backend

# Entrar no container
docker-compose exec backend bash
docker-compose exec database psql -U postgres -d sansys_database
```

### Desenvolvimento Local
```bash
# Backend
cd jtech-tasklist-backend
./gradlew bootRun

# Frontend
cd jtech-tasklist-frontend
npm run dev
```

### Verificação de Saúde
```bash
# Backend health check
curl http://localhost:8080/actuator/health

# Database connection
docker-compose exec database pg_isready -U postgres
```

## Portas Utilizadas
- **5432**: PostgreSQL
- **8080**: Backend (Spring Boot)
- **5173**: Frontend (Vite)

## Variáveis de Ambiente Importantes
- `VITE_API_BASE_URL`: URL da API para o frontend
- `JWT_SECRET`: Chave secreta para JWT
- `DS_URL`, `DS_PORT`, `DS_DATABASE`: Configurações do banco