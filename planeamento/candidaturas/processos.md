# Processos Principais & Lógica de Negócios (A API)

Estes são os "verbos" do sistema, implementados como endpoints de API (ex: REST) e serviços de background.

## Processo 1: Submissão da Candidatura (Frontend -> Backend)

1. **Auth:** `OST /api/v1/auth/register` e `POST /api/v1/auth/login` (gera um token JWT).

2. **Escolha:** O utilizador vê os concursos abertos: `GET /api/v1/concursos?abertos=true`.

3. **Início:** O utilizador clica em "Candidatar-se". O sistema cria um registo: `POST /api/v1/candidaturas` (body: `{ "fase_id": X }`). Isto cria uma Candidatura com estado 'RASCUNHO'.

4. **Preenchimento:**
    - O frontend pede os documentos necessários: `GET /api/v1/fases/{id}/documentos_requeridos`.

    - O utilizador faz upload: `POST /api/v1/candidaturas/{id}/documentos` (isto cria `DocumentoSubmetido` com estado 'PENDENTE' e guarda o ficheiro no S3/Blob Storage).

    - O utilizador preenche dados pessoais: `PUT /api/v1/utilizadores/me`.

    - O utilizador escolhe opções: `POST /api/v1/candidaturas/{id}/opcoes` (body: `[{ "curso_id": Y, "ordem": 1 }, ...]`).

5. **Submissão Final:** O utilizador clica "Submeter". POST /api/v1/candidaturas/{id}/submit.

    - Lógica de Negócios: O backend verifica se `data_fim_candidatura` já passou. Verifica se todos os `DocumentoRequerido` obrigatórios têm um `DocumentoSubmetido` associado.

    - Se OK, muda `Candidatura.estado` para 'SUBMETIDA' e envia um email de confirmação.

## Processo 2: Validação (Back-office Administrativo)

1. **Queue de Trabalho:** Um admin (Serviços Académicos) vê as candidaturas submetidas: `GET /api/v1/admin/candidaturas?estado=SUBMETIDA`.

2. **Análise Documental:** O admin vê cada candidatura.

    - GET /api/v1/admin/candidaturas/{id}/documentos.

    - Para cada documento, o admin clica "Validar" ou "Rejeitar".

    - `PUT /api/v1/admin/documentos_submetidos/{id_doc}` (body: `{ "estado_validacao": "VALIDADO" }`).

3. **Finalização da Análise:**

    - Se todos os docs obrigatórios estão 'VALIDADO', o admin muda o estado da candidatura: `PUT /api/v1/admin/candidaturas/{id}` (`body: { "estado": "VALIDADA" }`).

    - Se algum doc for 'REJEITADO', o admin muda o estado para 'INVALIDADA' (ou 'PENDENTE_CORRECAO') e escreve uma observação. O sistema notifica o aluno.

## Processo 3: Seriação & Colocação (O "Algoritmo")

Esta é a parte mais complexa. Geralmente é um background job (tarefa agendada) ou um script corrido por um admin.

1. **Gatilho:** Admin acede a `GET /api/v1/admin/fases/{id}/dashboard` e clica "Iniciar Seriação".

2. **Atribuição de Notas (Painel do Diretor de Curso):**

    - O Diretor de Curso (ex: `admin_curso`) vê as candidaturas 'VALIDADA': `GET /api/v1/admin/cursos/{id_curso}/candidaturas?estado=VALIDADA`.

    - Para cada candidato, ele vê os critérios (`CriterioSeriacao`).

    - Ele insere as notas: `POST /api/v1/admin/candidaturas/{id}/notas` (`body: [{ "criterio_id": A, "valor_nota": 18 }, { "criterio_id": B, "valor_nota": 16 }]`).

3. **Cálculo da Nota Final:**
    - Após todas as notas serem inseridas, o sistema (ou o admin) dispara o cálculo.

    - O sistema faz um loop por todas as candidaturas 'VALIDADA' daquela fase.

    - Para cada uma, calcula a `nota_final_seriacao` (média ponderada das `NotaComponente` com os `CriterioSeriacao.peso_percentual`).

    - Guarda o resultado em `Candidatura.nota_final_seriacao`.

4. **Algoritmo de Colocação (Matching):**
    - O sistema obtém todas as `OpcaoCurso` da `Fase`, ordenadas por `Candidatura.nota_final_seriacao` (descendente) e depois por `ordem_preferencia` (ascendente).

    - Obtém as `vagas` da `Fase`.

    - Itera pela lista de opções:

        - Candidato A, Opção 1 (Nota 19.5). Vagas > 0? Sim.

            - Coloca o Candidato A: `OpcaoCurso.estado_opcao = 'COLOCADO'`.

            - Define Candidatura.estado = 'COLOCADO'.

            - Decrementa `vagas`.

            - Importante: Anula as opções inferiores deste candidato (`ordem_preferencia` > 1) como EXCLUIDO_PREF_SUPERIOR.

        - Candidato B, Opção 1 (Nota 19.0). Vagas > 0? Sim.

            - ... (processo repete-se) ...

        - Candidato Z, Opção 1 (Nota 15.0). Vagas = 0? Sim.

            - Define `OpcaoCurso.estado_opcao = 'NAO_COLOCADO'`.

            - O algoritmo passa a considerar a Opção 2 deste candidato na próxima iteração (se houver).

        - No final, todas as opções não colocadas são marcadas como `NAO_COLOCADO`.

## Processo 4: Publicação e Matrícula

1. **Publicação:** O admin clica "Publicar Resultados". O sistema envia emails a todos os candidatos com o seu estado final ('COLOCADO', 'NAO_COLOCADO').

2. **Matrícula:**

    - O candidato 'COLOCADO' vê um novo botão: "Realizar Matrícula".

    - `POST /api/v1/candidaturas/{id}/matricular`.

    - Lógica: O sistema verifica se está dentro do prazo (`data_inicio_matricula` e `data_fim_matricula`).

    - Muda o `Candidatura.estado` para 'MATRICULADO'.

    - **Integração:** Neste ponto, o backend deve chamar outro sistema: o **Sistema de Gestão Académica**. Ele cria o "registo de aluno" oficial, gera o número de aluno, e cria a "conta corrente" para o pagamento de propinas.