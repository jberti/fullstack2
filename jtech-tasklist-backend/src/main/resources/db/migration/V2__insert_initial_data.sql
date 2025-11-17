-- Insert admin user (password: admin123)
INSERT INTO users (id, name, email, password, created_at, updated_at) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440000',
    'Administrador',
    'admin@j-tech.com.br',
    '$2a$10$qFx9vHtWBWKrm9zb7A7StudDicjoKEkgqd7BqpfEYqt3Hb4Yq2mAu',
    NOW(),
    NOW()
) ON CONFLICT (email) DO NOTHING;

-- Insert demo user (password: demo123)
INSERT INTO users (id, name, email, password, created_at, updated_at) 
VALUES (
    '550e8400-e29b-41d4-a716-446655440001',
    'Usuário Demo',
    'demo@j-tech.com.br',
    '$2a$10$CsKC3/4c3X.NIt/BxxVQROm4a3W/BuXkcJdACk2pOfiA/oRuYiIou',
    NOW(),
    NOW()
) ON CONFLICT (email) DO NOTHING;

-- Insert default tasklists for admin user
INSERT INTO tasklists (id, name, description, color, user_id, created_at, updated_at)
VALUES 
(
    '660e8400-e29b-41d4-a716-446655440000',
    'Tarefas Pessoais',
    'Lista padrão para organizar suas tarefas pessoais',
    '#1976D2',
    '550e8400-e29b-41d4-a716-446655440000',
    NOW(),
    NOW()
),
(
    '660e8400-e29b-41d4-a716-446655440001',
    'Trabalho',
    'Tarefas relacionadas ao trabalho e projetos profissionais',
    '#FF5722',
    '550e8400-e29b-41d4-a716-446655440000',
    NOW(),
    NOW()
),
(
    '660e8400-e29b-41d4-a716-446655440002',
    'Estudos',
    'Lista para organizar atividades de aprendizado e cursos',
    '#4CAF50',
    '550e8400-e29b-41d4-a716-446655440000',
    NOW(),
    NOW()
)
ON CONFLICT (name, user_id) DO NOTHING;

-- Insert default tasklist for demo user
INSERT INTO tasklists (id, name, description, color, user_id, created_at, updated_at)
VALUES (
    '660e8400-e29b-41d4-a716-446655440003',
    'Minha Lista Demo',
    'Lista de demonstração para o usuário demo',
    '#9C27B0',
    '550e8400-e29b-41d4-a716-446655440001',
    NOW(),
    NOW()
) ON CONFLICT (name, user_id) DO NOTHING;

-- Insert sample tasks for the default tasklist
INSERT INTO tasks (id, title, description, completed, user_id, tasklist_id, created_at, updated_at)
VALUES 
(
    '770e8400-e29b-41d4-a716-446655440000',
    'Configurar ambiente de desenvolvimento',
    'Instalar e configurar todas as ferramentas necessárias para desenvolvimento',
    true,
    '550e8400-e29b-41d4-a716-446655440000',
    '660e8400-e29b-41d4-a716-446655440000',
    NOW(),
    NOW()
),
(
    '880e8400-e29b-41d4-a716-446655440000',
    'Implementar sistema de listas',
    'Criar funcionalidade para gerenciar múltiplas listas de tarefas',
    false,
    '550e8400-e29b-41d4-a716-446655440000',
    '660e8400-e29b-41d4-a716-446655440000',
    NOW(),
    NOW()
),
(
    '990e8400-e29b-41d4-a716-446655440000',
    'Testar integração frontend-backend',
    'Verificar se todas as funcionalidades estão funcionando corretamente',
    false,
    '550e8400-e29b-41d4-a716-446655440000',
    '660e8400-e29b-41d4-a716-446655440000',
    NOW(),
    NOW()
)
ON CONFLICT DO NOTHING;