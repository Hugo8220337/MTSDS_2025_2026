# API de Pautas

Base URL: `/api/v1/pautas`
Base URL: `/api/v1/grades-report`

## Gestão de Pautas

## POST /create-grade-report
Cria nova pauta
```json
{
  "unidade_curricular_id": 123,
  "ano_letivo_id": 2025,
  "tipo": "NORMAL",
  "epoca": "NORMAL"
}
```

## /{id}/generate
Gera resultados da pauta

## PUT /{id}/publish
Publica pauta (provisória ou definitiva)
```json
{
  "estado": "PROVISORIA"
}
```

## /{id}/close
Fecha pauta definitivamente

### POST /{id}/sign
Adiciona assinatura à pauta
```json
{
  "tipo_assinatura": "REGENTE"
}
```

## Consultas

### /{id}
Obtém detalhes de uma pauta

### GET /{id}/results
Lista resultados de uma pauta

### GET curricular-unit/{udId}
Lista pautas de uma UC