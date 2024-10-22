package app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entity.Apuracao;
import app.entity.Voto;
import app.service.VotoService;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/voto")
@CrossOrigin("*")
public class VotoController {

	@Autowired
	VotoService votoService;
	
	@PostMapping("/votar/{idEleitor}")
	public ResponseEntity<String> votar(@Valid @RequestBody Voto voto, @PathVariable Long idEleitor){
		try {
			String mensagem = this.votoService.votar(voto, idEleitor);
			return new ResponseEntity<>(mensagem,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Algo deu errado! " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/realizarApuracao")
	public ResponseEntity<?> realizarApuracao(){
		try {
			Apuracao apuracao = this.votoService.realizarApuracao();
			return new ResponseEntity<>(apuracao,HttpStatus.OK);
		} catch (Exception e) {
			//e.printStackTrace();
			return new ResponseEntity<>("Algo deu errado! " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
}