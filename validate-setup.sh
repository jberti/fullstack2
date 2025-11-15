#!/bin/bash

echo "🔍 Validando setup do projeto JTech TaskList..."

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para verificar se um comando existe
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Função para verificar se uma porta está livre
port_free() {
    ! nc -z localhost $1 2>/dev/null
}

echo "📋 Verificando pré-requisitos..."

# Verificar Docker
if command_exists docker; then
    echo -e "${GREEN}✅ Docker encontrado${NC}"
    docker --version
else
    echo -e "${RED}❌ Docker não encontrado. Instale o Docker primeiro.${NC}"
    exit 1
fi

# Verificar Docker Compose
if command_exists docker-compose; then
    echo -e "${GREEN}✅ Docker Compose encontrado${NC}"
    docker-compose --version
else
    echo -e "${RED}❌ Docker Compose não encontrado. Instale o Docker Compose primeiro.${NC}"
    exit 1
fi

# Verificar se Docker está rodando
if docker info >/dev/null 2>&1; then
    echo -e "${GREEN}✅ Docker daemon está rodando${NC}"
else
    echo -e "${RED}❌ Docker daemon não está rodando. Inicie o Docker primeiro.${NC}"
    exit 1
fi

# Verificar portas
echo "🔌 Verificando portas..."

if port_free 5432; then
    echo -e "${GREEN}✅ Porta 5432 (PostgreSQL) está livre${NC}"
else
    echo -e "${YELLOW}⚠️  Porta 5432 está ocupada. Pode causar conflitos.${NC}"
fi

if port_free 8080; then
    echo -e "${GREEN}✅ Porta 8080 (Backend) está livre${NC}"
else
    echo -e "${YELLOW}⚠️  Porta 8080 está ocupada. Pode causar conflitos.${NC}"
fi

if port_free 5173; then
    echo -e "${GREEN}✅ Porta 5173 (Frontend) está livre${NC}"
else
    echo -e "${YELLOW}⚠️  Porta 5173 está ocupada. Pode causar conflitos.${NC}"
fi

# Verificar arquivos essenciais
echo "📁 Verificando arquivos essenciais..."

files=(
    "docker-compose.yml"
    "Dockerfile.backend"
    "Dockerfile.frontend"
    "jtech-tasklist-backend/build.gradle"
    "jtech-tasklist-backend/src/main/resources/application.yml"
    "jtech-tasklist-backend/src/main/resources/db/migration/V1__create_tables.sql"
    "jtech-tasklist-frontend/package.json"
    "jtech-tasklist-frontend/vite.config.ts"
)

for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo -e "${GREEN}✅ $file${NC}"
    else
        echo -e "${RED}❌ $file não encontrado${NC}"
        exit 1
    fi
done

# Validar docker-compose.yml
echo "🔧 Validando docker-compose.yml..."
if docker-compose config >/dev/null 2>&1; then
    echo -e "${GREEN}✅ docker-compose.yml é válido${NC}"
else
    echo -e "${RED}❌ docker-compose.yml tem erros${NC}"
    docker-compose config
    exit 1
fi

echo ""
echo -e "${GREEN}🎉 Todos os pré-requisitos estão OK!${NC}"
echo ""
echo "🚀 Para iniciar a aplicação, execute:"
echo "   docker-compose up --build"
echo ""
echo "⏱️  Tempo estimado: 5-7 minutos"