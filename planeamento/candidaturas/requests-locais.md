# API Candidaturas

Base URL: `/api/v1/candidaturas`
`/api/v1/applications`


# Concursos e Fases (Competicions BC)

## Criar Rascunho
```
POST /api/v1/applications/create-draft
```

## Atualizar Rascunho
```
PUT /api/v1/applications/{id}/update-draft
```

## Listar Concursos
```
GET /api/v1/candidaturas/competitions
```

## Criar Concursos
```
POST /api/v1/candidaturas/competitions
```

body:
```json
{
  "nome": "Mestrado em Engenharia Informática 2025/26",
  "tipo": "MESTRADO",
  "ano_letivo": "2025/2026",
  "data_inicio": "2025-06-01T00:00:00Z",
  "data_fim": "2025-07-30T23:59:59Z"
}
```

## Obter Concuso por ID
```
GET /api/v1/candidaturas/competitions/{id}
```

## Criar fase num concurso 
```
POST /api/v1/candidaturas/competitions/{id}/phases
```

body:
```json
{
  "numero": 1,
  "data_inicio": "2025-06-01T00:00:00Z",
  "data_fim": "2025-06-30T23:59:59Z",
  "vagas_totais": 30
}
```

## Listar fases
```
GET /api/v1/candidaturas/competitions/{id}/phases
```

# Candidaturas (Applications BC)

## Submeter Candidatura
```
POST /api/v1/applications/{applicationId}/submit
```

body:
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

## Obter Candudatura
```
GET /api/v1/candidaturas/applications/{id}
```

# Documentos

## Upload de documento
```
POST /api/v1/candidaturas/applications/{id}/documents
```

body:
```json
{
  "tipo_documento": "CERTIFICADO_HABILITACOES",
  "documento": "base64_do_documento"
}
```

## Listar docuemtnos de uma candidatura
```
GET /api/v1/candidaturas/applications/{id}/documents
```

# Estado da candidatura
## Atualizar Estado
```
PUT /api/v1/candidaturas/applications/{id}/state
```

body:
```json
{
  "estado": "APROVADA",
  "observacoes": "Candidatura completa e aprovada"
}
```

# Import DGES
```
POST /api/v1/candidaturas/competitions/{id}/phases/{fase}/dges-import
```

body:
```json
{
  "arquivo": "base64_do_ficheiro"
}
```