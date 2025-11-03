# Exemplos de API Requests & Responses (Microserviço de Gestão Académica)

Este ficheiro detalha os endpoints da API para gestão de escolas, cursos, unidades curriculares, alunos, docentes, inscrições e turmas.

---

## Processo 1: Gestão da Estrutura Académica (Fluxo do Admin)

Estes endpoints são usados para configurar a estrutura base da instituição.

### 1.1. Gestão de Escolas e Cursos

#### Criar uma nova Escola
```http
POST /api/v1/admin/escolas
/api/v1/admin/schools
Authorization: Bearer <token>
Content-Type: application/json

{
  "nome": "Escola Superior de Tecnologia e Gestão",
  "sigla": "ESTG"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 1,
  "nome": "Escola Superior de Tecnologia e Gestão",
  "sigla": "ESTG"
}
```

#### Criar um novo Curso para uma Escola
```http
POST /api/v1/admin/escolas/estg/cursos
/api/v1/admin/schools/estg/courses
Authorization: Bearer <token>
Content-Type: application/json

{
  "nome": "Engenharia Informática",
  "codigo_curso": "LEI",
  "grau": "Licenciatura"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 15,
  "escola_id": 1,
  "nome": "Engenharia Informática",
  "codigo_curso": "LEI",
  "grau": "Licenciatura"
}
```

### 1.2. Gestão de Unidades Curriculares e Planos de Estudo

#### Criar uma nova Unidade Curricular (UC)
```http
POST /api/v1/admin/unidades-curriculares
/api/v1/admin/curricular-units
Authorization: Bearer <token>
Content-Type: application/json

{
  "nome": "Programação Orientada a Objetos",
  "codigo_uc": "POO",
  "ects": 6.0
}
```
**Resposta:** `201 Created`
```json
{
  "id": 101,
  "nome": "Programação Orientada a Objetos",
  "codigo_uc": "POO",
  "ects": 6.0
}
```

#### Associar uma UC a um Plano de Estudos de um Curso
```http
POST /api/v1/admin/cursos/LEI/plano-estudos
/api/v1/admin/courses/LEI/study-plans
Authorization: Bearer <token>
Content-Type: application/json

{
  "unidade_curricular_codigo": "POO",
  "ano_curricular": 1,
  "semestre": 2
}
```
**Resposta:** `201 Created`
```json
{
  "id": 500,
  "curso_id": 15,
  "unidade_curricular_id": 101,
  "ano_curricular": 1,
  "semestre": 2
}
```

---

## Processo 2: Gestão de Turmas (Fluxo do Admin)

### 2.1. Gestão de Turmas

#### Criar uma nova turma para uma UC
```http
POST /api/v1/admin/unidades-curriculares/{ucId}/turmas
/api/v1/admin/curricular-units/{ucId}/classes
Authorization: Bearer <token>
Content-Type: application/json

{
  "codigo_turma": "LEI1A",
  "ano_letivo": "2025/2026",
  "tipo": "DIURNO",
  "vagas_max": 30
}
```
**Resposta:** `201 Created`
```json
{
  "id": 1,
  "codigo_turma": "LEI1A",
  "unidade_curricular": {
    "codigo": "POO",
    "nome": "Programação Orientada a Objetos"
  },
  "vagas_disponiveis": 30
}
```

#### Inscrever alunos numa turma
```http
POST /api/v1/admin/turmas/{turmaId}/alunos
/admin/classes/{turmaId}/students
Authorization: Bearer <token>
Content-Type: application/json

{
  "inscricoes": [
    { "inscricao_id": 1501 },
    { "inscricao_id": 1502 },
    { "inscricao_id": 1503 }
  ]
}
```
**Resposta:** `201 Created`
```json
{
  "turma": "LEI1A",
  "alunos_inscritos": 3,
  "vagas_restantes": 27
}
```

