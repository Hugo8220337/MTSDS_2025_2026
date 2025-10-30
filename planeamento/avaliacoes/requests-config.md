# API de Configuração de Avaliações

Base URL: `/api/v1/config-avaliacoes`

## Escalas de Avaliação

### POST /api/v1/config-avaliacoes/escalas
Cria uma nova escala de avaliação para uma UC
```json
{
  "unidade_curricular_id": 123,
  "ano_letivo_id": 2025,
  "descricao": "40% Frequência + 60% Exame"
}
```

### PUT /api/v1/config-avaliacoes/escalas/{id}/componentes
Adiciona componentes à escala
```json
{
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

### POST /api/v1/config-avaliacoes/componentes/{id}/momentos
Cria um momento de avaliação
```json
{
  "titulo": "1º Teste de Frequência",
  "data_realizacao": "2025-11-15T14:30:00Z",
  "duracao_minutos": 120,
  "sala": "Lab-1"
}
```

### PUT /api/v1/config-avaliacoes/escalas/{id}/submeter
Submete escala para aprovação

### GET /api/v1/config-avaliacoes/escalas/{id}
Obtém detalhes de uma escala

### GET /api/v1/config-avaliacoes/unidades-curriculares/{id}/escala
Obtém escala atual de uma UC