# API de Revisões

Base URL: `/api/v1/revisions`

## Pedidos de Revisão

### POST /make-requests
Submete pedido de revisão
```json
{
  "classificacao_id": 123,
  "justificacao": "Erro na soma das pontuações da questão 3"
}
```

**Response:**
```json
{
  "id": 456,
  "estado": "AGUARDA_APROVACAO",
  "prazo_resposta": "2025-11-12T23:59:59Z",
  "data_submissao": "2025-11-05T10:30:00Z"
}
```

### GET /requests/{id}
Obtém detalhes de um pedido
```json
{
  "id": 456,
  "classificacao_id": 123,
  "aluno_id": 789,
  "docente_responsavel_id": 101,
  "justificacao": "Erro na soma...",
  "estado": "EM_REVISAO",
  "aprovado": true,
  "justificacao_docente": "Pedido deferido para reanálise",
  "nota_original": 14.5,
  "nota_revista": 16.0,
  "data_submissao": "2025-11-05T10:30:00Z"
}
```

### GET /requests
Lista pedidos (com filtros)
```
Query params:
- aluno_id (optional)
- docente_responsavel_id (optional)
- estado (optional): AGUARDA_APROVACAO, REJEITADO, EM_REVISAO, CONCLUIDO, CANCELADO
- classificacao_id (optional)
```

### PATCH /requests/{id}/cancel
Cancela pedido (só o aluno, antes de aprovação)
```json
{
  "estado": "CANCELADO"
}
```

## Aprovação/Rejeição (Docente)

### PATCH /requests/{id}/approve
Aprova pedido para revisão
```json
{
  "justificacao_docente": "Pedido válido, vou reanalisar a correção"
}
```

**Response:**
```json
{
  "id": 456,
  "estado": "EM_REVISAO",
  "aprovado": true,
  "data_decisao_aprovacao": "2025-11-06T14:00:00Z"
}
```

### PATCH /requests/{id}/reject
Rejeita pedido
```json
{
  "justificacao_docente": "Correção está correta, sem motivos para revisão"
}
```

**Response:**
```json
{
  "id": 456,
  "estado": "REJEITADO",
  "aprovado": false,
  "data_decisao_aprovacao": "2025-11-06T14:00:00Z"
}
```

## Revisão da Prova (Docente)

### PUT /requests/{id}/finish-revisoin
Conclui revisão e define nova nota
```json
{
  "nota_revista": 16.0,
  "observacoes_revisao": "Erro identificado na questão 3: soma estava incorreta. Nota corrigida de 14.5 para 16.0"
}
```

**Response:**
```json
{
  "id": 456,
  "estado": "CONCLUIDO",
  "nota_original": 14.5,
  "nota_revista": 16.0,
  "data_revisao": "2025-11-08T16:30:00Z"
}
```

**Evento gerado:**
```json
{
  "event": "revisao.concluida",
  "data": {
    "pedido_revisao_id": 456,
    "classificacao_id": 123,
    "nota_antiga": 14.5,
    "nota_nova": 16.0,
    "aluno_id": 789
  }
}
```

## Documentos

### POST /requests/{id}/add.document
Anexa documento ao pedido
```json
{
  "tipo": "RESOLUCAO",
  "nome_ficheiro": "prova_questao3.pdf",
  "documento": "base64_encoded_file"
}
```

### GET /requests/{id}/documents
Lista documentos de um pedido

### DELETE /documents/{documentId}
Remove documento
