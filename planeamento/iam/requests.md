# Exemplos de API Requests & Responses (Microserviço de Identidade)

Este ficheiro detalha os endpoints da API para gestão de utilizadores, autenticação e autorização.

---

## Processo 1: Autenticação e Gestão de Conta (Fluxo do Utilizador)

### 1.1. Registo e Login

#### Registo de um novo utilizador
Cria um novo registo na tabela `Utilizador` e atribui-lhe automaticamente o papel `CANDIDATO`.

```http
POST /api/v1/auth/register
Content-Type: application/json

{
  "nome_completo": "Ana Silva",
  "email": "ana.silva@email.com",
  "password": "uma_password_forte_123",
  "nif": "285948372"
}
```
**Resposta:** `201 Created`
```json
{
  "utilizador": {
    "id": 1,
    "nome_completo": "Ana Silva",
    "email": "ana.silva@email.com",
    "ativo": true
  },
  "message": "Registo efetuado com sucesso. Por favor, verifique o seu email."
}
```

#### Login de um utilizador existente
Verifica as credenciais e, se forem válidas, gera um token JWT contendo o ID do utilizador, papéis e permissões.

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "email": "ana.silva@email.com",
  "password": "uma_password_forte_123"
}
```
**Resposta:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "utilizador": {
    "id": 1,
    "nome_completo": "Ana Silva",
    "papeis": [
        { "nome": "CANDIDATO", "escola_id": null }
    ]
  }
}
```

### 1.2. Gestão da Conta Pessoal

#### Obter dados do perfil do utilizador autenticado
Devolve os dados do utilizador que está atualmente logado.

```http
GET /api/v1/account/me
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "id": 1,
  "nome_completo": "Ana Silva",
  "email": "ana.silva@email.com",
  "papeis": [
      { "nome": "CANDIDATO", "escola_id": null }
  ]
}
```

#### Atualizar dados do perfil do utilizador autenticado
Permite ao utilizador atualizar os seus próprios dados pessoais.

```http
PUT /api/v1/account/me
Authorization: Bearer <token>
Content-Type: application/json

{
  "nome_completo": "Ana Sofia Ribeiro",
  "telefone": "+351919876543"
}
```
**Resposta:** `200 OK`
```json
{
  "id": 1,
  "nome_completo": "Ana Sofia Ribeiro",
  "email": "ana.ribeiro@email.com",
  "telefone": "+351919876543",
  "data_ultima_atualizacao": "2025-10-24T14:00:00Z"
}
```

---

## Processo 2: Gestão de Utilizadores e Papéis (Fluxo do Admin)

### 2.1. Gestão de Utilizadores

#### Listar todos os utilizadores
Permite a um `ADMIN_SISTEMA` ou `ADMIN_ESCOLA` listar utilizadores. Um `ADMIN_ESCOLA` só verá os utilizadores associados à sua escola.

```http
GET /api/v1/admin/users?papel=CANDIDATO&limit=20
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "total": 150,
  "data": [
    {
      "id": 1,
      "nome_completo": "Ana Silva",
      "email": "ana.silva@email.com",
      "ativo": true
    },
    {
      "id": 2,
      "nome_completo": "Carlos Mendes",
      "email": "carlos.mendes@email.com",
      "ativo": false
    }
  ]
}
```

#### Criar um novo utilizador (Admin)
Permite a um administrador criar um novo utilizador (ex: um docente ou funcionário) e atribuir-lhe um papel inicial.

```http
POST /api/v1/admin/create-user
Authorization: Bearer <token>
Content-Type: application/json

{
  "nome_completo": "Prof. João Carlos",
  "email": "joao.carlos.docente@email.com",
  "papeis": [
    { "papel_id": 3, "escola_id": 10 } // papel_id 3 = DOCENTE, escola_id 10 = ESTG
  ]
}
```
**Resposta:** `201 Created`
```json
{
  "id": 151,
  "nome_completo": "João Carlos",
  "email": "joao.carlos.docente@email.com",
  "ativo": true,
  "message": "Utilizador criado. Um email foi enviado para definição da password."
}
```

### 2.2. Gestão de Papéis e Permissões

#### Listar todos os papéis disponíveis
Permite a um `ADMIN_SISTEMA` ver todos os papéis configurados no sistema.

```http
GET /api/v1/admin/roles
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
    { "id": 1, "nome": "ADMIN_SISTEMA" },
    { "id": 2, "nome": "ADMIN_ESCOLA" },
    { "id": 3, "nome": "DOCENTE" },
    { "id": 4, "nome": "CANDIDATO" }
]
```

#### Atribuir um papel a um utilizador
Associa um papel a um utilizador, opcionalmente no contexto de uma escola. Apenas para `ADMIN_SISTEMA` ou `ADMIN_ESCOLA`.

```http
POST /api/v1/admin/users/123/add-role
Authorization: Bearer <token>
Content-Type: application/json

{
  "papel_id": 2,
  "escola_id": 10  // ID da escola (ex: ESTG)
}
```
**Resposta:** `201 Created`
```json
{
    "utilizador_id": 123,
    "papel_id": 2,
    "escola_id": 10
}
```

#### Remover um papel de um utilizador

```http
DELETE /api/v1/admin/users/123/roles/{roleId}/delete?escola_id=10

Authorization: Bearer <token>
```
**Resposta:** `204 No Content`

#### Listar permissões de um papel
Mostra todas as permissões associadas a um determinado papel.

```http
GET /api/v1/admin/roles/2/permissions
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
    { "id": 10, "nome": "validar-documentos" },
    { "id": 11, "nome": "atribuir-notas-seriacao" }
]
```