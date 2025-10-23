# Exemplos de API Requests & Responses

Este ficheiro detalha os endpoints da API, com exemplos de pedidos e respostas JSON, com base nos processos definidos.

---

## Processo 1: Submissão da Candidatura (Fluxo do Candidato)

### 1.1. Autenticação (Registo e Login)

#### Registo de um novo utilizador
Este pedido cria um novo registo na tabela `Utilizador`.

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
    "nif": "285948372",
    "ativo": true,
    "data_criacao": "2025-10-23T10:00:00Z"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Login de um utilizador existente
Este pedido verifica as credenciais na tabela `Utilizador` e, se forem válidas, gera um token de autenticação.

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
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 1.2. Ver Concursos e Iniciar Candidatura

#### Obter lista de concursos com fases abertas
Este pedido consulta as tabelas `Concurso` e `Fase` para devolver os concursos que têm fases de candidatura atualmente a decorrer.

```http
GET /api/v1/concursos?abertos=true
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "id": 1,
    "ano_letivo": "2025/2026",
    "tipo": "MESTRADO",
    "name": "Mestrado em Engenharia Informática",
    "fases_abertas": [
      {
        "id": 1,
        "nome": "1ª Fase",
        "data_inicio_candidatura": "2025-09-01",
        "data_fim_candidatura": "2025-10-30"
      }
    ]
  }
]
```

#### Iniciar uma nova candidatura (cria um rascunho)
Este pedido cria um novo registo na tabela `Candidatura` com o estado `RASCUNHO`, associando o utilizador autenticado à fase escolhida.

```http
POST /api/v1/candidaturas
Authorization: Bearer <token>
Content-Type: application/json

{
  "fase_id": 1
}
```
**Resposta:** `201 Created`
```json
{
  "id": 101,
  "utilizador_id": 1,
  "fase_id": 1,
  "origem": "LOCAL",
  "estado": "RASCUNHO",
  "data_submissao": null,
  "nota_final_serica": null
}
```

### 1.3. Preenchimento da Candidatura

#### Obter documentos requeridos para o concurso
Este pedido consulta a tabela `DocumentoRequerido` para listar os documentos necessários para um determinado concurso.

```http
GET /api/v1/concursos/1/documentos_requeridos
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "id": 1,
    "nome": "Certificado de Habilitações",
    "obrigatorio": true
  },
  {
    "id": 2,
    "nome": "Curriculum Vitae",
    "obrigatorio": true
  },
  {
    "id": 3,
    "nome": "Carta de Motivação",
    "obrigatorio": false
  }
]
```

#### Fazer upload de um documento
Este pedido cria um registo na tabela `DocumentoSubmetido`, associando o ficheiro enviado a uma candidatura e a um tipo de documento requerido.

```http
POST /api/v1/candidaturas/101/documentos
Authorization: Bearer <token>
Content-Type: multipart/form-data; boundary=boundary

--boundary
Content-Disposition: form-data; name="documento_requerido_id"

1
--boundary
Content-Disposition: form-data; name="file"; filename="certificado.pdf"
Content-Type: application/pdf

<...conteúdo binário do ficheiro...>
--boundary--
```
**Resposta:** `201 Created`
```json
{
  "id": 201,
  "candidatura_id": 101,
  "documento_requerido_id": 1,
  "url_ficheiro": "https://storage.example.com/path/to/certificado.pdf",
  "estado_validacao": "PENDENTE",
  "observacoes_validacao": null
}
```

#### Adicionar opções de curso à candidatura
Este pedido cria um ou mais registos na tabela `OpcaoCurso`, definindo as escolhas de curso do candidato e a sua ordem de preferência.

```http
POST /api/v1/candidaturas/101/opcoes
Authorization: Bearer <token>
Content-Type: application/json

[
  { "curso_id": 15, "ordem": 1 },
  { "curso_id": 18, "ordem": 2 }
]
```
**Resposta:** `201 Created`
```json
[
  {
    "id": 301,
    "candidatura_id": 101,
    "curso_id": 15,
    "ordem_preferencia": 1,
    "estado_opcao": null
  },
  {
    "id": 302,
    "candidatura_id": 101,
    "curso_id": 18,
    "ordem_preferencia": 2,
    "estado_opcao": null
  }
]
```

