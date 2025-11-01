# Processamento_Candidaturas_DGES.bpmn
1. **Início com Ficheiro DGES:** O processo é acionado pela receção do ficheiro oficial da Direção-Geral do Ensino Superior (DGES) com a lista de alunos colocados.

2. **Importar Colocações:** A primeira tarefa é um Service Task que lê o ficheiro e cria um registo para cada aluno na tabela colocacoes_dges com o estado `IMPORTADO`.

3. **Processar Cada Aluno (Sub-processo Multi-instância):** Para tratar cada aluno de forma independente, utilizei um sub-processo configurado para múltiplas instâncias. Este sub-processo irá executar uma vez para cada aluno na lista importada.

- **Mapear Curso:** Tenta encontrar a correspondência entre o código do curso da DGES e o código do curso interno da instituição (usando a tabela mapping_cursos).

- **Decisão (Gateway):**
    - **Sim (Curso Mapeado):** Se o mapeamento for bem-sucedido, avança para a criação do processo de matrícula.
    - **Não (Curso não Mapeado):** Se não for encontrada correspondência, a colocação é marcada com o estado ERRO.
- **Criar Processo de Matrícula:** Se o curso foi mapeado, um Service Task cria uma entrada na tabela processos_matricula, dando início ao processo de matrícula do aluno.
- **Marcar Erro e Notificar:** Em caso de falha no mapeamento, o estado da colocação é atualizado para ERRO e uma notificação é enviada a um administrador para que o problema seja resolvido manualmente.

4. **Fim do Processo:** O processo principal termina quando todos os alunos da lista foram processados (seja com sucesso ou com erro).


# Processamento_Matricula.bpmn
1. **Início do Processo (Aluno Colocado):** O processo é iniciado por uma mensagem (evento) que indica que um aluno foi colocado. Isto pode vir tanto do processo de seriação local como do processo de importação da DGES.

2. **Criar Processo de Matrícula:** O sistema cria um registo na tabela processos_matricula com o estado `CRIADA` e notifica o aluno para dar início aos procedimentos.

3. **Ações em Paralelo (Gateway Paralelo):** O processo divide-se em dois fluxos que podem ocorrer em simultâneo: a submissão de documentos e o pagamento.
- **Sub-processo de Documentos:**
    - O aluno submete os documentos necessários.
    - Os serviços académicos validam os documentos.
    - Se os documentos forem inválidos, o aluno é notificado para os submeter novamente.
    - O sub-processo termina quando todos os documentos são considerados `VALIDO`.
- **Sub-processo de Pagamento:**
    - O sistema gera uma referência de pagamento.
    - O processo aguarda por uma mensagem de confirmação do sistema de pagamentos.
    - O sub-processo termina quando o pagamento é confirmado (PAGO).

4. **Sincronização (Gateway Paralelo):** O processo principal só continua depois de ambos os sub-processos (documentos e pagamento) estarem concluídos.
Finalizar Matrícula: Um Service Task atualiza o estado do processo para `MATRICULADO`.

5. **Notificar Aluno:** O aluno recebe uma notificação a confirmar que a sua matrícula foi concluída com sucesso.

6. **Fim do Processo:** O processo termina com o aluno oficialmente matriculado.