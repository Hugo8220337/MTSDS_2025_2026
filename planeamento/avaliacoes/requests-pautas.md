# API de Pautas

Base URL: `/api/v1/pautas`

## Gestão de Pautas

### POST /api/v1/pautas
## /grades
Cria nova pauta
```json
{
  "unidade_curricular_id": 123,
  "ano_letivo_id": 2025,
  "tipo": "NORMAL",
  "epoca": "NORMAL"
}
```

### POST /api/v1/pautas/{id}/gerar
## /grades/{id}/generate
Gera resultados da pauta

### PUT /api/v1/pautas/{id}/publicar
## /grades/{id}/publish
Publica pauta (provisória ou definitiva)
```json
{
  "estado": "PROVISORIA"
}
```

### PUT /api/v1/pautas/{id}/fechar
## /grades/{id}/close
Fecha pauta definitivamente

### POST /api/v1/pautas/{id}/assinaturas
## /grades/{id}/sign
Adiciona assinatura à pauta
```json
{
  "tipo_assinatura": "REGENTE"
}
```

## Consultas

### GET /api/v1/pautas/{id}
## /grades/{id}
Obtém detalhes de uma pauta

### GET /api/v1/pautas/{id}/resultados
## /grades/{id}/results
Lista resultados de uma pauta

### GET /api/v1/pautas/{ucId}
## /grade/{udId}
Lista pautas de uma UC