### 1.4. Submissão Final

#### Submeter a candidatura para análise
Este pedido atualiza o registo na tabela `Candidatura`, alterando o seu estado de `RASCUNHO` para `SUBMETIDA`.

```http
POST /api/v1/candidaturas/101/submit
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "id": 101,
  "utilizador_id": 1,
  "fase_id": 1,
  "origem": "LOCAL",
  "estado": "SUBMETIDA",
  "data_submissao": "2025-10-23T11:30:00Z",
  "nota_final_serica": null
}
```

---

## Processo 2: Validação (Fluxo do Admin)

### 2.1. Análise Documental

#### Obter candidaturas que precisam de validação
Este pedido consulta a tabela `Candidatura` para encontrar todas as candidaturas com o estado `SUBMETIDA`, para que um administrador as possa rever.

```http
GET /api/v1/admin/candidaturas?estado=SUBMETIDA
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "id": 101,
    "estado": "SUBMETIDA",
    "data_submissao": "2025-10-23T11:30:00Z",
    "utilizador": {
        "id": 1,
        "nome_completo": "Ana Silva"
    }
  }
]
```

#### Validar ou Rejeitar um documento submetido
Este pedido atualiza o `estado_validacao` de um registo específico na tabela `DocumentoSubmetido`.

```http
PUT /api/v1/admin/documentos_submetidos/201
Authorization: Bearer <token>
Content-Type: application/json

{
  "estado_validacao": "VALIDADO",
  "observacoes_validacao": "Documento conforme."
}
```
**Resposta:** `200 OK`
```json
{
  "id": 201,
  "estado_validacao": "VALIDADO",
  "observacoes_validacao": "Documento conforme."
}
```

### 2.2. Finalização da Análise

#### Mudar o estado da candidatura (após análise de todos os docs)
Este pedido atualiza o `estado` de um registo na tabela `Candidatura` para `VALIDADA` ou `INVALIDADA`, após a análise de todos os documentos.

```http
PUT /api/v1/admin/candidaturas/101
Authorization: Bearer <token>
Content-Type: application/json

{
  "estado": "VALIDADA"
}
```
**Resposta:** `200 OK`
```json
{
  "id": 101,
  "estado": "VALIDADA",
  "data_submissao": "2025-10-23T11:30:00Z"
}
```

---

## Processo 3: Seriação e Colocação (Fluxo do Admin/Diretor de Curso)

### 3.1. Atribuição de Notas

#### Obter candidaturas validadas para um curso
Este pedido consulta as tabelas `Candidatura`, `OpcaoCurso` e `DocumentoSubmetido` para apresentar ao avaliador as candidaturas validadas de um curso específico.

```http
GET /api/v1/admin/cursos/15/candidaturas?estado=VALIDADA
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "id": 101,
    "utilizador": { "id": 1, "nome_completo": "Ana Silva" },
    "documentos": [
      { "nome": "Certificado de Habilitações", "url": "..." },
      { "nome": "Curriculum Vitae", "url": "..." }
    ]
  }
]
```

#### Inserir as notas dos critérios de seriação
Este pedido cria um ou mais registos na tabela `NotaComponente`, associando as notas de avaliação de um diretor de curso a uma candidatura.

```http
POST /api/v1/admin/candidaturas/101/notas
Authorization: Bearer <token>
Content-Type: application/json

[
  { "criterio_id": 5, "valor_nota": 18.50 },
  { "criterio_id": 6, "valor_nota": 17.00 }
]
```
**Resposta:** `201 Created`
```json
[
  {
    "id": 501,
    "candidatura_id": 101,
    "criterio_seriacao_id": 5,
    "valor_nota": 18.50
  },
  {
    "id": 502,
    "candidatura_id": 101,
    "criterio_seriacao_id": 6,
    "valor_nota": 17.00
  }
]
```

### 3.2. Colocação e Publicação

*(Estes passos são tipicamente acionados por botões na UI de admin que disparam background jobs. As respostas podem ser simples confirmações.)*

#### Disparar o algoritmo de colocação para uma fase
Este pedido inicia um processo de fundo que lê as tabelas `Candidatura`, `NotaComponente` e `CriterioSeriacao` para calcular a `nota_final_sericao` e atualizar o estado em `Candidatura` e `OpcaoCurso`.

