# API Candidaturas

Base URL: `/api/v1/candidaturas`
`/api/v1/applications`

## Concursos

### GET /api/v1/candidaturas/concursos
## /api/v1/applications/admissions
Lista concursos disponíveis

### POST /api/v1/candidaturas/concursos
## /api/v1/applications/admissions
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
## /api/v1/applications/admissions/{id}/phases
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

### POST /api/v1/candidaturas
## /api/v1/applications
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

### GET /api/v1/candidaturas/{id}
## /api/v1/applications/{id}
Obtém detalhes de uma candidatura

### POST /api/v1/candidaturas/{id}/documentos
## /api/v1/applications/{id}/documents

Upload de documento
```json
{
  "tipo_documento": "CERTIFICADO_HABILITACOES",
  "documento": "base64_do_documento"
}
```

### PUT /api/v1/candidaturas/{id}/atualizar-estado
## /api/v1/applications/{id}/update-state
Atualiza estado da candidatura
```json
{
  "estado": "APROVADA",
  "observacoes": "Candidatura completa e aprovada"
}
```

## Seriação

### POST /api/v1/candidaturas/concursos/{id}/fases/{fase_id}/seriar
## POST /api/v1/applications/admissions/{id}/phases/{phase_id}/filtering
Inicia processo de seriação da fase

### GET /api/v1/candidaturas/concursos/{id}/fases/{fase_id}/resultados
## GET /api/v1/applications/admissions/{id}/phases/{phase_id}/results
Obtém resultados da seriação