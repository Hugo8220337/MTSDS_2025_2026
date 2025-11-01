Escopo
Escopo do Projeto
O presente projeto tem como objetivo o desenvolvimento de um sistema de gestão e lançamento de notas destinado a um grupo de universidades pertencentes à mesma instituição. O sistema pretende centralizar e padronizar o processo de avaliação académica, garantindo simultaneamente a autonomia de cada universidade na gestão dos seus cursos, docentes e alunos.
O sistema permitirá que docentes registem e validem notas, que coordenadores de curso aprovem pautas e que alunos consultem as suas avaliações e histórico académico. A administração central do grupo terá a capacidade de configurar políticas académicas comuns, supervisionar universidades e monitorizar indicadores de desempenho globais.

Escopo Funcional do Sistema
1. Gestão de Escolas
Permite o registo e administração das universidades pertencentes ao grupo, incluindo a definição de políticas de avaliação, períodos académicos, e configurações institucionais específicas de cada entidade.
2. Gestão Académica
Abrange o controlo de cursos, unidades curriculares, turmas e alunos, assegurando o correto enquadramento académico de cada universidade. Inclui o processo de inscrição de alunos e associação de docentes às unidades curriculares.
Inclui o processo de matrícula, que permite associar alunos a cursos e unidades curriculares de acordo com o calendário académico, garantindo a sua participação nas avaliações correspondentes.  Este módulo também assegura a associação de docentes às unidades curriculares e turmas.
3. Gestão de Colaboradores
Responsável pela gestão de docentes e outros tipos de funcionários, permitindo registar novas entradas, atualizar dados de colaboradores, e gerir cessação de funções. Este módulo fornece informações sobre a ligação entre docentes e universidades e integra-se com a gestão académica para permitir o lançamento de notas e criação de avaliações.
4. Gestão de Avaliações e Notas
Permite a criação de avaliações (testes, trabalhos, exames, projetos), o lançamento e atualização de notas pelos docentes, a validação e publicação de pautas, e o processo de revisão de notas solicitado pelos alunos.
5. Autenticação e Autorização
Assegura o controlo de acesso ao sistema com base em papéis e permissões. Garante que cada utilizador apenas acede às funcionalidades adequadas ao seu perfil (administração central, gestor institucional, docente, aluno, entre outros).

Fora do escopo do projeto
Gestão financeira e administrativa (propinas, inscrições e pagamentos).
Gestão de recursos humanos de caráter contratual (folhas de pagamento, contratos e benefícios).
Funcionalidades de e-learning ou gestão de conteúdos pedagógicos (como aulas, materiais ou avaliações online).

Abordagem Técnica
O sistema será concebido segundo o paradigma orientado a microserviços, de forma a garantir escalabilidade, independência tecnológica e facilidade de manutenção. Cada microserviço corresponderá a um contexto limitado (Bounded Context), identificado através da metodologia Domain-Driven Design (DDD).