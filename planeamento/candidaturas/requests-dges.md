# API Candidaturas DGES

Base URL: `/api/v1/candiaturas-dges`
`/applications-dges`

## Importação DGES

### POST /api/v1/candiaturas-dges/importar
## /import
Importa arquivo CSV/XML com colocações DGES
```json
{
  "ano_letivo": "2025/2026",
  "fase": 1,
  "arquivo": "base64_do_ficheiro"
}
```

### GET /api/v1/candidaturas-dges/cursos/mapeamentos
## /api/v1/applications-dges/courses/mappings
Lista mapeamento de cursos DGES → internos

### POST /api/v1/candidaturas-dges/cursos/mapeamentos
## /api/v1/applications-dges/courses/mappings
Cria/atualiza mapeamento de curso
```json
{
  "codigo_dges": "9119",
  "curso_interno_id": "LEI",
  "ano_letivo": "2025/2026"
}
```

### GET /api/v1/candidaturas-dges/colocacoes
## /api/v1/applications-dges/placements
Lista colocações importadas
```json
{
  "ano_letivo": "2025/2026",
  "fase": 1
}
```

### GET /api/v1/candidaturas-dges/estatisticas
## /api/v1/applications-dges/statistics
Obtém estatísticas de importação
```json
{
  "ano_letivo": "2025/2026",
  "fase": 1
}
```