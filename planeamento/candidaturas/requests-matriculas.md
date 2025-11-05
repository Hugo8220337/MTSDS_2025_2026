# API Matrículas

## Processos de Matrícula

## POST /api/enrollments/create
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

## GET/api/enrollments/{id}
Obtém detalhes do processo de matrícula

### POST /api/enrollments/{id}/upload-document
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


### PUT /api/matriculas/{id}/atualizar-estado
## PUT /api/enrollments/{id}/update-state
Atualiza estado da matrícula
```json
{
  "estado": "MATRICULADO",
  "observacoes": "Processo concluído com sucesso"
}
```