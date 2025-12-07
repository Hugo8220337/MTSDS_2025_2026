# Exemplos de API Requests & Responses (Microserviço de Horários)

Este ficheiro detalha os endpoints da API para gestão de salas, horários e aulas.

---

Base URL: `/api/v1/schedules`

## Processo 1: Gestão de Salas (Fluxo do Admin)

### 1.1. Criar e Gerir Salas

#### Criar uma nova sala
```http
POST /api/v1/admin/schools/{schoolAcronym}/create-classrom

Authorization: Bearer <token>
Content-Type: application/json

{
  "edificio": "A",
  "piso": 2,
  "numero": "201",
  "tipo": "SALA_AULA",
  "capacidade": 30
}
```
**Resposta:** `201 Created`
```json
{
  "id": 1,
  "edificio": "A",
  "piso": 2,
  "numero": "201",
  "codigo_sala": "A201",
  "tipo": "SALA_AULA",
  "capacidade": 30,
  "escola_id": 5
}
```

#### Listar equipamentos disponíveis
```http
GET /api/v1/admin/equipment

Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "equipamentos": [
    {
      "id": 1,
      "nome": "Projetor",
      "descricao": "Projetor HD com HDMI"
    },
    {
      "id": 2,
      "nome": "Quadro Interativo",
      "descricao": "Quadro touch screen 65 polegadas"
    },
    {
      "id": 3,
      "nome": "Computador",
      "descricao": "PC com Windows 11 e Office"
    }
  ]
}
```

#### Adicionar equipamento a uma sala
```http
POST /api/v1/admin/classrooms/{classroomId}/equipment

Authorization: Bearer <token>
Content-Type: application/json

{
  "equipamento_id": 1,
  "quantidade": 1,
  "observacoes": "Projetor instalado no teto"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 15,
  "sala_id": 1,
  "equipamento": {
    "id": 1,
    "nome": "Projetor"
  },
  "quantidade": 1,
  "observacoes": "Projetor instalado no teto"
}
```

#### Listar equipamentos de uma sala
```http
GET /api/v1/admin/classrooms/{classroomId}/equipment

Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "sala": {
    "id": 1,
    "codigo": "A201",
    "capacidade": 30
  },
  "equipamentos": [
    {
      "id": 1,
      "nome": "Projetor",
      "quantidade": 1,
      "observacoes": "Projetor instalado no teto"
    },
    {
      "id": 3,
      "nome": "Computador",
      "quantidade": 20,
      "observacoes": "Computadores para alunos"
    }
  ]
}
```

#### Obter disponibilidade de uma sala
```http
GET /api/v1/admin/classrooms/{classroomId}/availability?date=2025-11-01

Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "sala": {
    "id": 1,
    "codigo": "A201",
    "edificio": "A",
    "piso": 2
  },
  "data": "2025-11-01",
  "ocupacoes": [
    {
      "tipo": "AULA_PERIODICA",
      "hora_inicio": "08:00",
      "hora_fim": "09:30",
      "turma": "LEI1A",
      "uc": "POO",
      "docente": "Prof. João Silva"
    },
    {
      "tipo": "RESERVA",
      "hora_inicio": "14:30",
      "hora_fim": "16:30",
      "titulo": "Teste de Programação Web",
      "responsavel": "Prof. Maria Santos"
    }
  ]
}
```

---

## Processo 2: Gestão de Horários de Turma (Fluxo do Admin)

### 2.1. Criar Horário de Turma (Contexto do Semestre)

#### Criar um horário para uma turma/UC no semestre
```http
POST /api/v1/admin/create-class-schedule

Authorization: Bearer <token>
Content-Type: application/json

{
  "turma_id": 10,
  "unidade_curricular_id": 25,
  "ano_letivo_id": 3,
  "semestre": 1,
  "data_inicio": "2025-09-15",
  "data_fim": "2025-12-20"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 50,
  "turma": {
    "id": 10,
    "codigo": "LEI1A"
  },
  "unidade_curricular": {
    "id": 25,
    "codigo": "POO",
    "nome": "Programação Orientada a Objetos"
  },
  "ano_letivo": "2025/2026",
  "semestre": 1,
  "periodo": {
    "inicio": "2025-09-15",
    "fim": "2025-12-20"
  }
}
```

### 2.2. Agendar Aulas Periódicas

