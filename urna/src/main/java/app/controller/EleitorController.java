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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.entity.Eleitor;

import app.service.EleitorService;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/api/eleitor")
@CrossOrigin("*")
public class EleitorController {
	
	@Autowired 
	EleitorService eleitorService;
	
	@PostMapping("/cadastrar")
	public ResponseEntity<String> cadastrar(@Valid @RequestBody Eleitor eleitor){
		try {
			String mensagem = this.eleitorService.save(eleitor);
			return new ResponseEntity<>(mensagem,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Algo deu errado!" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	/*TODO: perguntar ao professor se o status pode ser visível no findAll*/
	@GetMapping("/findAll")
	public ResponseEntity<List<Eleitor>> findAll(){
		try {
			List<Eleitor> todosEleitores = this.eleitorService.findAll();
			return new ResponseEntity<>(todosEleitores, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/atualizar/{id}")
	public ResponseEntity<String> atualizar(@Valid @RequestBody Eleitor eleitor, @PathVariable Long id){
		try {
			String mensagem = this.eleitorService.update(eleitor, id);
			return new ResponseEntity<>(mensagem,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Algo deu errado!" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/desativar/{id}")
	public ResponseEntity<String> desativar(@PathVariable Long id){
		try {
			String mensagem = this.eleitorService.desativarEleitor(id);
			return new ResponseEntity<>(mensagem,HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Algo deu errado!" + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	 

    @GetMapping("/buscarCPF/{cpf}")
    public ResponseEntity<Eleitor> getEleitorByCPF(@PathVariable String cpf) {
        try {
            Eleitor eleitor = this.eleitorService.findByCpf(cpf);
            return new ResponseEntity<>(eleitor, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    

	
	
	
}