```http
POST /api/v1/admin/fases/1/iniciar-colocacao
Authorization: Bearer <token>
```
**Resposta:** `202 Accepted`
```json
{
  "message": "O processo de seriação e colocação foi iniciado. Os resultados estarão disponíveis em breve."
}
```

#### Publicar os resultados
Este pedido aciona um processo que torna os resultados visíveis aos candidatos e envia notificações (ex: email). Não altera diretamente os dados, mas sim a sua visibilidade.

```http
POST /api/v1/admin/fases/1/publicar-resultados
Authorization: Bearer <token>
```
**Resposta:** `202 Accepted`
```json
{
  "message": "Os resultados estão a ser publicados e os candidatos notificados."
}
```

---

## Processo 4: Matrícula (Fluxo do Candidato Colocado)

### 4.1. Ver Resultado e Realizar Matrícula

#### Obter o estado da candidatura (após publicação)
Este pedido consulta as tabelas `Candidatura` e `OpcaoCurso` para mostrar ao candidato o seu resultado final.

```http
GET /api/v1/candidaturas/101
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "id": 101,
  "estado": "COLOCADO",
  "nota_final_serica": 17.90,
  "opcoes": [
    {
      "curso_id": 15,
      "ordem_preferencia": 1,
      "estado_opcao": "COLOCADO",
      "curso": {
        "nome": "Mestrado em Engenharia Informática"
      }
    },
    {
      "curso_id": 18,
      "ordem_preferencia": 2,
      "estado_opcao": "EXCLUIDO_POR_PREFERENCIA_SUPERIOR",
      "curso": {
        "nome": "Mestrado em Gestão"
      }
    }
  ]
}
```

#### Realizar a matrícula
Este pedido atualiza o estado na tabela `Candidatura` para `MATRICULADO` e pode acionar outros processos, como a criação de um registo de aluno no sistema de gestão académica.

```http
POST /api/v1/candidaturas/101/matricular
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "id": 101,
  "estado": "MATRICULADO",
  "message": "Matrícula realizada com sucesso. O seu número de aluno é 202500123."
}
```

**Resposta:** `201 Created`
```json
{
  "utilizador": {
    "id": 1,
    "nome_completo": "Ana Silva",
    "email": "ana.silva@email.com",
    "nif": "285948372",
    "ativo": true,
    "data_criacao": "2025-10-23T10:00:00Z"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Login de um utilizador existente
Este pedido verifica as credenciais na tabela `Utilizador` e, se forem válidas, gera um token de autenticação.

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
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 1.2. Ver Concursos e Iniciar Candidatura

#### Obter lista de concursos com fases abertas
Este pedido consulta as tabelas `Concurso` e `Fase` para devolver os concursos que têm fases de candidatura atualmente a decorrer.

```http
GET /api/v1/concursos?abertos=true
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "id": 1,
    "ano_letivo": "2025/2026",
    "tipo": "MESTRADO",
    "name": "Mestrado em Engenharia Informática",
    "fases_abertas": [
      {
        "id": 1,
        "nome": "1ª Fase",
        "data_inicio_candidatura": "2025-09-01",
        "data_fim_candidatura": "2025-10-30"
      }
    ]
  }
]
```

#### Iniciar uma nova candidatura (cria um rascunho)
Este pedido cria um novo registo na tabela `Candidatura` com o estado `RASCUNHO`, associando o utilizador autenticado à fase escolhida.

```http
POST /api/v1/candidaturas
Authorization: Bearer <token>
Content-Type: application/json