#### Criar uma aula periódica (que se repete semanalmente)
```http
POST /api/v1/admin/class-schedules/{scheduleId}/create-periodic-lesson

Authorization: Bearer <token>
Content-Type: application/json

{
  "docente_id": 42,
  "sala_id": 1,
  "dia_semana": 1,
  "hora_inicio": "08:00",
  "hora_fim": "09:30",
  "tipo_aula": "TEORICA"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 100,
  "horario_turma": {
    "id": 50,
    "turma": "LEI1A",
    "uc": "POO"
  },
  "docente": {
    "id": 42,
    "nome": "Prof. João Silva",
    "numero_mecanografico": "D9876"
  },
  "sala": {
    "id": 1,
    "codigo": "A201"
  },
  "dia_semana": 1,
  "dia_semana_nome": "Segunda-feira",
  "hora_inicio": "08:00",
  "hora_fim": "09:30",
  "duracao_minutos": 90,
  "tipo_aula": "TEORICA",
  "recorrencia": "Todas as segundas-feiras, 08:00-09:30, de 15/09/2025 a 20/12/2025"
}
```

#### Listar aulas de um horário de turma
```http
GET /api/v1/admin/class-schedules/{scheduleId}/lessons

Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "horario_turma": {
    "id": 50,
    "turma": "LEI1A",
    "uc": "POO",
    "semestre": 1,
    "periodo": "15/09/2025 a 20/12/2025"
  },
  "aulas": [
    {
      "id": 100,
      "dia_semana": 1,
      "dia_semana_nome": "Segunda-feira",
      "hora_inicio": "08:00",
      "hora_fim": "09:30",
      "sala": "A201",
      "docente": "Prof. João Silva",
      "tipo_aula": "TEORICA"
    },
    {
      "id": 101,
      "dia_semana": 3,
      "dia_semana_nome": "Quarta-feira",
      "hora_inicio": "14:30",
      "hora_fim": "16:00",
      "sala": "Lab-1",
      "docente": "Prof. Maria Santos",
      "tipo_aula": "PRATICA"
    }
  ]
}
```

---

## Processo 3: Gestão de Exceções ao Horário (Fluxo do Admin)

### 3.1. Criar Exceções Pontuais

#### Cancelar uma aula num dia específico
```http
POST /api/v1/admin/lessons/{lessonId}/create-exception

Authorization: Bearer <token>
Content-Type: application/json

{
  "data": "2025-11-15",
  "tipo_excecao": "CANCELAMENTO",
  "observacoes": "Feriado - Dia da Proclamação da República"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 200,
  "aula_periodica": {
    "id": 100,
    "turma": "LEI1A",
    "uc": "POO",
    "dia_semana": "Segunda-feira",
    "hora_inicio": "08:00"
  },
  "data": "2025-11-15",
  "tipo_excecao": "CANCELAMENTO",
  "observacoes": "Feriado - Dia da Proclamação da República"
}
```

#### Mudar sala numa data específica
```http
PUT /api/v1/admin/lessons/{lessonId}/exceptions

Authorization: Bearer <token>
Content-Type: application/json

{
  "data": "2025-11-22",
  "tipo_excecao": "MUDANCA_SALA",
  "sala_alternativa_id": 5,
  "observacoes": "Sala A201 indisponível para manutenção"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 201,
  "aula_periodica": {
    "id": 100,
    "sala_normal": "A201"
  },
  "data": "2025-11-22",
  "tipo_excecao": "MUDANCA_SALA",
  "sala_alternativa": {
    "id": 5,
    "codigo": "B101"
  },
  "observacoes": "Sala A201 indisponível para manutenção"
}
```

#### Mudar horário numa data específica
```http
PUT /api/v1/admin/lessons/{lessonId}/exceptions

Authorization: Bearer <token>
Content-Type: application/json

{
  "data": "2025-11-29",
  "tipo_excecao": "MUDANCA_HORARIO",
  "hora_inicio_alternativa": "10:00",
  "hora_fim_alternativa": "11:30",
  "observacoes": "Horário ajustado devido a evento especial"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 202,
  "aula_periodica": {
    "id": 100,
    "horario_normal": "08:00-09:30"
  },
  "data": "2025-11-29",
  "tipo_excecao": "MUDANCA_HORARIO",
  "horario_alternativo": {
    "hora_inicio": "10:00",
    "hora_fim": "11:30"
  },
  "observacoes": "Horário ajustado devido a evento especial"
}
```

#### Substituir docente numa data específica
```http
PUT /api/v1/admin/lessons/{lessonId}/exceptions

Authorization: Bearer <token>
Content-Type: application/json

{
  "data": "2025-12-06",
  "tipo_excecao": "MUDANCA_DOCENTE",
  "docente_substituto_id": 55,
  "observacoes": "Prof. João Silva em conferência"
}
```
**Resposta:** `201 Created`
```json
{
  "id": 203,
  "aula_periodica": {
    "id": 100,
    "docente_normal": "Prof. João Silva"
  },
  "data": "2025-12-06",
  "tipo_excecao": "MUDANCA_DOCENTE",
  "docente_substituto": {
    "id": 55,
    "nome": "Prof. Ana Costa"
  },
  "observacoes": "Prof. João Silva em conferência"
}
```

