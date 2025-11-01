# API Matrículas

## Processos de Matrícula

### POST /api/matriculas
## /api/enrollments
Cria novo processo de matrícula
```json
{
  "nif": "123456789",
  "nome": "João Silva",
  "curso_id": "LEI",
  "ano_letivo": "2025/2026",
  "origem": "DGES"
}
```

### GET /api/matriculas/{id}
## /api/enrollments/{id}
Obtém detalhes do processo de matrícula

### POST /api/matriculas/{id}/documentos
## /api/enrollments/{id}/documents
Upload de documento de matrícula
```json
{
  "tipo_documento": "CARTAO_CIDADAO",
  "documento": "base64_do_documento"
}
```

### GET /api/matriculas/{id}/documentos
## /api/enrollments/{id}/documents
Lista documentos do processo

### GET /api/matriculas/{id}/pagamentos
## /api/enrollments/{id}/payments
Lista pagamentos do processo

### POST /api/matriculas/{id}/pagamentos/gerar-referencia
## /api/enrollments/{id}/payments/
Gera referência para pagamento
```json
{
  "valor": 50.00,
  "descricao": "Taxa de Matrícula"
}
```

### PUT /api/matriculas/{id}/atualizar-estado
## PUT /api/enrollments/{id}/update-state
Atualiza estado da matrícula
```json
{
  "estado": "MATRICULADO",
  "observacoes": "Processo concluído com sucesso"
}
```