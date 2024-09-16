package app.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.entity.Candidato;
import app.repository.CandidatoRepository;

@Service
public class CandidatoService {
	
	@Autowired
	CandidatoRepository candidatoRepository;
	
	public String save(Candidato candidato) {
		candidato.setStatus("Ativo"); /*3.11 - Este status deverá ser atribuído sempre para um novo candidato cadastrado*/
		
		//TODO: validar FUNÇÃO, STATUS E NÚMERO antes do save - CONTINUAÇÃO: o status será sobrescrito, por isso não precisa ser validado. O número será validado por meio do @Column(unique = true)
		
		validaCandidato(candidato);
		
		this.candidatoRepository.save(candidato); 
		return "Candidato cadastrado com sucesso!";
	}
	
	public String desativarCandidato(Candidato candidato) {
		candidato.setStatus("Inativo");
		this.candidatoRepository.save(candidato);
		return "Candidato desativado com sucesso!";
	}
	
	
	public List<Candidato> findAll(){
		List<Candidato> candidatosAtivos = new ArrayList<>();
		
		List<Candidato> todosCandidatos = this.candidatoRepository.findAll();
		
		for(Candidato x: todosCandidatos) {
			if(x.getStatus().equalsIgnoreCase("Ativo")) {
				candidatosAtivos.add(x);
			}
		}
		
		return candidatosAtivos;
	}
	
	public List<Candidato> findAllPrefeito(){
		List<Candidato> candidatosAtivos = findAll();
		
		List<Candidato> prefeitos = new ArrayList<>();
		
		for (Candidato x: candidatosAtivos) {
			if(x.getFuncao()==1) {
				prefeitos.add(x);
			}
		}
		
		return prefeitos;
		
	}
	
	public List<Candidato> findAllVereador(){
		List<Candidato> candidatosAtivos = findAll();
		
		List<Candidato> vereadores = new ArrayList<>();
		
		for (Candidato x: candidatosAtivos) {
			if(x.getFuncao()==2) {
				vereadores.add(x);
			}
		}
		
		return vereadores;
	}
	
	private void validaCandidato(Candidato candidato) {
	    int funcaoCandidato = candidato.getFuncao();

	    if (funcaoCandidato != 1 && funcaoCandidato != 2) {
	        throw new IllegalArgumentException("O candidato precisa apresentar uma função válida para ser salvo.");
	    }

	}
	
	
    public List<Candidato> findCandidatosAtivosPorFuncao(int funcao) {
        //filtro apenas candidatos ativos // -> filtor no repository
        return candidatoRepository.findByFuncaoAndStatus(funcao, "ATIVO");
    }
	
}