# API de Classificações

Base URL: `/api/v1/classificacoes`

## Classificações

### POST /submit-classifications
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

## GET /classifications
Lista classificações com suporte a filtros
```
Parâmetros:
- momento_id (opcional)
- inscricao_id (opcional)
- aluno_id (opcional)
- estado (opcional)
```

## GET /classifications/{id}
Obtém uma classificação específica

### PUT /classifications/{id}/update-classification
Atualiza uma classificação
```json
{
  "valor": 16.0,
  "motivo": "Correção de erro de soma"
}
```

## Médias

## /students/{student_number}/curricular-units/{ucId}/averages
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

## POST /averages/recalculate
Recalcula as médias para uma inscrição específica após o lançamento de classificações. Este endpoint deve ser chamado automaticamente (via evento ou workflow) após operações como POST /classifications, para garantir consistência.

**Parâmetros de Query (obrigatórios):**
- `inscricao_id`: ID da inscrição do aluno na UC.
- `momento_avaliacao_id` (opcional): ID do momento de avaliação afetado, para recálculo seletivo.

**Exemplo de Request:**
```http
POST /api/v1/classificacoes/medias/recalcular?inscricao_id=1001&
momento_avaliacao_id=123

Authorization: Bearer <token>
Content-Type: application/json

{
  // Body vazio ou opcional para metadados adicionais, se necessário
}
```
**Resposta:** `200 OK`
```json
{
  "message": "Médias recalculadas com sucesso.",
  "inscricao_id": 1001,
  "componentes_atualizados": [
    {
      "componente_id": 10,
      "nova_media": 16.2,
      "ultima_atualizacao": "2025-11-05T12:00:00Z"
    }
  ],
  "nota_final_provisoria": 16.2
}
```

**Notas:**
- Este endpoint é idempotente: pode ser chamado múltiplas vezes sem efeitos colaterais.
- Em produção, integra com um sistema de eventos (ex.: Kafka) para acionamento automático após lançamentos de classificações.
- Validações: Verifica se a inscrição existe e se o utilizador tem permissões (ex.: docente da UC).
