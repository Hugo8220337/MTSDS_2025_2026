# Exemplos de API Requests & Responses (Microserviço de Notificações)

Este ficheiro detalha os endpoints da API para gestão de templates, envio e consulta de notificações.

---

## Processo 1: Envio de Notificações (API Interna)

Este é o principal endpoint a ser consumido por outros microserviços (Candidaturas, Avaliações, etc.) para solicitar o envio de uma notificação.

### 1.1. Solicitar o Envio de uma Notificação

Cria um registo na tabela `Notificacao` que será processado por um worker para o envio efetivo.

```http
POST /api/v1/notificacoes/enviar
Content-Type: application/json
// Este endpoint seria chamado internamente, possivelmente sem um token de utilizador,
// mas com um token de serviço-para-serviço.

{
  "utilizador_id": 1,
  "template_codigo": "NOTA_PUBLICADA",
  "dados": {
    "nome_aluno": "Ana Silva",
    "nome_uc": "Programação Orientada a Objetos",
    "nota_final": 12.1
  }
}
```
**Resposta:** `202 Accepted`
```json
{
  "message": "Notificação adicionada à fila para processamento.",
  "notificacao_id": 1
}
```

---

## Processo 2: Gestão de Templates (Fluxo do Admin)

Endpoints para administradores criarem e gerirem os modelos de mensagens.

### 2.1. Criar um novo Template

```http
POST /api/v1/notifications/templates
Authorization: Bearer <token>
Content-Type: application/json

{
  "codigo": "CANDIDATURA_SUBMETIDA",
  "tipo": "EMAIL",
  "assunto": "A sua candidatura foi submetida com sucesso",
  "corpo": "Olá {nome_candidato}, confirmamos a submissão da sua candidatura ao concurso {nome_concurso}."
}
```
**Resposta:** `201 Created`
```json
{
  "id": 1,
  "codigo": "CANDIDATURA_SUBMETIDA",
  "tipo": "EMAIL"
}
```

### 2.2. Listar todos os Templates

```http
GET /api/v1/notifications/templates
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "id": 1,
    "codigo": "CANDIDATURA_SUBMETIDA",
    "tipo": "EMAIL",
    "assunto": "A sua candidatura foi submetida com sucesso"
  },
  {
    "id": 2,
    "codigo": "NOTA_PUBLICADA",
    "tipo": "EMAIL",
    "assunto": "Nova classificação disponível"
  }
]
```

---

## Processo 3: Consulta e Gestão (Fluxo do Utilizador)

Endpoints para os utilizadores consultarem o seu histórico e gerirem as suas preferências.

### 3.1. Obter Histórico de Notificações do Utilizador

```http
GET /api/v1/notifications/me
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "id": 1,
    "assunto": "Nova classificação disponível",
    "estado": "ENVIADA",
    "data_envio": "2026-02-10T10:05:00Z"
  },
  {
    "id": 2,
    "assunto": "A sua candidatura foi submetida com sucesso",
    "estado": "ENVIADA",
    "data_envio": "2025-10-23T11:35:00Z"
  }
]
```

### 3.2. Gerir Preferências de Notificação

Permite ao utilizador ativar ou desativar canais de comunicação.

```http
PUT /api/v1/notifications/me/change-preferences
Authorization: Bearer <token>
Content-Type: application/json

{
  "email_ativo": true,
  "sms_ativo": false,
  "push_ativo": true
}
```
**Resposta:** `200 OK`
```json
{
  "utilizador_id": 1,
  "email_ativo": true,
  "sms_ativo": false,
  "push_ativo": true
}
```

---

## Processo 4: Monitorização (Fluxo do Admin)

Endpoints para administradores monitorizarem o estado do sistema de notificações.

### 4.1. Consultar o Log de Notificações

Permite ver todas as notificações do sistema, com filtros para depuração.

```http
GET /api/v1/notifications?estado=FALHOU&limit=50
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "id": 3,
    "utilizador_id": 15,
    "template_codigo": "RECUPERAR_PASSWORD",
    "estado": "FALHOU",
    "erro": "Erro de SMTP: Endereço de email inválido.",
    "data_criacao": "2025-11-01T18:00:00Z"
  }
]
```

### 4.2. Obter Detalhes de uma Notificação (com tentativas de envio)

```http
GET /api/v1/notificacoes/3
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "id": 3,
  "utilizador_id": 15,
  "template_codigo": "RECUPERAR_PASSWORD",
  "estado": "FALHOU",
  "tentativas": [
    {
      "numero_tentativa": 1,
      "resultado": "ERRO_SMTP",
      "erro_detalhes": "550 5.1.1 User unknown",
      "data_tentativa": "2025-11-01T18:00:05Z"
    },
    {
      "numero_tentativa": 2,
      "resultado": "ERRO_SMTP",
      "erro_detalhes": "550 5.1.1 User unknown",
      "data_tentativa": "2025-11-01T18:05:05Z"
    }
  ]
}
```