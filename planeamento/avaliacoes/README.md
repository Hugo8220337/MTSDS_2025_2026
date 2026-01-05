# Porque se replicou:
Vantagens desta subdivisão:

1. **Melhor Escalabilidade**
- Classificações pode escalar durante períodos de avaliação
- Revisões pode escalar durante períodos pós-publicação
- Pautas pode escalar durante fechamento de semestre

2. **Isolamento de Responsabilidades**
- Configuração é mais crítica e precisa ser mais protegida
- Classificações precisa ser mais performática
- Pautas tem requisitos específicos de consistência
- Revisões tem workflow próprio

3. **Manutenção Simplificada**
- Cada serviço tem um propósito único
- Mudanças em um processo não afetam os outros
- Mais fácil de testar e fazer deploy

4. **Gestão de Dados Otimizada**
- Dados históricos podem ser geridos diferentemente
- Cache pode ser otimizado por uso
- Backup pode ser priorizado por importância

5. **Resiliência Melhorada**
- Falha em revisões não afeta lançamento de notas
- Problemas em pautas não impedem registros de classificações
- Cada serviço pode ter seu próprio SLA