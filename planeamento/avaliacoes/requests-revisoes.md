# API de Revisões

Base URL: `/api/v1/revisoes`

## Gestão de Revisões

### POST /api/v1/revisoes/pedidos
## /api/v1/revisions/requests
Submete pedido de revisão
```json
{
  "classificacao_id": 123,
  "justificacao": "Erro na soma das pontuações"
}
```

### POST /api/v1/revisoes/pedidos/{id}/documentos
## /api/v1/revisions/requests/{id}/documents

Anexa documento ao pedido
```json
{
  "tipo": "RESOLUCAO",
  "documento": "base64_do_documento"
}
```

### POST /api/v1/revisoes/pedidos/{id}/analises
## /api/v1/revisions/requests/{id}/analysis
Registra análise do pedido
```json
{
  "parecer": "Após reanálise, identificado erro na soma",
  "nota_recomendada": 16.5,
  "decisao": "ALTERAR_NOTA"
}
```

### PUT /api/v1/revisoes/pedidos/{id}/estado
## /api/v1/revisions/requests/{id}/state
Atualiza estado do pedido
```json
{
  "estado": "DEFERIDO",
  "motivo": "Erro confirmado na correção"
}
```

## Consultas

### GET /api/v1/revisoes/pedidos/{id}
## /api/v1/revisions/requests/{id}
Obtém detalhes de um pedido

### GET /api/v1/revisoes/pedidos/{id}/historico
## /api/v1/revisions/requests/{id}/history
Obtém histórico de estados

### GET /api/v1/revisoes/alunos/{aluno_id}/pedidos
## /api/v1/revisions/students/{student_id}/requests
Lista pedidos de um aluno

### GET /api/v1/revisoes/docentes/{docente_id}/pedidos
## /api/v1/revisions/teachers/{teacher_id}/requests
Lista pedidos para análise do docente