# Grade Reviews API

Base URL: `/api/v1/grade-reviews`

This API follows a DDD approach, where endpoints represent clear business commands.

---

## 1. Requesting a Review (Student)

### `POST submit-request`
Submits a new request for a grade review.

**Use Case:** A student disagrees with a grade and wants to formally request a review.
**State Transition:** `(none)` -> `PENDING_APPROVAL`

**Request Body:**
```json
{
  "grade_id": 123,
  "justification": "I believe the sum of the points for question 3 is incorrect."
}
```

**Response (201 Created):**
```json
{
  "id": 456,
  "grade_id": 123,
  "student_id": 789,
  "status": "PENDING_APPROVAL",
  "created_at": "2025-11-05T10:30:00Z"
}
```

---

## 2. Managing the Review (Lecturer)

### `PATCH /{id}/schedule-meeting`
Schedules the in-person meeting with the student to discuss the review.

**Use Case:** The lecturer accepts the request and sets a date/time for the meeting.
**State Transition:** `PENDING_APPROVAL` -> `MEETING_SCHEDULED`

**Request Body:**
```json
{
  "meeting_datetime": "2025-11-10T15:00:00Z",
  "meeting_location": "Lecturer's Office - Room 3.21"
}
```

**Response:**
```json
{
  "id": 456,
  "status": "MEETING_SCHEDULED",
  "meeting_datetime": "2025-11-10T15:00:00Z",
  "meeting_location": "Lecturer's Office - Room 3.21",
  "updated_at": "2025-11-06T11:00:00Z"
}
```

---
## 3. Conclusão da Revisão (Docente)

Após a reunião, o docente pode concluir o processo de duas formas, usando comandos explícitos que refletem a intenção.

### `PATCH /{id}/change-grade`
**Altera a nota** e conclui o processo de revisão.

**Use Case:** O docente concorda com o aluno e identifica um erro, corrigindo a nota.
**State Transition:** `REUNIAO_AGENDADA` -> `CONCLUIDO`

**Request Body:**
```json
{
  "nota_revista": 16.0,
  "observacoes_revisao": "Após análise conjunta, foi identificado um erro na soma da pontuação da questão 3. A nota foi corrigida."
}
```

**Response (200 OK):**
```json
{
  "id": 456,
  "estado": "CONCLUIDO",
  "nota_original": 14.5,
  "nota_revista": 16.0,
  "data_revisao": "2025-11-10T15:30:00Z",
  "updated_at": "2025-11-10T15:30:00Z"
}
```

### `PATCH /{id}/keep-grade`
**Mantém a nota original** e conclui o processo de revisão.

**Use Case:** O docente esclarece a dúvida do aluno, mas conclui que a correção original estava correta.
**State Transition:** `REUNIAO_AGENDADA` -> `CONCLUIDO`

**Request Body:**
```json
{
  "observacoes_revisao": "A correção foi reavaliada com o aluno e não foram encontrados erros. A nota original foi mantida."
}
```

**Response (200 OK):**
```json
{
  "id": 456,
  "estado": "CONCLUIDO",
  "nota_original": 14.5,
  "nota_revista": 14.5,
  "data_revisao": "2025-11-10T15:30:00Z",
  "updated_at": "2025-11-10T15:30:00Z"
}
```

**Evento Gerado (em ambos os casos):**
```json
{
  "event": "revisao.concluida",
  "data": {
    "revisao_id": 456,
    "classificacao_id": 123,
    "nota_antiga": 14.5,
    "nota_nova": 16.0, // ou 14.5 se a nota for mantida
    "aluno_id": 789
  }
}
```

---

## 4. Querying Reviews

### `GET /{id}`
Retrieves the details of a specific grade review request.

**Response:**
```json
{
  "id": 456,
  "grade_id": 123,
  "student_id": 789,
  "lecturer_id": 101,
  "status": "MEETING_SCHEDULED",
  "meeting_datetime": "2025-11-10T15:00:00Z",
  "meeting_location": "Lecturer's Office - Room 3.21",
  "original_grade": 14.5,
  "revised_grade": null,
  "review_remarks": null,
  "created_at": "2025-11-05T10:30:00Z",
  "updated_at": "2025-11-06T11:00:00Z"
}
```

### `GET /`
Lists all grade review requests, with filtering options.

**Query Parameters:**
- `student_id` (optional)
- `lecturer_id` (optional)
- `status` (optional): `PENDING_APPROVAL`, `MEETING_SCHEDULED`, `CONCLUDED`

---