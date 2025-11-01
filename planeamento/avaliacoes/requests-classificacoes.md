# API de Classificações

Base URL: `/api/v1/classificacoes`

## Classificações

### POST /classificacoes
Lança novas classificações
As médias têm de ser atualizadas quando isto acontece (provavelmente tem de se  lançar um evento).
```json
{
  "momento_avaliacao_id": 123,
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

### GET /classificacoes
## /classifications
Lista classificações com suporte a filtros
```
Parâmetros:
- momento_id (opcional)
- inscricao_id (opcional)
- aluno_id (opcional)
- estado (opcional)
```

### GET /classificacoes/{id}
## /classifications/{id}
Obtém uma classificação específica

### PUT /classificacoes/{id}
## /classifications/{id}
Atualiza uma classificação
```json
{
  "valor": 16.0,
  "motivo": "Correção de erro de soma"
}
```

## Médias

### GET /alunos/{numeroAluno}/unidades-curriculares/{codigoUC}/medias
## /students/{student_number}/curricular_units/{ucId}/averages
Obtém todas as médias de um aluno numa UC
```json
{
  "inscricao_id": 1001,
  "unidade_curricular_id": 50,
  "componentes": [
    {
      "componente_id": 10,
      "titulo": "Avaliação Contínua",
      "peso": 0.4,
      "media": 15.5,
      "ultima_atualizacao": "2025-10-30T10:30:00Z"
    },
    {
      "componente_id": 11,
      "titulo": "Exame Final",
      "peso": 0.6,
      "media": null,
      "ultima_atualizacao": null
    }
  ],
  "nota_final_provisoria": null
}
```