#### Listar exceções de uma aula
```http
GET /api/v1/admin/lessons/{lessonId}/exceptions

Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "aula_periodica": {
    "id": 100,
    "turma": "LEI1A",
    "uc": "POO",
    "dia_semana": "Segunda-feira",
    "horario": "08:00-09:30"
  },
  "excecoes": [
    {
      "id": 200,
      "data": "2025-11-15",
      "tipo_excecao": "CANCELAMENTO",
      "observacoes": "Feriado"
    },
    {
      "id": 201,
      "data": "2025-11-22",
      "tipo_excecao": "MUDANCA_SALA",
      "sala_alternativa": "B101"
    }
  ]
}
```

---

## Processo 4: Consulta de Horários (Fluxo do Aluno/Docente)

### 4.1. Consultar Horário Pessoal

#### Obter horário do aluno para uma semana
```http
GET /api/v1/students/me/schedule?week=2025-11-10

Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "startDate": "2025-11-10",
  "endDate": "2025-11-14",    
  "aulas": [
    {
      "dia": "Segunda-feira",
      "data": "2025-11-10",
      "hora_inicio": "08:00",
      "hora_fim": "09:30",
      "uc": {
        "codigo": "POO",
        "nome": "Programação Orientada a Objetos"
      },
      "turma": "LEI1A",
      "sala": "A201",
      "docente": "Prof. João Silva",
      "tipo_aula": "TEORICA"
    },
    {
      "dia": "Quarta-feira",
      "data": "2025-11-12",
      "hora_inicio": "14:30",
      "hora_fim": "16:00",
      "uc": {
        "codigo": "POO",
        "nome": "Programação Orientada a Objetos"
      },
      "turma": "LEI1A",
      "sala": "Lab-1",
      "docente": "Prof. Maria Santos",
      "tipo_aula": "PRATICA"
    }
  ],
  "excecoes": [
    {
      "data": "2025-11-15",
      "aula_original": {
        "hora": "08:00-09:30",
        "uc": "POO",
        "sala": "A201"
      },
      "tipo_excecao": "CANCELAMENTO",
      "motivo": "Feriado"
    }
  ]
}
```

#### Obter horário do aluno para um dia específico
```http
GET /api/v1/students/me/schedule?date=2025-11-10

Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "data": "2025-11-10",
  "dia_semana": "Segunda-feira",
  "aulas": [
    {
      "hora_inicio": "08:00",
      "hora_fim": "09:30",
      "uc": "POO",
      "turma": "LEI1A",
      "sala": "A201",
      "docente": "Prof. João Silva",
      "tipo_aula": "TEORICA"
    },
    {
      "hora_inicio": "10:00",
      "hora_fim": "11:30",
      "uc": "BD",
      "turma": "LEI1A",
      "sala": "B205",
      "docente": "Prof. Ana Costa",
      "tipo_aula": "TEORICA"
    }
  ]
}
```

#### Obter horário do docente
```http
GET /api/v1/teachers/me/schedule?week=2025-11-10

Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "docente": {
    "id": 42,
    "nome": "Prof. João Silva",
    "numero_mecanografico": "D9876"
  },
  "semana": {
    "inicio": "2025-11-10",
    "fim": "2025-11-14"
  },
  "aulas": [
    {
      "dia": "Segunda-feira",
      "data": "2025-11-10",
      "hora_inicio": "08:00",
      "hora_fim": "09:30",
      "uc": "POO",
      "turma": "LEI1A",
      "sala": "A201",
      "tipo_aula": "TEORICA"
    },
    {
      "dia": "Terça-feira",
      "data": "2025-11-11",
      "hora_inicio": "10:00",
      "hora_fim": "11:30",
      "uc": "POO",
      "turma": "LEI1B",
      "sala": "A302",
      "tipo_aula": "TEORICA"
    }
  ]
}
```

### 4.2. Consultar Ocupação de Salas

#### Obter ocupação de todas as salas numa data
```http
GET /api/v1/schools/{schoolAcronym}/classrooms/occupation?date=2025-11-10

Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "escola": "ESTG",
  "data": "2025-11-10",
  "dia_semana": "Segunda-feira",
  "salas": [
    {
      "id": 1,
      "codigo": "A201",
      "capacidade": 30,
      "ocupacoes": [
        {
          "tipo": "AULA_PERIODICA",
          "hora_inicio": "08:00",
          "hora_fim": "09:30",
          "turma": "LEI1A",
          "uc": "POO",
          "docente": "Prof. João Silva"
        },
        {
          "tipo": "AULA_PERIODICA",
          "hora_inicio": "10:00",
          "hora_fim": "11:30",
          "turma": "LEI2A",
          "uc": "ES",
          "docente": "Prof. Maria Santos"
        }
      ]
    },
    {
      "id": 5,
      "codigo": "Lab-1",
      "capacidade": 25,
      "ocupacoes": [
        {
          "tipo": "RESERVA",
          "hora_inicio": "14:30",
          "hora_fim": "16:30",
          "titulo": "Teste de Programação Web",
          "responsavel": "Prof. Carlos Oliveira"
        }
      ]
    }
  ]
}
```

