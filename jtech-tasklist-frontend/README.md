# jtech-tasklist-frontend

## Descrição

Frontend da aplicação TaskList desenvolvido com Vue 3, TypeScript, Vuetify e Pinia. Sistema completo de gerenciamento de tarefas com autenticação JWT.

## Stack Tecnológica

- **Vue 3** (Composition API) - Framework JavaScript progressivo
- **TypeScript** - Tipagem estática para JavaScript
- **Vue Router 4** - Roteamento oficial do Vue
- **Pinia** - Gerenciamento de estado moderno
- **Vuetify 3** - Framework Material Design para Vue
- **Axios** - Cliente HTTP para comunicação com API
- **Vitest** - Framework de testes unitários

## Pré-requisitos

- **Node.js 20.19.0+ ou 22.12.0+** (inclui npm automaticamente)
  - Baixe em: https://nodejs.org/
  - Escolha a versão LTS (Long Term Support)
  - Consulte `INSTALL.md` para instruções detalhadas de instalação

## Instalação

> **⚠️ IMPORTANTE:** Se você não tem Node.js instalado, consulte o arquivo `INSTALL.md` primeiro para instruções de instalação.

1. **Verifique se o Node.js está instalado:**
   ```bash
   node --version
   npm --version
   ```
   Se algum comando falhar, instale o Node.js (consulte `INSTALL.md`)

2. **Instale as dependências:**
   ```bash
   npm install
   ```

3. **Configure as variáveis de ambiente:**
   
   No Windows, crie um arquivo `.env` na pasta `jtech-tasklist-frontend` e adicione:
   ```
   VITE_API_BASE_URL=http://localhost:8080/api/v1
   ```
   
   Ou se preferir copiar o exemplo (Linux/Mac):
   ```bash
   cp .env.example .env
   ```

## Como Rodar

### Desenvolvimento

```bash
npm run dev
```

A aplicação estará disponível em `http://localhost:5173`

### Build para Produção

```bash
npm run build
```

### Preview da Build

```bash
npm run preview
```

## Estrutura do Projeto

```
src/
├── assets/          # Arquivos estáticos (CSS, imagens)
├── components/      # Componentes Vue reutilizáveis
│   ├── TaskList.vue
│   └── TaskDialog.vue
├── plugins/         # Plugins (Vuetify)
├── router/          # Configuração de rotas
├── services/        # Serviços de API
│   ├── api.ts
│   ├── authService.ts
│   └── taskService.ts
├── stores/          # Stores Pinia
│   ├── auth.ts
│   └── tasks.ts
├── views/           # Views/Páginas
│   ├── LoginView.vue
│   ├── RegisterView.vue
│   └── TasksView.vue
├── App.vue          # Componente raiz
└── main.ts          # Ponto de entrada
```

## Funcionalidades

### Autenticação
- Login de usuários
- Registro de novos usuários
- Persistência de sessão com JWT
- Guards de rota para proteção de páginas

### Gerenciamento de Tarefas
- Criar novas tarefas
- Editar tarefas existentes
- Excluir tarefas
- Marcar tarefas como concluídas/pendentes
- Filtrar tarefas (Todas, Pendentes, Concluídas)
- Validação de formulários

## Rotas

- `/login` - Página de login
- `/register` - Página de registro
- `/tasks` - Página principal de tarefas (requer autenticação)

## Testes

```bash
npm run test:unit
```

## Linting

```bash
npm run lint
```

## Formatação

```bash
npm run format
```

## Integração com Backend

O frontend se comunica com o backend através de requisições HTTP REST:

- **Autenticação**: `POST /api/v1/auth/login` e `POST /api/v1/auth/register`
- **Tarefas**: 
  - `GET /api/v1/tasks` - Listar tarefas
  - `POST /api/v1/tasks` - Criar tarefa
  - `PUT /api/v1/tasks/{id}` - Atualizar tarefa
  - `DELETE /api/v1/tasks/{id}` - Excluir tarefa

Todas as requisições (exceto autenticação) incluem o token JWT no header `Authorization: Bearer {token}`.

## Decisões Arquiteturais

1. **Composition API**: Uso da Composition API do Vue 3 para melhor organização e reutilização de lógica
2. **TypeScript**: Tipagem forte para maior segurança e melhor experiência de desenvolvimento
3. **Pinia**: Store moderna e simples para gerenciamento de estado
4. **Vuetify**: Framework Material Design completo com componentes prontos
5. **Separação de Responsabilidades**: Serviços para API, stores para estado, componentes para UI
6. **Persistência**: Uso de `pinia-plugin-persistedstate` para persistir estado no localStorage

