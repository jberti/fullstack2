# 🚀 Setup Rápido para Avaliação

## Para o Avaliador - Execução em 1 Comando

### Pré-requisitos
- Docker e Docker Compose instalados
- Portas 5432, 8080 e 5173 disponíveis

### Comando Único
```bash
docker-compose up --build
```

### ⏱️ Tempo Estimado
- **Build inicial**: 3-5 minutos
- **Inicialização**: 1-2 minutos

### 🌐 URLs para Teste
- **Frontend (Vue.js)**: http://localhost:5173
- **Backend API**: http://localhost:8080
- **Swagger/OpenAPI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health

### 📊 Status dos Containers
```bash
# Verificar se todos os containers estão rodando
docker-compose ps

# Ver logs em tempo real
docker-compose logs -f
```

### 🧪 Teste Rápido da API
```bash
# Health check do backend
curl http://localhost:8080/actuator/health

# Verificar se o banco está conectado
curl http://localhost:8080/actuator/health/db
```

### 🛑 Para Parar
```bash
docker-compose down
```

### 🔧 Em Caso de Problemas

#### Backend não responde (ERR_CONNECTION_REFUSED)
```bash
# 1. Verificar se o container está rodando
docker-compose ps

# 2. Ver logs do backend
docker-compose logs backend

# 3. Script de debug automático
./debug-backend.sh

# 4. Rebuild se necessário
docker-compose up --build backend
```

#### Outros problemas comuns
1. **Portas ocupadas**: Verifique se as portas 5432, 8080, 5173 estão livres
2. **Build falha**: Execute `docker-compose down -v` e tente novamente
3. **Containers não iniciam**: Aguarde 2-3 minutos para o PostgreSQL inicializar

### 📋 Checklist de Funcionamento
- [ ] Database container iniciou (tasklist-db)
- [ ] Backend container iniciou (tasklist-backend)  
- [ ] Frontend container iniciou (tasklist-frontend)
- [ ] Frontend acessível em http://localhost:5173
- [ ] API acessível em http://localhost:8080
- [ ] Swagger UI acessível em http://localhost:8080/swagger-ui.html

---

**Tempo total esperado**: 5-7 minutos do clone até a aplicação rodando completamente.