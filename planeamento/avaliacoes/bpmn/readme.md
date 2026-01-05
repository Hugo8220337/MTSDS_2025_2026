# Publicacao Pauta
1. **Fim da Época de Avaliação:** O processo é iniciado quando a época de avaliação (Normal, Recurso, etc.) termina.
2. **Criar Pauta:** É criada uma nova pauta no sistema com o estado `EM_PREPARACAO`.
3. **Gerar Resultados da Pauta:** O sistema calcula as notas finais e os estados de aprovação de cada aluno, preenchendo a pauta.
4. **Publicar Pauta Provisória:** A pauta é publicada com o estado PROVISORIA, ficando visível para os alunos.
5. **Período de Revisão:** O processo aguarda por um período de tempo definido (representado por um evento de timer) durante o qual os alunos podem solicitar a revisão de notas. (As revisões em si seriam tratadas num processo à parte).
6. **Recolher Assinaturas:** Após o período de revisão e eventuais acertos, a pauta é enviada para assinatura dos responsáveis (Regente, Coordenador, etc.).
7. **Publicar Pauta Definitiva:** Com as assinaturas recolhidas, a pauta é publicada com o estado `PUBLICADA`.
8. **Fechar Pauta:** Após um determinado período, a pauta é fechada administrativamente, passando ao estado `FECHADA`.
9. **Pauta Fechada e Arquivada:** O processo termina com a pauta finalizada e arquivada.