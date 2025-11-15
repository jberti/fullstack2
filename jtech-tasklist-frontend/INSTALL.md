# Guia de Instalação - Frontend TaskList

## Pré-requisitos: Instalar Node.js

O Node.js é necessário para executar o projeto frontend. Ele inclui o npm (Node Package Manager).

### Windows

1. **Baixar Node.js:**
   - Acesse: https://nodejs.org/
   - Baixe a versão LTS (Long Term Support) recomendada
   - Versão mínima necessária: Node.js 20.19.0+ ou 22.12.0+

2. **Instalar:**
   - Execute o instalador baixado (.msi)
   - Siga o assistente de instalação (aceite os padrões)
   - Marque a opção "Automatically install the necessary tools" se oferecida

3. **Verificar instalação:**
   - Abra um novo terminal (PowerShell ou CMD)
   - Execute:
   ```bash
   node --version
   npm --version
   ```

### Alternativa: Via Chocolatey (se já tiver Chocolatey instalado)

```bash
choco install nodejs-lts
```

### Alternativa: Via Winget

```bash
winget install OpenJS.NodeJS.LTS
```

## Após instalar Node.js

1. **Navegue até a pasta do frontend:**
   ```bash
   cd jtech-tasklist-frontend
   ```

2. **Instale as dependências:**
   ```bash
   npm install
   ```

3. **Configure a variável de ambiente:**
   - Crie um arquivo `.env` na pasta `jtech-tasklist-frontend`
   - Adicione:
   ```
   VITE_API_BASE_URL=http://localhost:8080/api/v1
   ```

4. **Execute o projeto:**
   ```bash
   npm run dev
   ```

5. **Acesse no navegador:**
   - Abra: http://localhost:5173

## Troubleshooting

### Erro: "npm não é reconhecido"
- Certifique-se de ter reiniciado o terminal após instalar o Node.js
- Verifique se o Node.js está no PATH:
  ```bash
  where node
  where npm
  ```

### Erro: "permission denied"
- No Windows, execute o terminal como Administrador se necessário

### Versão do Node.js incorreta
- O projeto requer Node.js 20.19.0+ ou 22.12.0+
- Verifique a versão: `node --version`
- Se necessário, atualize: https://nodejs.org/

