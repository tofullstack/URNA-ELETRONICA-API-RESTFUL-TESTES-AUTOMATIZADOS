# URNA-VIRTUAL-BACKEND-COM-TESTES-AUTOMATIZADOS
Sistema que simula uma urna eletrônica virtual em API Restful com testes automatizados únicos e de integração.

## Regras de Negócio e Requisitos
## ELEITOR
- Nome, profissão, telefone celular são campos obrigatórios.
- CPF, telefone fixo e e-mail são campos válidos mas não obrigatórios.
- Os status possíveis que serão processados pelo sistema são: APTO, INATIVO, BLOQUEADO, PENDENTE e VOTOU.
## CANDIDATO
- Nome, CPF, número do candidato e função são campos obrigatórios.
- O campo funçao terá identificação númerica sendo 1 para prefeito e 2 para vereador.
- Votos apurados são processados pelo sistema inciados em 0 e marcados pela annotation @Transient.
- Os status possíveis processados pelo sistema são ATIVO e INATIVO e não devem ser enviados na requisição.
- Um candidato não deve ser nunca deletado, apenas desativado.
- O método findAll lista apenas candidatos Ativos.
## VOTO
- Data e Hora da votação é um campo processado pelo sistema,não enviado na requisição e setado quando um voto é protocolocado com sucesso.
- Vereador e Prefeito são campos obrigatórios.
- Hash é um campo gerado pelo sistema assim que um voto é protocolocado com sucesso e não enviado na requisição.
- Somente eleitores APTOS podem votar.
- O eleitor nunca é identificado ou salvo no banco.
- Votos brancos e nulos não são processados pelo sistema.
- A protocolação de um voto é feita pelos parâmetros Voto e ID do eleitor.
- O método votar valida se Prefeito e  Vereador estão com suas respectivas funções.
## APURAÇÃO
- A apuração nunca é persistida no banco.
- O cálculo da apuração utiliza filtros do repositório de Candidatos.
- realizarApuracao é um método de Voto.
- Apuração apenas representa um objeto do resultado das eleições.
- Total de votos é um campo incrementável.
- Lista de prefeitos e vereadores é um campo da classe Apuracao.

