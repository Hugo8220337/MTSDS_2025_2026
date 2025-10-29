# API Candidaturas

Base URL: `/api/v1/candidaturas`

## Concursos

### GET /api/v1/candidaturas/concursos
Lista concursos disponíveis

### POST /api/v1/candidaturas/concursos
Cria novo concurso
```json
{
  "nome": "Mestrado em Engenharia Informática 2025/26",
  "tipo": "MESTRADO",
  "ano_letivo": "2025/2026",
  "data_inicio": "2025-06-01T00:00:00Z",
  "data_fim": "2025-07-30T23:59:59Z"
}
```

### POST /api/v1/candidaturas/concursos/{id}/fases
Cria nova fase do concurso
```json
{
  "numero": 1,
  "data_inicio": "2025-06-01T00:00:00Z",
  "data_fim": "2025-06-30T23:59:59Z",
  "vagas_totais": 30
}
```

## Candidaturas

### POST /api/v1/candidaturas/candidaturas
Submete nova candidatura
```json
{
  "fase_id": 123,
  "nif": "123456789",
  "nome": "João Silva",
  "opcoes": [
    {
      "curso_id": "MEI",
      "ordem_preferencia": 1
    }
  ]
}
```

### GET /api/v1/candidaturas/candidaturas/{id}
Obtém detalhes de uma candidatura

### POST /api/v1/candidaturas/candidaturas/{id}/documentos
Upload de documento
```json
{
  "tipo_documento": "CERTIFICADO_HABILITACOES",
  "documento": "base64_do_documento"
}
```

### PUT /api/v1/candidaturas/candidaturas/{id}/estado
Atualiza estado da candidatura
```json
{
  "estado": "APROVADA",
  "observacoes": "Candidatura completa e aprovada"
}
```

## Seriação

### POST /api/v1/candidaturas/concursos/{id}/fases/{fase_id}/seriar
Inicia processo de seriação da fase

### GET /api/v1/candidaturas/concursos/{id}/fases/{fase_id}/resultados
Obtém resultados da seriação