{
  "fase_id": 1
}
```
**Resposta:** `201 Created`
```json
{
  "id": 101,
  "utilizador_id": 1,
  "fase_id": 1,
  "origem": "LOCAL",
  "estado": "RASCUNHO",
  "data_submissao": null,
  "nota_final_serica": null
}
```

### 1.3. Preenchimento da Candidatura

#### Obter documentos requeridos para o concurso
Este pedido consulta a tabela `DocumentoRequerido` para listar os documentos necessários para um determinado concurso.

```http
GET /api/v1/concursos/1/documentos_requeridos
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "id": 1,
    "nome": "Certificado de Habilitações",
    "obrigatorio": true
  },
  {
    "id": 2,
    "nome": "Curriculum Vitae",
    "obrigatorio": true
  },
  {
    "id": 3,
    "nome": "Carta de Motivação",
    "obrigatorio": false
  }
]
```

#### Fazer upload de um documento
Este pedido cria um registo na tabela `DocumentoSubmetido`, associando o ficheiro enviado a uma candidatura e a um tipo de documento requerido.

```http
POST /api/v1/candidaturas/101/documentos
Authorization: Bearer <token>
Content-Type: multipart/form-data; boundary=boundary

--boundary
Content-Disposition: form-data; name="documento_requerido_id"

1
--boundary
Content-Disposition: form-data; name="file"; filename="certificado.pdf"
Content-Type: application/pdf

<...conteúdo binário do ficheiro...>
--boundary--
```
**Resposta:** `201 Created`
```json
{
  "id": 201,
  "candidatura_id": 101,
  "documento_requerido_id": 1,
  "url_ficheiro": "https://storage.example.com/path/to/certificado.pdf",
  "estado_validacao": "PENDENTE",
  "observacoes_validacao": null
}
```

#### Adicionar opções de curso à candidatura
Este pedido cria um ou mais registos na tabela `OpcaoCurso`, definindo as escolhas de curso do candidato e a sua ordem de preferência.

```http
POST /api/v1/candidaturas/101/opcoes
Authorization: Bearer <token>
Content-Type: application/json

[
  { "curso_id": 15, "ordem": 1 },
  { "curso_id": 18, "ordem": 2 }
]
```
**Resposta:** `201 Created`
```json
[
  {
    "id": 301,
    "candidatura_id": 101,
    "curso_id": 15,
    "ordem_preferencia": 1,
    "estado_opcao": null
  },
  {
    "id": 302,
    "candidatura_id": 101,
    "curso_id": 18,
    "ordem_preferencia": 2,
    "estado_opcao": null
  }
]
```

### 1.4. Submissão Final

#### Submeter a candidatura para análise
Este pedido atualiza o registo na tabela `Candidatura`, alterando o seu estado de `RASCUNHO` para `SUBMETIDA`.

```http
POST /api/v1/candidaturas/101/submit
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "id": 101,
  "utilizador_id": 1,
  "fase_id": 1,
  "origem": "LOCAL",
  "estado": "SUBMETIDA",
  "data_submissao": "2025-10-23T11:30:00Z",
  "nota_final_serica": null
}
```

---

## Processo 2: Validação (Fluxo do Admin)

### 2.1. Análise Documental

#### Obter candidaturas que precisam de validação
Este pedido consulta a tabela `Candidatura` para encontrar todas as candidaturas com o estado `SUBMETIDA`, para que um administrador as possa rever.

```http
GET /api/v1/admin/candidaturas?estado=SUBMETIDA
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "id": 101,
    "estado": "SUBMETIDA",
    "data_submissao": "2025-10-23T11:30:00Z",
    "utilizador": {
        "id": 1,
        "nome_completo": "Ana Silva"
    }
  }
]
```

#### Validar ou Rejeitar um documento submetido
Este pedido atualiza o `estado_validacao` de um registo específico na tabela `DocumentoSubmetido`.

```http
PUT /api/v1/admin/documentos_submetidos/201
Authorization: Bearer <token>
Content-Type: application/json

{
  "estado_validacao": "VALIDADO",
  "observacoes_validacao": "Documento conforme."
}
```
**Resposta:** `200 OK`
```json
{
  "id": 201,
  "estado_validacao": "VALIDADO",
  "observacoes_validacao": "Documento conforme."
}
```

### 2.2. Finalização da Análise

#### Mudar o estado da candidatura (após análise de todos os docs)
Este pedido atualiza o `estado` de um registo na tabela `Candidatura` para `VALIDADA` ou `INVALIDADA`, após a análise de todos os documentos.

```http
PUT /api/v1/admin/candidaturas/101
Authorization: Bearer <token>
Content-Type: application/json