#### Obter salas livres num horário específico
```http
GET /api/v1/schools/{schoolAcronym}/classrooms/free?date=2025-11-10&hora_inicio=14:00&hora_fim=16:00

Authorization: Bearer <token>
```
**Resposta:** `200 OK`
```json
{
  "parametros": {
    "data": "2025-11-10",
    "hora_inicio": "14:00",
    "hora_fim": "16:00"
  },
  "salas_livres": [
    {
      "id": 2,
      "codigo": "A202",
      "edificio": "A",
      "piso": 2,
      "tipo": "SALA_AULA",
      "capacidade": 35,
      "equipamentos": ["Projetor", "Computador"]
    },
    {
      "id": 8,
      "codigo": "B101",
      "edificio": "B",
      "piso": 1,
      "tipo": "SALA_AULA",
      "capacidade": 40,
      "equipamentos": ["Projetor", "Quadro Interativo"]
    }
  ]
}
```

---

## Processo 5: Reservas de Sala (Eventos Pontuais)

---

## Processo 5: Reservas de Sala (Eventos Pontuais)

### 5.1. Criar uma reserva de sala
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
  "tipo_reserva": "TESTE",
  "observacoes": "Teste de frequência - 1º semestre"
}
```

**Resposta:** `201 Created`
```json
{
  "id": 456,
  "sala": {
    "id": 15,
    "codigo": "Lab-1",
    "edificio": "A",
    "piso": 2,
    "capacidade": 30
  },
  "titulo": "Teste de Programação Web",
  "data_hora_inicio": "2025-11-15T14:30:00Z",
  "data_hora_fim": "2025-11-15T16:30:00Z",
  "responsavel": {
    "id": 78,
    "nome": "Prof. Carlos Oliveira"
  },
  "tipo_reserva": "TESTE",
  "observacoes": "Teste de frequência - 1º semestre",
  "created_at": "2025-11-05T10:30:00Z"
}
```

### 5.2. Obter detalhes de uma reserva

```http
GET /api/v1/reservations/{reservationId}

Authorization: Bearer <token>
```

**Resposta:** `200 OK`
```json
{
  "id": 456,
  "sala": {
    "id": 15,
    "codigo": "Lab-1",
    "edificio": "A",
    "piso": 2,
    "capacidade": 30,
    "equipamentos": [
      {
        "nome": "Computador",
        "quantidade": 25
      },
      {
        "nome": "Projetor",
        "quantidade": 1
      }
    ]
  },
  "titulo": "Teste de Programação Web",
  "data_hora_inicio": "2025-11-15T14:30:00Z",
  "data_hora_fim": "2025-11-15T16:30:00Z",
  "responsavel": {
    "id": 78,
    "nome": "Prof. Carlos Oliveira",
    "numero_mecanografico": "D5432"
  },
  "tipo_reserva": "TESTE",
  "observacoes": "Teste de frequência - 1º semestre",
  "created_at": "2025-11-05T10:30:00Z"
}
```

### 5.3. Listar reservas de um responsável

```http
GET /api/v1/teachers/me/reservations?data_inicio=2025-11-01&data_fim=2025-11-30

Authorization: Bearer <token>
```

**Resposta:** `200 OK`
```json
{
  "responsavel": {
    "id": 78,
    "nome": "Prof. Carlos Oliveira"
  },
  "periodo": {
    "inicio": "2025-11-01",
    "fim": "2025-11-30"
  },
  "reservas": [
    {
      "id": 456,
      "sala": "Lab-1",
      "titulo": "Teste de Programação Web",
      "data_hora_inicio": "2025-11-15T14:30:00Z",
      "data_hora_fim": "2025-11-15T16:30:00Z",
      "tipo_reserva": "TESTE"
    },
    {
      "id": 457,
      "sala": "B205",
      "titulo": "Reunião de Departamento",
      "data_hora_inicio": "2025-11-20T10:00:00Z",
      "data_hora_fim": "2025-11-20T12:00:00Z",
      "tipo_reserva": "REUNIAO"
    }
  ]
}
```

### 5.4. Atualizar uma reserva

```http
PUT /api/v1/reservations/{reservationId}

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
    "codigo": "Lab-2"
  },
  "data_hora_inicio": "2025-11-22T14:30:00Z",
  "data_hora_fim": "2025-11-22T16:30:00Z",
  "updated_at": "2025-11-06T09:15:00Z"
}
```

### 5.5. Cancelar uma reserva

```http
DELETE /api/v1/reservations/{reservationId}

Authorization: Bearer <token>
```

**Resposta:** `204 No Content`
