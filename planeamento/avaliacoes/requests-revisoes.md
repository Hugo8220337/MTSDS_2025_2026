# API de Revisões

Base URL: `/api/v1/revisoes`

## Gestão de Revisões

### POST /api/v1/revisoes/pedidos
Submete pedido de revisão
```json
{
  "classificacao_id": 123,
  "justificacao": "Erro na soma das pontuações"
}
```

### POST /api/v1/revisoes/pedidos/{id}/documentos
Anexa documento ao pedido
```json
{
  "tipo": "RESOLUCAO",
  "documento": "base64_do_documento"
}
```

### POST /api/v1/revisoes/pedidos/{id}/analises
Registra análise do pedido
```json
{
  "parecer": "Após reanálise, identificado erro na soma",
  "nota_recomendada": 16.5,
  "decisao": "ALTERAR_NOTA"
}
```

### PUT /api/v1/revisoes/pedidos/{id}/estado
Atualiza estado do pedido
```json
{
  "estado": "DEFERIDO",
  "motivo": "Erro confirmado na correção"
}
```

## Consultas

### GET /api/v1/revisoes/pedidos/{id}
Obtém detalhes de um pedido

### GET /api/v1/revisoes/pedidos/{id}/historico
Obtém histórico de estados

### GET /api/v1/revisoes/alunos/{aluno_id}/pedidos
Lista pedidos de um aluno

### GET /api/v1/revisoes/docentes/{docente_id}/pedidos
Lista pedidos para análise do docente