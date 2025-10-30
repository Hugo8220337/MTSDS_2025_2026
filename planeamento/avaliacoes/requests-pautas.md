# API de Pautas

Base URL: `/api/v1/pautas`

## Gestão de Pautas

### POST /api/v1/pautas
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
Gera resultados da pauta

### PUT /api/v1/pautas/{id}/publicar
Publica pauta (provisória ou definitiva)
```json
{
  "estado": "PROVISORIA"
}
```

### PUT /api/v1/pautas/{id}/fechar
Fecha pauta definitivamente

### POST /api/v1/pautas/{id}/assinaturas
Adiciona assinatura à pauta
```json
{
  "tipo_assinatura": "REGENTE"
}
```

## Consultas

### GET /api/v1/pautas/{id}
Obtém detalhes de uma pauta

### GET /api/v1/pautas/{id}/resultados
Lista resultados de uma pauta

### GET /api/v1/unidades-curriculares/{id}/pautas
Lista pautas de uma UC