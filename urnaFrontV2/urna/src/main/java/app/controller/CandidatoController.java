package app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entity.Candidato;
import app.service.CandidatoService;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/candidato")
//@CrossOrigin("*")
@CrossOrigin(origins = "http://localhost:4200")
public class CandidatoController {
	@Autowired
	CandidatoService candidatoService;
	
	@PostMapping("/cadastrar")
	public ResponseEntity<String> cadastrar(@Valid @RequestBody Candidato candidato){
		try {
			String mensagem = this.candidatoService.save(candidato);
			return new ResponseEntity<>(mensagem,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Algo deu errado! " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<List<Candidato>> findAll(){
		try {
			List<Candidato> todosCandidatos = this.candidatoService.findAll();
			return new ResponseEntity<>(todosCandidatos, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/desativar/{id}")
	public ResponseEntity<String> desativar(@Valid @RequestBody Candidato candidato, @PathVariable Long id){
		try {
			candidato.setId(id);
			String mensagem = this.candidatoService.desativarCandidato(candidato);
			return new ResponseEntity<>(mensagem,HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>("Algo deu errado! " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
    @GetMapping("/buscarPorNumero/{numero}")
    public ResponseEntity<Candidato> buscarCandidatoPorNumero(@PathVariable Integer numero) {
        Candidato candidato = candidatoService.buscarPorNumero(numero);
        if (candidato != null) {
            return new ResponseEntity<>(candidato, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
	
}