# Exemplos de API Requests & Responses (Microserviço de Perfis)

Este ficheiro detalha os endpoints da API para gestão de perfis de utilizadores, incluindo dados pessoais, de contacto e documentos.

---

## Processo 1: Gestão de Perfis

### 1.1. Criar um novo Perfil (Consumidor de Evento)

Este endpoint é tipicamente interno e acionado por um evento (ex: `UtilizadorCriado`) vindo do serviço de IAM.

```http
POST /api/v1/internal/profiles
Content-Type: application/json

// Payload do evento recebido da message queue
{
  "utilizador_id: "1",
  "nome_completo": "Ana Silva",
  "email": "ana.silva@email.com" // Pode ser útil para notificações iniciais
}
```
**Resposta:** `201 Created`
```json
{
  "id": 1,
  "utilizador_id": "1",
  "nome_completo": "Ana Silva",
  "data_criacao": "2025-11-03T10:00:00Z"
}
```

### 1.2. Obter o Perfil do Utilizador Autenticado

Endpoint para o utilizador obter os seus próprios dados de perfil. O serviço extrai o id do utilizador a partir do token JWT.

```http
GET /api/v1/profiles/me
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "utilizador_id": "1",
  "nome_completo": "Ana Silva",
  "nome_social": null,
  "data_nascimento": null,
  "nif": null,
  "telefone": null,
  "documentos": [
    {
      "id": 1,
      "tipo": "Cartão Cidadão",
      "numero": "********123",
      "data_validade": "2030-10-20"
    }
  ]
}
```

### 1.3. Atualizar o Perfil do Utilizador Autenticado

Permite ao utilizador atualizar os seus dados pessoais.

```http
PATCH /api/v1/profiles/me
Authorization: Bearer <token>
Content-Type: application/json

{
  "nome_social": "Ana",
  "data_nascimento": "1998-05-15",
  "telefone": "+351912345678",
  "nif": "285948372"
}
```
**Resposta:** `200 OK`
```json
{
  "utilizador_id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
  "nome_completo": "Ana Silva",
  "nome_social": "Ana",
  "data_nascimento": "1998-05-15",
  "nif": "285948372",
  "telefone": "+351912345678",
  "data_ultima_atualizacao": "2025-11-03T11:30:00Z"
}
```

### 1.4. Obter Perfil de um Utilizador por id (Acesso restrito)

Endpoint para outros serviços (com permissão) obterem o perfil de um utilizador específico.

```http
GET /api/v1/profiles/{userId}
Authorization: Bearer <token_servico_ou_admin>
```
**Resposta:** `200 OK` (corpo da resposta similar ao `GET /profiles/me`)

---

## Processo 2: Gestão de Documentos de Identificação

### 2.1. Adicionar um Documento de Identificação

```http
POST /api/v1/profiles/me/documents
Authorization: Bearer <token>
Content-Type: application/json

{
  "tipo_documento_id": 1, // ID correspondente a "Cartão Cidadão"
  "numero": "30123456 7 ZZ9",
  "data_validade": "2030-10-20"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 1,
  "tipo_documento_id": 1,
  "numero": "********ZZ9", // Retorna o número mascarado por segurança
  "data_validade": "2030-10-20"
}
```

### 2.2. Listar Documentos de Identificação

```http
GET /api/v1/profiles/me/documents
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "id": 1,
    "tipo": "Cartão Cidadão",
    "numero": "********ZZ9",
    "data_validade": "2030-10-20"
  }
]
```

### 2.3. Remover um Documento de Identificação

```http
DELETE /api/v1/profiles/me/documents/1
Authorization: Bearer <token>
```
**Resposta:** `204 No Content`