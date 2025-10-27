Para desenhar um backend para um sistema de candidaturas como o da ESTG/P.Porto, primeiro é necessário distinguir os dois sistemas que o utilizador (candidato) vê como um só:

1. O Sistema Centralizado (DGES): O Concurso Nacional de Acesso (CNA) para licenciaturas. Este é um sistema massivo, gerido pelo governo (DGES), que recolhe todas as vagas de todas as universidades/politécnicos públicos, recolhe todas as candidaturas dos alunos do secundário, e corre um algoritmo de colocação nacional. O P.Porto (e a ESTG) recebe os resultados (a "lista de colocados") deste sistema.

2. O Sistema Local (P.PORTO): É aqui que o P.Porto tem controlo. Este sistema gere:
    - Mestrados
    - Pós-Graduações
    - Maiores de 23 Anos (M23)
    - CTeSP
    - Mudanças de Curso, Reingressos, etc


## Como funciona as colocações pela DGES
É mais fácil explcar o passo a passo:

1. **Configuração:**
- Crie um registo na tabela `Concurso` para o Concurso Nacional de Acesso. Ex: `(ano_letivo: '2025/2026', tipo: 'CNA_LICENCIATURA', name: 'Concurso Nacional de Acesso - Licenciaturas')`.
- Crie um registo na tabela `Fase` associado a este concurso.

2. **Processo de Importação:**
- Após a DGES libertar os resultados, um administrador importa um ficheiro (ex: CSV) com os alunos colocados.
- Para cada aluno no ficheiro, o sistema:
    - Cria ou atualiza o registo na tabela `Utilizador` (usando o NIF como chave).
    - Cria um registo na tabela `Candidatura` com `origem: 'DGES'` e `estado: 'COLOCADO'`.
    - Cria um registo na tabela `OpcaoCurso` para o curso onde o aluno foi colocado, com `estado_opcao: 'COLOCADO'` e `ordem_preferencia: 1`.

3. **Matrícula:**
- A partir daqui, o processo converge. O aluno importado da DGES acede ao seu portal, vê que está "Colocado" e procede para a matrícula, seguindo o mesmo fluxo (`Processo 4`) que os candidatos locais.

---

# Novo plano

### 1 - Microserviço candidaturas-dges

Responsável apenas pela integração com o DGES:

- Importa ficheiros CSV/XML oficiais

- Faz o mapping de cursos DGES → internos

- Cria “candidaturas externas” com estado “Colocado DGES”

- Emite evento: AlunoColocadoDGES


### 2 - Microserviço candidaturas-locais

Responsável pelos concursos geridos internamente:

- M23, CTeSP, Mestrados, Pós-Graduações, Reingressos…

- Fluxos completos: submissão, análise, resultados

- Gestão de concursos, fases e candidaturas

- Exporta eventos: CandidaturaAprovada, CandidatoSelecionado

### 3 - Microserviço matriculas

Responsável pela matrícula em si (comum a ambos os fluxos DGES e locais):

- Recebe eventos: AlunoColocadoDGES, CandidaturaAprovada

- Cria processos de matrícula

- Integra com serviço de utilizadores e pagamentos

- Gere documentos e estados (matriculado, pendente, etc.)
