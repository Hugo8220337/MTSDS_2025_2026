# API Matrículas

## Processos de Matrícula

### POST /api/matriculas
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
Obtém detalhes do processo de matrícula

### POST /api/matriculas/{id}/documentos
Upload de documento de matrícula
```json
{
  "tipo_documento": "CARTAO_CIDADAO",
  "documento": "base64_do_documento"
}
```

### GET /api/matriculas/{id}/documentos
Lista documentos do processo

### GET /api/matriculas/{id}/pagamentos
Lista pagamentos do processo

### POST /api/matriculas/{id}/pagamentos/gerar-referencia
Gera referência para pagamento
```json
{
  "valor": 50.00,
  "descricao": "Taxa de Matrícula"
}
```

### PUT /api/matriculas/{id}/estado
Atualiza estado da matrícula
```json
{
  "estado": "MATRICULADO",
  "observacoes": "Processo concluído com sucesso"
}
```

## Eventos (Webhooks)

### POST /api/matriculas/eventos/dges
Recebe evento de colocação DGES
```json
{
  "tipo": "AlunoColocadoDGES",
  "dados": {
    "nif": "123456789",
    "nome": "João Silva",
    "curso_id": "LEI"
  }
}
```

### POST /api/matriculas/eventos/candidatura
Recebe evento de candidatura aprovada
```json
{
  "tipo": "CandidaturaAprovada",
  "dados": {
    "nif": "123456789",
    "nome": "João Silva",
    "curso_id": "MEI"
  }
}
```