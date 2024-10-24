package app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.Eleitor;
import app.repository.EleitorRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class EleitorService {
	@Autowired
	EleitorRepository eleitorRepository;
	
	public String save(Eleitor eleitor) {
		if((eleitor.getCpf() == null) || (eleitor.getEmail() == null)) { //caso faltem informações do eleitor, ele terá o status pendente
			eleitor.setStatus("Pendente");
		}
		else {
			eleitor.setStatus("Apto"); /*TODO: PADRONIZAR ATIVO E APTO*/
		}
		
		this.eleitorRepository.save(eleitor);
		return "Eleitor cadastrado com sucesso!";
	}
	
	
	public String update(Eleitor eleitor, long id) {
		eleitor.setId(id);
		
		Eleitor eleitorOriginal = findById(id);
		
		//TODO: tratar exceptions futuramente
		
		/*Conserva os status Inativo e Bloqueado independentemente da informação fornecida*/
		if((eleitorOriginal.getStatus().equalsIgnoreCase("Inativo"))||(eleitorOriginal.getStatus().equalsIgnoreCase("Bloqueado"))||(eleitorOriginal.getStatus().equalsIgnoreCase("Votou"))){
			this.eleitorRepository.save(eleitor);
			return "Eleitor atualizado com sucesso!";
		}
		
		else {
			if((eleitor.getCpf() == null) || (eleitor.getEmail() == null)) { //caso o Eleitor receba uma atualização com informações pendentes
				eleitor.setStatus("Pendente");
				this.eleitorRepository.save(eleitor);
			}
			else { //caso o cadastro seja completo
				eleitor.setStatus("Apto");
				this.eleitorRepository.save(eleitor);
			}
			
			this.eleitorRepository.save(eleitor);
			return "Eleitor atualizado com sucesso!";
			
		}
		
	}
	
	//Uma versão sem verificações para atualizar o eleitor após a operação de voto
	public void atualizaVoto(long id) {
		Optional<Eleitor> optionalEleitor = this.eleitorRepository.findById(id);
		Eleitor eleitor = optionalEleitor.get();
		
		eleitor.setStatus("Votou");
		this.eleitorRepository.save(eleitor);
	}
	
	public List<Eleitor> findAll(){
		List<Eleitor> eleitoresAtivos = new ArrayList<>();
		
		List<Eleitor> todosEleitores = this.eleitorRepository.findAll();
		
		for(Eleitor x: todosEleitores) {
			if(!x.getStatus().equalsIgnoreCase("Inativo")) { //apenas caso o eleitor seja outra coisa além de Inativo (Pendente, Bloqueado, Votou, Apto)
				eleitoresAtivos.add(x);
			}
		}
		
		return eleitoresAtivos;
	}
	
	public Eleitor findById(long id) {
		Optional<Eleitor> optional = this.eleitorRepository.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}else {
			return null;
		}
	}
	
	/*public String desativarEleitor(Eleitor eleitor) {
		if(eleitor.getStatus().equalsIgnoreCase("Votou")) { //TODO: verificar com o prof. se esse trecho deverá ser mudado para Controller
			throw new IllegalArgumentException("Não foi possível desativar o eleitor, pois sua operação de voto já foi concluída.");
		}
		
		else {
			eleitor.setStatus("Inativo");
			eleitorRepository.save(eleitor);
			return "Eleitor desativado com sucesso!";
		}
			
	}*/
	
	public String desativarEleitor(Long id) {
		Optional<Eleitor> optional = this.eleitorRepository.findById(id);
		Eleitor eleitor = optional.get();
		
		if(optional.isPresent()) {
			if(eleitor.getStatus().equalsIgnoreCase("Votou")) { //TODO: verificar com o prof. se esse trecho deverá ser mudado para Controller
				throw new IllegalArgumentException("Não foi possível desativar o eleitor, pois sua operação de voto já foi concluída.");
			}
			
			else {
				eleitor.setStatus("Inativo");
				eleitorRepository.save(eleitor);
				return "Eleitor desativado com sucesso!";
			}
		}
		
		else {
			return null;
		}
			
	}
	
	/*findByCPF urna-front*/
	/*
	public Eleitor findByCpf(String cpf) {
	    Optional<Eleitor> optional = eleitorRepository.findByCpf(cpf);
	    if (optional.isPresent()) {
	        return optional.get();
	    } else {
	        return null;
	    }
	}
	*/
	
	public Eleitor findByCpf(String cpf) {
	    Eleitor eleitor = eleitorRepository.findByCpf(cpf);
	    if (eleitor == null) {
	        throw new EntityNotFoundException("Eleitor não encontrado com o CPF: " + cpf);
	    }
	    return eleitor;
	}

}