#### Listar alunos de uma turma
```http
GET /api/v1/admin/turmas/{turmaId}/alunos
/admin/classes/{turmaId}/students
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "turma": "LEI1A",
  "alunos": [
    {
      "numero": "8220337",
      "nome": "João Silva",
      "inscricao_id": 1501,
      "data_inscricao": "2025-09-01T10:00:00Z"
    },
    {
      "numero": "8220307",
      "nome": "Maria Santos",
      "inscricao_id": 1502,
      "data_inscricao": "2025-09-01T10:05:00Z"
    }
  ],
  "total_alunos": 2,
  "vagas_restantes": 28
}
```

---

## Processo 3: Gestão de Alunos e Docentes (Fluxo do Admin e Eventos)

Estes endpoints gerem os registos académicos. A criação é tipicamente acionada por eventos de outros serviços.

#### Obter dados de um Aluno
```http
GET /api/v1/admin/alunos/8220337
/admin/students/8220337
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "id": 845,
  "utilizador_id": 1,
  "numero_aluno": "202500123",
  "curso_codigo": "LEI",
  "estado": "ATIVO"
}
```

#### Associar um Docente a uma Unidade Curricular (lecionação)
coloquei D9876 como se fosse o Id do docente, mas o id é inteiro, então tem que se normaliza de outra forma
```http
POST /api/v1/admin/docentes/D9876/lecionacoes
/admin/teachers/D9876/teachings
Authorization: Bearer <token>
Content-Type: application/json

{
  "unidade_curricular_codigo": "POO",
  "ano_letivo": "2025/2026"
}
```
**Resposta:** `201 Created`
```json
{
  "docente_numero": "D8200326",
  "unidade_curricular_codigo": "POO",
  "ano_letivo": "2025/2026"
}
```

---

## Processo 3: Inscrições e Consultas (Fluxo do Aluno e Docente)

### 3.1. Processo de Inscrição do Aluno

#### Obter UCs disponíveis para inscrição (para um aluno)
Devolve as UCs do plano de estudos do aluno que ele possa frequentar o ano letivo atual.

```http
GET /api/v1/alunos/me/inscricoes/disponiveis?ano_letivo=2025/2026
/students/me/registrations/available?school_year=2025/2026
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "unidade_curricular_id": 101,
    "nome": "Programação Orientada a Objetos",
    "ano_curricular": 1,
    "semestre": 2,
    "ects": 6.0
  },
  {
    "unidade_curricular_id": 102,
    "nome": "Bases de Dados",
    "ano_curricular": 1,
    "semestre": 2,
    "ects": 6.0
  }
]
```

#### Realizar inscrição em Unidades Curriculares
```http
POST /api/v1/alunos/me/inscricoes
/students/me/registrations
Authorization: Bearer <token>
Content-Type: application/json

{
  "ano_letivo": "2025/2026",
  "unidade_curricular_codigos": ["POO", "BD"]
}
```
**Resposta:** `201 Created`
```json
{
  "message": "Inscrição realizada com sucesso em 2 unidades curriculares.",
  "inscricoes": [
    { "id": 1501, "unidade_curricular_id": 101, "estado": "INSCRITO" },
    { "id": 1502, "unidade_curricular_id": 102, "estado": "INSCRITO" }
  ]
}
```

### 3.2. Consultas de Docentes e Alunos

#### Obter histórico de inscrições (pauta do aluno)
```http
GET /api/v1/alunos/me/inscricoes
/students/me/registrations
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "ano_letivo": "2025/2026",
    "unidade_curricular": {
      "nome": "Programação Orientada a Objetos",
      "codigo_uc": "POO"
    },
    "estado": "INSCRITO",
    "nota_final": null
  }
]
```

#### Obter alunos inscritos numa UC (para um docente)
```http
GET /api/v1/docentes/me/unidades-curriculares/POO/alunos?ano_letivo=2025/2026
/api/v1/teachers/me/curricular-units/POO/students?school_year=2025/2026
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
[
  {
    "aluno_id": 8220337,
    "numero_aluno": "202500123",
    "nome_aluno": "Ana Silva" // O nome vem de uma chamada interna/cache do serviço de Identidade
  },
  {
    "aluno_id": 8220307,
    "numero_aluno": "202500124",
    "nome_aluno": "Carlos Mendes"
  }
]
```