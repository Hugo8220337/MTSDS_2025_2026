# API de Classificações

Base URL: `/api/v1/classificacoes`

## Lançamento de Notas

### POST /momentos/{momento_id}/classificacoes
Lança notas para um momento de avaliação
```json
{
  "classificacoes": [
    {
      "inscricao_id": 1001,
      "valor": 15.5
    },
    {
      "inscricao_id": 1002,
      "valor": 17.0
    }
  ]
}
```

### PUT /classificacoes/{id}
Atualiza uma classificação
```json
{
  "valor": 16.0,
  "motivo": "Correção de erro de soma"
}
```

### GET /momentos/{momento_id}/classificacoes
Lista classificações de um momento

### GET /alunos/{aluno_id}/classificacoes
Lista classificações de um aluno

## Cálculo de Médias

### POST /componentes/{componente_id}/calcular-medias
Calcula médias de um componente

### GET /componentes/{componente_id}/medias
Lista médias calculadas de um componente