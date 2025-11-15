#!/bin/bash

echo "🚀 Iniciando JTech TaskList..."

# Verificar se Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker não está rodando. Por favor, inicie o Docker primeiro."
    exit 1
fi

# Verificar se Docker Compose está disponível
if ! command -v docker-compose > /dev/null 2>&1; then
    echo "❌ Docker Compose não encontrado. Por favor, instale o Docker Compose."
    exit 1
fi

# Parar containers existentes
echo "🛑 Parando containers existentes..."
docker-compose down

# Remover volumes órfãos (opcional)
echo "🧹 Limpando volumes órfãos..."
docker volume prune -f

# Construir e iniciar os serviços
echo "🔨 Construindo e iniciando os serviços..."
docker-compose up --build -d

echo "⏳ Aguardando serviços inicializarem..."
sleep 10

echo "⏳ Verificando se os serviços estão respondendo..."
# Aguardar backend responder
for i in {1..18}; do
    if curl -f http://localhost:8080/actuator/health >/dev/null 2>&1; then
        echo "✅ Backend está respondendo!"
        break
    fi
    echo "⏳ Aguardando backend... (tentativa $i/18)"
    sleep 10
done

# Verificar status dos containers
echo "📊 Status dos containers:"
docker-compose ps

echo ""
echo "✅ Aplicação iniciada com sucesso!"
echo ""
echo "🌐 URLs disponíveis:"
echo "   Frontend: http://localhost:5173"
echo "   Backend API: http://localhost:8080"
echo "   Swagger UI: http://localhost:8080/swagger-ui.html"
echo "   Database: PostgreSQL na porta 5432"
echo ""
echo "📝 Para ver os logs:"
echo "   docker-compose logs -f"
echo ""
echo "🛑 Para parar a aplicação:"
echo "   docker-compose down"