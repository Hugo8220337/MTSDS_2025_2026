# API Candidaturas DGES

Base URL: `/api/v1/candiaturas/dges`

## Importação DGES

### POST /api/v1/candiaturas/dges/importacao
Importa arquivo CSV/XML com colocações DGES
```json
{
  "ano_letivo": "2025/2026",
  "fase": 1,
  "arquivo": "base64_do_ficheiro"
}
```

### GET /api/v1/candidaturas/dges/cursos/mapeamentos
Lista mapeamento de cursos DGES → internos

### POST /api/v1/candidaturas/dges/cursos/mapeamentos
Cria/atualiza mapeamento de curso
```json
{
  "codigo_dges": "9119",
  "curso_interno_id": "LEI",
  "ano_letivo": "2025/2026"
}
```

### GET /api/v1/candidaturas/dges/colocacoes
Lista colocações importadas
```json
{
  "ano_letivo": "2025/2026",
  "fase": 1
}
```

### GET /api/v1/candidaturas/dges/estatisticas
Obtém estatísticas de importação
```json
{
  "ano_letivo": "2025/2026",
  "fase": 1
}
```