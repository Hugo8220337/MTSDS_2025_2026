# API de Configuração de Avaliações

Base URL: `/api/v1/planeamento-avaliacoes`

## Componentes de Avaliação

### POST /create-evaluation-component
Cria componentes de avaliação para uma UC
```json
{
  "unidade_curricular_id": 123,
  "ano_letivo_id": 2025,
  "componentes": [
    {
      "nome": "Frequência",
      "peso": 0.4,
      "nota_minima": 8.0
    },
    {
      "nome": "Exame",
      "peso": 0.6,
      "nota_minima": 8.0
    }
  ]
}
```

### GET /evaluation-components
Lista componentes por UC e ano letivo
```
Query params:
- unidade_curricular_id (required)
- ano_letivo_id (required)
```

### PUT /evaluation-component/{id}/edit
Atualiza um componente
```json
{
  "nome": "Frequência",
  "peso": 0.5,
  "nota_minima": 9.0
}
```

### DELETE /evaluation-component/{id}/delete
Remove um componente de avaliação

## Momentos de Avaliação

### POST /evaluation-component/{componentId}/add-evaluation-moment
Cria um momento de avaliação para um componente
```json
{
  "titulo": "1º Teste de Frequência",
  "data_realizacao": "2025-11-15T14:30:00Z",
  "duracao_minutos": 120,
  "sala": "Lab-1"
}
```

### GET /evaluation-component/{componenteId}/evaluation-moment
Lista momentos de um componente

### PUT /evaluation-moment/{id}
Atualiza um momento de avaliação
```json
{
  "titulo": "1º Teste de Frequência (Remarcado)",
  "data_realizacao": "2025-11-22T14:30:00Z",
  "duracao_minutos": 120,
  "sala": "Lab-2",
  "estado": "AGENDADO"
}
```

### PATCH /evaluation-moment/{id}/update-state
Atualiza estado do momento
```json
{
  "estado": "EM_CURSO" // ou "REALIZADO", "CANCELADO"
}
```

### DELETE /evaluation-moment/{id}/delete
Remove um momento de avaliação