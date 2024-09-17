# URNA-VIRTUAL-BACKEND-COM-TESTES-AUTOMATIZADOS
Sistema que simula uma urna virtual, desenvolvido com padrões de API Restful e arquitetura MVC, contando com testes automatizados unitários e de integração, aplicados através da biblioteca Jacoco e do framework JUnit.

<!-- ## Regras de Negócio e Requisitos -->
<h1>Entidades, Regras de Negócio e Requisitos Associados</h1>
<h2>Eleitor</h2>
  <ul>
      <li>Os atributos nome completo, profissão e telefone celular precisam ser válidos e são obrigatórios para criação de uma entidade Eleitor.</li>
      <li>Os atributos CPF, telefone fixo e e-mail precisam ser válidos mas não são obrigatórios para a criação de uma entidade Eleitor.</li>
      <li>Possíveis status de um Eleitor incluem: apto, inativo, bloqueado, pendente, votou.</li>
  </ul>

<h2>Candidato</h2>
  <ul>
      <li>Os atributos nome completo, profissão, telefone celular são obrigatórios para criação de uma entidade Candidato.</li>
      <li>O campo função identifica a função governamental que o Candidato desempenha, onde o número 1 indica Prefeito e o número 2 indica Vereador.</li>
      <li>O atributo votos apurados é marcado pela annotation @Transient e é inicializado como 0. Ele será incrementado apenas durante a operação de contagem dos votos dentro da Apuração dos Votos.
      <li>Possíveis status de um Candidato incluem: ativo e inativo.</li>
      <li>Um candidato nunca pode ser deletado, apenas receber o status "Inativo".</li>
      <li>O método findAll() lista apenas os Candidatos Ativos</li>
  </ul>

<h2>Voto</h2>
    <ul>
        <li>A data e hora de uma votação não serão enviados na requisição e são determinados automaticamente com base no horário que consta no sistema operacional que roda o programa por meio da função LocalDate.now().</li>
        <li>Dois candidatos são obrigatoriamente mencionados dentro de uma operação de voto, um sendo Prefeito e outro sendo Vereador. Ambos são campos obrigatórios.</li>
        <li>O hash será um campo gerado pelo sistema durante a ação de protocolar o voto com sucesso, não sendo enviado na requisição.</li>
        <li>Somente Eleitores com o status "Apto" podem concluir uma operação de Voto.</li>
        <li>Durante a operação de Voto, o eleitor nunca é associado ou persistido em associação com as informações de seu voto. Após a conclusão, ele recebe o status "Votou"</li>
        <li>Votos brancos ou nulos não são processados pelo sistema.</li>
        <li>O método votar valida se Prefeito e Vereador estão associados a suas respectivas funções dentro do voto.</li>
    </ul>

<h2>Apuração</h2>
    <ul>
        <li>A apuração dos vencedores e dos votos totais de uma eleição nunca é persistida no banco. Deste modo, ela será apenas representativa do resultado imediato de uma operação de Eleição e não será armazenada.</li>
        <li>O cálculo da apuração utiliza filtros presentes no repositório associado à entidade Candidato (CandidatoRepository).</li>
        <li>O método de realização de apuração é um método da Controller do Voto.</li>
        <li>O total de votos será incrementado durante a operação de apuração.</li>
        <li>A de Prefeitos e de Vereadores é um campo da classe Apuração e contém todos os Candidatos elegíveis dentro da operação.</li>
    </ul>



<!--
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
-->
