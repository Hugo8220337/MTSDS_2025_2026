# Exemplos de API Requests & Responses (Microserviço de Horários)

Este ficheiro detalha os endpoints da API para gestão de salas, horários e aulas.

---

base URL: `/api/v1/schedules`

## Processo 1: Gestão de Salas (Fluxo do Admin)

### 1.1. Criar e Gerir Salas

#### Criar uma nova sala
```http
POST /api/v1/admin/schools/{schoolAcronym}/create-classroom

Authorization: Bearer <token>
Content-Type: application/json

{
  "edificio": "A",
  "piso": 2,
  "numero": "201",
  "tipo": "SALA_AULA",
  "capacidade": 30,
  "equipamento": ["Projetor", "Computador"]
}
```
**Resposta:** `201 Created`
```json
{
  "id": 1,
  "codigo_sala": "A201",
  "tipo": "SALA_AULA",
  "capacidade": 30
}
```

#### Obter disponibilidade de uma sala
```http
GET/api/v1/admin/classrooms/{classroomNumber}/available?date=2025-11-01

Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "sala": "A201",
  "data": "2025-11-01",
  "slots_ocupados": [
    {
      "hora_inicio": "08:00",
      "hora_fim": "10:00",
      "turma": "LEI1A",
      "uc": "POO"
    }
  ]
}
```

---

## Processo 3: Gestão de Horários (Fluxo do Admin)

### 3.1. Definir Slots de Horário

#### Criar um novo slot horário
```http
POST /api/v1/admin/create-schedule-slot

Authorization: Bearer <token>
Content-Type: application/json

{
  "hora_inicio": "08:00",
  "hora_fim": "09:30",
  "tipo_slot": "AULA_NORMAL"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 1,
  "hora_inicio": "08:00",
  "hora_fim": "09:30",
  "duracao_minutos": 90
}
```

### 3.2. Agendar Aulas

#### Criar uma aula regular
```http
/api/v1/admin/classes/{classNumber}/lessons

Authorization: Bearer <token>
Content-Type: application/json

{
  "docente_numero": "D9876",
  "sala": "A201",
  "slot_horario": 1,
  "dia_semana": 1,
  "tipo_aula": "TEORICA",
  "data_inicio": "2025-09-15",
  "data_fim": "2025-12-20"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 1,
  "turma": "LEI1A",
  "sala": "A201",
  "horario": "Segunda-feira, 08:00-09:30",
  "periodo": "15/09/2025 a 20/12/2025"
}
```

#### Registrar uma exceção (alteração pontual)
```http
POST /api/v1/admin/lessons/{lessonNumber}/create-schedule-exception

Authorization: Bearer <token>
Content-Type: application/json

{
  "data": "2025-11-15",
  "tipo": "ALTERACAO_SALA",
  "sala_alternativa": "B101",
  "observacoes": "Sala A201 indisponível para manutenção"
}
```
**Resposta:** `201 Created`
```json
{
  "aula_id": 1,
  "data": "2025-11-15",
  "alteracao": "Mudança de sala para B101"
}
```

---

## Processo 4: Consulta de Horários (Fluxo do Aluno/Docente)

### 4.1. Consultar Horário Pessoal

#### Obter horário do aluno
```http
GET /api/v1/alunos/me/horario?semana=2025-11-10
/api/v1/students/me/schedule?week=2025-11-10
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "semana": "10/11/2025 a 14/11/2025",
  "aulas": [
    {
      "dia": "Segunda-feira",
      "hora_inicio": "08:00",
      "hora_fim": "09:30",
      "uc": "Programação Orientada a Objetos",
      "turma": "LEI1A",
      "sala": "A201",
      "tipo": "TEORICA"
    }
  ]
}
```

#### Obter horário do docente
```http
GET /api/v1/docentes/me/horario?semana=2025-11-10
/api/v1/teachers/me/schedule?week=2025-11-10
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "semana": "10/11/2025 a 14/11/2025",
  "aulas": [
    {
      "dia": "Segunda-feira",
      "hora_inicio": "08:00",
      "hora_fim": "09:30",
      "uc": "Programação Orientada a Objetos",
      "turma": "LEI1A",
      "sala": "A201",
      "tipo": "TEORICA"
    }
  ]
}
```

### 4.2. Consultar Ocupação de Salas

#### Obter ocupação de todas as salas
```http
GET /api/v1/escolas/estg/salas/ocupacao?data=2025-11-10
/schools/estg/classrooms/ocupied?date=2025-11-10
Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "data": "2025-11-10",
  "salas": [
    {
      "sala": "A201",
      "ocupacao": [
        {
          "hora": "08:00-09:30",
          "turma": "LEI1A",
          "uc": "POO"
        }
      ]
    }
  ]
}
```


## Reservas de Sala (Eventos Pontuais)

### Criar uma reserva de sala
Reserva uma sala para um evento pontual (teste, reunião, etc.)

```http
POST /api/v1/reservate-classrom

Authorization: Bearer <token>
Content-Type: application/json

{
  "sala_id": 15,
  "titulo": "Teste de Programação Web",
  "data_hora_inicio": "2025-11-15T14:30:00Z",
  "data_hora_fim": "2025-11-15T16:30:00Z",
  "observacoes": "Teste de frequência - 1º semestre"
}
```

**Resposta:** `201 Created`
```json
{
  "id": 456,
  "sala": {
    "id": 15,
    "numero": "Lab-1",
    "edificio": "A"
  },
  "titulo": "Teste de Programação Web",
  "data_hora_inicio": "2025-11-15T14:30:00Z",
  "data_hora_fim": "2025-11-15T16:30:00Z",
  "responsavel_id": 78,
  "created_at": "2025-11-05T10:30:00Z"
}
```


### Obter detalhes de uma reserva

```http
GET /api/v1/reservation/{reservationId}
Authorization: Bearer <token>
```

**Resposta:** `200 OK`
```json
{
  "id": 456,
  "sala": {
    "id": 15,
    "numero": "Lab-1",
    "edificio": "A",
    "piso": 2,
    "capacidade": 30
  },
  "titulo": "Teste de Programação Web",
  "data_hora_inicio": "2025-11-15T14:30:00Z",
  "data_hora_fim": "2025-11-15T16:30:00Z",
  "responsavel": {
    "id": 78,
    "nome": "Prof. João Silva"
  },
  "observacoes": "Teste de frequência - 1º semestre",
  "created_at": "2025-11-05T10:30:00Z"
}
```

### Atualizar uma reserva

```http
PUT /api/v1/reservatoin/{reservationId}
Authorization: Bearer <token>
Content-Type: application/json

{
  "titulo": "Teste de Programação Web (Remarcado)",
  "data_hora_inicio": "2025-11-22T14:30:00Z",
  "data_hora_fim": "2025-11-22T16:30:00Z",
  "sala_id": 16
}
```

**Resposta:** `200 OK`
```json
{
  "id": 456,
  "titulo": "Teste de Programação Web (Remarcado)",
  "sala": {
    "id": 16,
    "numero": "Lab-2"
  },
  "data_hora_inicio": "2025-11-22T14:30:00Z",
  "data_hora_fim": "2025-11-22T16:30:00Z",
  "updated_at": "2025-11-06T09:15:00Z"
}
```

### Cancelar uma reserva

```http
DELETE /api/v1/reservation/{reservationId}/delete
Authorization: Bearer <token>
```

**Resposta:** `204 No Content`