{
  "estado": "VALIDADA"
}
```
**Resposta:** `200 OK`
```json
{
  "id": 101,
  "estado": "VALIDADA",
  "data_submissao": "2025-10-23T11:30:00Z"
}
```

---

## Processo 3: Seriação e Colocação (Fluxo do Admin/Diretor de Curso)

### 3.1. Atribuição de Notas

#### Obter candidaturas validadas para um curso
Este pedido consulta as tabelas `Candidatura`, `OpcaoCurso` e `DocumentoSubmetido` para apresentar ao avaliador as candidaturas validadas de um curso específico.

```http
GET /api/v1/admin/cursos/15/candidaturas?estado=VALIDADA
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "id": 101,
    "utilizador": { "id": 1, "nome_completo": "Ana Silva" },
    "documentos": [
      { "nome": "Certificado de Habilitações", "url": "..." },
      { "nome": "Curriculum Vitae", "url": "..." }
    ]
  }
]
```

#### Inserir as notas dos critérios de seriação
Este pedido cria um ou mais registos na tabela `NotaComponente`, associando as notas de avaliação de um diretor de curso a uma candidatura.

```http
POST /api/v1/admin/candidaturas/101/notas
Authorization: Bearer <token>
Content-Type: application/json

[
  { "criterio_id": 5, "valor_nota": 18.50 },
  { "criterio_id": 6, "valor_nota": 17.00 }
]
```
**Resposta:** `201 Created`
```json
[
  {
    "id": 501,
    "candidatura_id": 101,
    "criterio_seriacao_id": 5,
    "valor_nota": 18.50
  },
  {
    "id": 502,
    "candidatura_id": 101,
    "criterio_seriacao_id": 6,
    "valor_nota": 17.00
  }
]
```

### 3.2. Colocação e Publicação

*(Estes passos são tipicamente acionados por botões na UI de admin que disparam background jobs. As respostas podem ser simples confirmações.)*

#### Disparar o algoritmo de colocação para uma fase
Este pedido inicia um processo de fundo que lê as tabelas `Candidatura`, `NotaComponente` e `CriterioSeriacao` para calcular a `nota_final_sericao` e atualizar o estado em `Candidatura` e `OpcaoCurso`.

```http
POST /api/v1/admin/fases/1/iniciar-colocacao
Authorization: Bearer <token>
```
**Resposta:** `202 Accepted`
```json
{
  "message": "O processo de seriação e colocação foi iniciado. Os resultados estarão disponíveis em breve."
}
```

#### Publicar os resultados
Este pedido aciona um processo que torna os resultados visíveis aos candidatos e envia notificações (ex: email). Não altera diretamente os dados, mas sim a sua visibilidade.

```http
POST /api/v1/admin/fases/1/publicar-resultados
Authorization: Bearer <token>
```
**Resposta:** `202 Accepted`
```json
{
  "message": "Os resultados estão a ser publicados e os candidatos notificados."
}
```

---

## Processo 4: Matrícula (Fluxo do Candidato Colocado)

### 4.1. Ver Resultado e Realizar Matrícula

#### Obter o estado da candidatura (após publicação)
Este pedido consulta as tabelas `Candidatura` e `OpcaoCurso` para mostrar ao candidato o seu resultado final.

```http
GET /api/v1/candidaturas/101
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "id": 101,
  "estado": "COLOCADO",
  "nota_final_serica": 17.90,
  "opcoes": [
    {
      "curso_id": 15,
      "ordem_preferencia": 1,
      "estado_opcao": "COLOCADO",
      "curso": {
        "nome": "Mestrado em Engenharia Informática"
      }
    },
    {
      "curso_id": 18,
      "ordem_preferencia": 2,
      "estado_opcao": "EXCLUIDO_POR_PREFERENCIA_SUPERIOR",
      "curso": {
        "nome": "Mestrado em Gestão"
      }
    }
  ]
}
```

#### Realizar a matrícula
Este pedido atualiza o estado na tabela `Candidatura` para `MATRICULADO` e pode acionar outros processos, como a criação de um registo de aluno no sistema de gestão académica.

```http
POST /api/v1/candidaturas/101/matricular
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "id": 101,
  "estado": "MATRICULADO",
  "message": "Matrícula realizada com sucesso. O seu número de aluno é 202500123."
}
```