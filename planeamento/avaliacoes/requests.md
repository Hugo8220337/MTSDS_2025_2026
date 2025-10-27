# Exemplos de API Requests & Responses (Microserviço de Avaliações)

Este ficheiro detalha os endpoints da API para gestão de avaliações, classificações, pautas e revisões, seguindo a abordagem DDD.

---

## Processo 1: Configuração da Avaliação (Fluxo do Docente/Regente)

O docente responsável pela Unidade Curricular (UC) define como os alunos serão avaliados num determinado ano letivo.

### 1.1. Definir a Escala de Classificações

Define a fórmula de cálculo da nota final (ex: 40% Frequência + 60% Exame).

```http
POST /api/v1/docentes/me/unidades-curriculares/POO/escala-classificacao
Authorization: Bearer <token>
Content-Type: application/json

{
  "ano_letivo": "2025/2026",
  "descricao": "0.4*Frequência + 0.6*Exame",
  "componentes": [
    { "nome": "Frequência", "peso": 0.40 },
    { "nome": "Exame", "peso": 0.60 }
  ]
}
```
**Resposta:** `201 Created`
```json
{
  "id": 1,
  "unidade_curricular_codigo": "POO",
  "ano_letivo": "2025/2026",
  "componentes": [
    { "id": 1, "nome": "Frequência", "peso": 0.40 },
    { "id": 2, "nome": "Exame", "peso": 0.60 }
  ]
}
```

### 1.2. Criar os Momentos de Avaliação

Cria os instrumentos de avaliação específicos (testes, trabalhos) para cada componente.

```http
POST /api/v1/docentes/me/componentes-avaliacao/1/avaliacoes
Authorization: Bearer <token>
Content-Type: application/json

{
  "titulo": "1º Teste de Frequência",
  "data_realizacao": "2025-11-15"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 10,
  "componente_id": 1,
  "titulo": "1º Teste de Frequência",
  "data_realizacao": "2025-11-15"
}
```

---

## Processo 2: Lançamento de Classificações (Fluxo do Docente)

O docente insere as notas dos alunos para uma avaliação específica.

### 2.1. Obter a lista de alunos para lançar notas

Para uma dada avaliação, o sistema apresenta a lista de alunos inscritos na UC.

```http
GET /api/v1/docentes/me/avaliacoes/10/classificacoes
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "avaliacao": {
    "id": 10,
    "titulo": "1º Teste de Frequência"
  },
  "classificacoes": [
    { "inscricao_id": 1501, "numero_aluno": "202500123", "nome_aluno": "Ana Silva", "valor": null },
    { "inscricao_id": 1503, "numero_aluno": "202500124", "nome_aluno": "Carlos Mendes", "valor": null }
  ]
}
```

### 2.2. Submeter as classificações

O docente preenche e submete as notas. O pedido pode ser para uma ou várias notas em simultâneo.

```http
POST /api/v1/docentes/me/avaliacoes/10/classificacoes
Authorization: Bearer <token>
Content-Type: application/json

{
  "classificacoes": [
    { "inscricao_id": 1501, "valor": 14.5 },
    { "inscricao_id": 1503, "valor": 9.0 }
  ]
}
```
**Resposta:** `201 Created`
```json
{
  "message": "2 classificações foram lançadas com sucesso.",
  "resultados": [
    { "inscricao_id": 1501, "classificacao_id": 101, "valor": 14.5 },
    { "inscricao_id": 1503, "classificacao_id": 102, "valor": 9.0 }
  ]
}
```

---

## Processo 3: Gestão de Pautas (Fluxo do Docente/Regente)

### 3.1. Publicar uma Pauta

O docente promove a pauta a um estado visível para os alunos (primeiro provisória, depois definitiva).

```http
POST /api/v1/docentes/me/unidades-curriculares/POO/pauta/publicar
Authorization: Bearer <token>
Content-Type: application/json

{
  "ano_letivo": "2025/2026",
  "estado": "PROVISORIA"
}
```
**Resposta:** `200 OK`
```json
{
  "pauta_id": 5,
  "unidade_curricular_codigo": "POO",
  "ano_letivo": "2025/2026",
  "estado": "PROVISORIA",
  "data_publicacao": "2026-02-10T10:00:00Z"
}
```

---

## Processo 4: Consulta e Revisão (Fluxo do Aluno)

### 4.1. Consultar Classificações

O aluno consulta as suas notas para uma determinada UC.

```http
GET /api/v1/alunos/me/unidades-curriculares/POO/classificacoes?ano_letivo=2025/2026
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "unidade_curricular": "Programação Orientada a Objetos",
  "classificacao_final_calculada": 12.1,
  "componentes": [
    {
      "nome": "Frequência",
      "classificacao_componente": 14.5,
      "avaliacoes": [
        { "id": 101, "titulo": "1º Teste de Frequência", "valor": 14.5 }
      ]
    },
    {
      "nome": "Exame",
      "classificacao_componente": 10.5,
      "avaliacoes": [
        { "id": 105, "titulo": "Exame Época Normal", "valor": 10.5 }
      ]
    }
  ]
}
```

### 4.2. Pedir Revisão de uma Classificação

O aluno submete um pedido formal para que uma nota específica seja revista.

```http
POST /api/v1/alunos/me/revisoes
Authorization: Bearer <token>
Content-Type: application/json

{
  "classificacao_id": 101,
  "justificacao_aluno": "A cotação da pergunta 3 parece não ter sido corretamente aplicada."
}
```
**Resposta:** `201 Created`
```json
{
  "id": 1,
  "classificacao_id": 101,
  "estado": "SUBMETIDO",
  "data_pedido": "2026-02-11T15:00:00Z"
}
```