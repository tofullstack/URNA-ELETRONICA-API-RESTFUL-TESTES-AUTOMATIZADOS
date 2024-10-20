package app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.Apuracao;
import app.entity.Candidato;
import app.entity.Eleitor;
import app.entity.Voto;
import app.repository.CandidatoRepository;
import app.repository.VotoRepository;

@Service
public class VotoService {
	@Autowired
	VotoRepository votoRepository;
	
	@Autowired
	EleitorService eleitorService;
	
	@Autowired
	CandidatoService candidatoService;
	
	@Autowired
	CandidatoRepository candidatoRepository;
	
	/*3.13*/
	public String votar(Voto voto, long id){ //id do eleitor
		Eleitor eleitor = eleitorService.findById(id);
		
		validarEleitor(eleitor);
		
		validarVoto(voto);
			
		voto.setData(LocalDate.now()); //coloca o horário de votação 
		voto.setHash(UUID.randomUUID().toString()); //estamos setando o valor do Hash durante o processo de salvamento do voto
		this.votoRepository.save(voto);
		eleitorService.atualizaVoto(id);
		
		return "Voto protocolado com sucesso! "+voto.getHash();
					
	}
	
	public void validarVoto(Voto voto) {
		Candidato prefeitoCompleto = candidatoRepository.findById(voto.getPrefeito().getId()).get();
		Candidato vereadorCompleto = candidatoRepository.findById(voto.getVereador().getId()).get();
		
		if((prefeitoCompleto.getFuncao()==1)&&(vereadorCompleto.getFuncao()==2)) {
			/*TODO: verificar se esse trecho está puxando o objeto completo*/
			
			
			validarCandidato(prefeitoCompleto);
			validarCandidato(vereadorCompleto);
		}
		
		else {
			throw new IllegalArgumentException("O voto fornecido contém informações incorretas e não será processado.");

		}
	}
	
	public void validarEleitor(Eleitor eleitor){
		if(eleitor.getStatus().equalsIgnoreCase("Pendente")) {
			eleitor.setStatus("Bloqueado");
			eleitorService.save(eleitor);
			throw new IllegalArgumentException("Este eleitor está impossibilitado de votar e recebeu o status Bloqueado.");
		}
		
		else if(eleitor.getStatus().equalsIgnoreCase("Bloqueado")) {
			throw new IllegalArgumentException("Este eleitor já foi bloqueado e continua impossibilitado de votar.");
		}
		
		else if(eleitor.getStatus().equalsIgnoreCase("Inativo")) {
			throw new IllegalArgumentException("Este eleitor está Inativo e impossibilitado de votar.");
		}
		
		else if(eleitor.getStatus().equalsIgnoreCase("Votou")) {
			throw new IllegalArgumentException("Este eleitor já votou e está impossibilitado de votar novamente.");
		}
		
	}
	
	public void validarCandidato(Candidato candidato) {
		String statusCandidato = candidato.getStatus();
		
		if(statusCandidato.equalsIgnoreCase("Inativo")) {
			throw new IllegalArgumentException("O candidato selecionado está inativo e não poderá receber um voto.");

		}
	}
	
	/*3.20 Invoque estas listagens no método realizarApuracao() do service de voto. Depois, percorra cada lista e faça o
	set inserindo o total de votos de cada candidato dos loops. Para isso, crie um método customizado
	no repository para retornar o total ( count(*) ) de votos pelo ID do candidato.*/
	
	/*public Apuracao realizarApuracao() {
		
	}*/
	
	//TODO: realizar apuração
	public Apuracao realizarApuracao() {
	    // método previamente declaro no repository que filtra função e status.
	    List<Candidato> prefeitosAtivos = candidatoRepository.findByFuncaoAndStatus(1, "Ativo");
	    
	    if(prefeitosAtivos.isEmpty()) {
	    	throw new IllegalArgumentException("A apuração não pôde ser completada pois não existem Prefeitos elegíveis no momento");
	    }
	    
	    List<Candidato> vereadoresAtivos = candidatoRepository.findByFuncaoAndStatus(2, "Ativo");
	    
	    if(vereadoresAtivos.isEmpty()) {
	    	throw new IllegalArgumentException("A apuração não pôde ser completada pois não existem Vereadores elegíveis no momento");
	    }

	    Apuracao apuracao = new Apuracao();
	    apuracao.setTotalVotos((int) votoRepository.count());
	    
	    int votosTotais = apuracao.getTotalVotos();
	    if (votosTotais == 0) {
	    	throw new IllegalArgumentException("A apuração não pôde ser completada pois não existem votos a serem contados");
	    }
	    
	    
	    apuracao.setPrefeitos(prefeitosAtivos);
	    apuracao.setVereadores(vereadoresAtivos);

	    // apura o voto de cada prefeito que estiver ATIVO utilizando o filtor feito no repository
	    for (Candidato prefeito : prefeitosAtivos) {
	       int votos = votoRepository.countByPrefeitoId(prefeito.getId());
	       prefeito.setVotosApurados(votos);//TODO: salvar?
	       
	    }

	    // apura o voto de cada vereador que estiver ATIVO
	    for (Candidato vereador : vereadoresAtivos) {
	        vereador.setVotosApurados(votoRepository.countByVereadorId(vereador.getId()));
	    }

	    // metodo baseado no documento que ordena os candidatos a prefeito pelos votos de maior para menor
	    prefeitosAtivos.sort((c1, c2) -> Integer.compare(c2.getVotosApurados(), c1.getVotosApurados()));

	    // metodo baseado no documento que ordena os candidatos a vereador pelos votos de maior para menor
	    vereadoresAtivos.sort((c1, c2) -> Integer.compare(c2.getVotosApurados(), c1.getVotosApurados()));

	    //retorno um objeto Apuração
	    return apuracao;
	}

	
}