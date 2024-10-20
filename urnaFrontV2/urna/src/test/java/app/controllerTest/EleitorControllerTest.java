package app.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import app.controller.EleitorController;
import app.entity.Eleitor;
import app.repository.EleitorRepository;
import app.service.EleitorService;

@SpringBootTest
public class EleitorControllerTest {
	@Autowired
	EleitorController eleitorController;
	
	@Autowired
	EleitorService eleitorService;
	
	@MockBean
	EleitorRepository eleitorRepository;
	
	//========================================================================================================
	/* Funções que proporcionam a resposta do Mockito emulando o Repository */
	//========================================================================================================

	void mockitoSave() {
		Eleitor MOCKeleitor = new Eleitor();
		MOCKeleitor.setId(1L);
		MOCKeleitor.setNome("Galadriel Lady");
		MOCKeleitor.setCpf("773.413.292-89");
		MOCKeleitor.setProfissao("Queen of Lothlórien");
		MOCKeleitor.setTelefoneCelular("(96) 96947-6135");
		MOCKeleitor.setTelefoneFixo("(35) 99705-5444");
		MOCKeleitor.setEmail("galadriel.lady@lothlorien.com");
		
		Mockito.when(eleitorRepository.save(Mockito.any(Eleitor.class))).thenReturn(MOCKeleitor);
	}
	

	void mockitoFindAll() {
		mockitoSave();
		
		Eleitor MOCKeleitorA = new Eleitor();
		MOCKeleitorA.setId(1L);
		MOCKeleitorA.setNome("Galadriel Lady");
		MOCKeleitorA.setCpf("773.413.292-89");
		MOCKeleitorA.setProfissao("Queen of Lothlórien");
		MOCKeleitorA.setTelefoneCelular("(96) 96947-6135");
		MOCKeleitorA.setTelefoneFixo("(35) 99705-5444");
		MOCKeleitorA.setEmail("galadriel.lady@lothlorien.com");
		
		eleitorController.cadastrar(MOCKeleitorA);
		
		Eleitor MOCKeleitorB = new Eleitor();
		MOCKeleitorB.setId(2L);
		MOCKeleitorB.setNome("Aragorn Son of Arathorn");
		MOCKeleitorB.setCpf("431.937.300-68");
		MOCKeleitorB.setProfissao("Ranger");
		MOCKeleitorB.setTelefoneCelular("(96) 99403-7425");
		MOCKeleitorB.setTelefoneFixo("(48) 98225-6660");
		MOCKeleitorB.setEmail("aragorn.son@middleearth.com");
		
		eleitorController.cadastrar(MOCKeleitorB);
		
		Eleitor MOCKeleitorC = new Eleitor();
		MOCKeleitorC.setId(3L);
		MOCKeleitorC.setNome("Legolas Greenleaf");
		//MOCKeleitorC.setCpf("826.104.840-38"); //Receberá o status de Pendente
		MOCKeleitorC.setProfissao("Elven Archer");
		MOCKeleitorC.setTelefoneCelular("(31) 91234-5678");
		MOCKeleitorC.setTelefoneFixo("(31) 3456-7890");
		MOCKeleitorC.setEmail("legolas.greenleaf@rivendell.com");
		
		
		eleitorController.cadastrar(MOCKeleitorC);
		
		List<Eleitor> MOCKlistaEleitores = new ArrayList<>();
		MOCKlistaEleitores.add(MOCKeleitorA);
		MOCKlistaEleitores.add(MOCKeleitorB);
		MOCKlistaEleitores.add(MOCKeleitorC);
		
		Mockito.when(eleitorRepository.findAll()).thenReturn(MOCKlistaEleitores);
	}
	
	void mockitoUpdate() {
		mockitoSave();
		Eleitor MOCKeleitorUpdate = new Eleitor();
		MOCKeleitorUpdate.setId(1L);
		MOCKeleitorUpdate.setNome("Galadriel Lady");
		MOCKeleitorUpdate.setCpf("773.413.292-89");
		MOCKeleitorUpdate.setProfissao("Queen of Lothlórien");
		MOCKeleitorUpdate.setTelefoneCelular("(96) 96947-6135");
		MOCKeleitorUpdate.setTelefoneFixo("(35) 99705-5444");
		MOCKeleitorUpdate.setEmail("galadriel.lady@lothlorien.com");
		
		eleitorController.cadastrar(MOCKeleitorUpdate);
		
		/*Dentro do update no Service, fazemos uso do setId e do save. Portanto, chamaremos esses casos aqui*/
		Mockito.when(eleitorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(MOCKeleitorUpdate));
		Mockito.when(eleitorRepository.save(Mockito.any(Eleitor.class))).thenReturn(MOCKeleitorUpdate);

	}
	
	void mockitoFindById() {
		mockitoSave();
		Eleitor MOCKeleitorFindById = new Eleitor();
		MOCKeleitorFindById.setId(1L);
		MOCKeleitorFindById.setNome("Galadriel Lady");
		MOCKeleitorFindById.setCpf("773.413.292-89");
		MOCKeleitorFindById.setProfissao("Queen of Lothlórien");
		MOCKeleitorFindById.setTelefoneCelular("(96) 96947-6135");
		MOCKeleitorFindById.setTelefoneFixo("(35) 99705-5444");
		MOCKeleitorFindById.setEmail("galadriel.lady@lothlorien.com");
		
		eleitorController.cadastrar(MOCKeleitorFindById);
		Mockito.when(eleitorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(MOCKeleitorFindById));
	}
	
	
	//-------------------------------------------------------------------------------------------------------- 
	//-------------------------------------------------------------------------------------------------------- 
	//-------------------------------------------------------------------------------------------------------- 

	@Test
	@DisplayName("Cadastro de novo Eleitor válido com informações completas [SUCESSO]")
	void cenario01() {
		mockitoSave();
		
		Eleitor eleitorTeste = new Eleitor();
		
		eleitorTeste.setId(1L);
		eleitorTeste.setNome("Galadriel Lady");
		eleitorTeste.setCpf("773.413.292-89");
		eleitorTeste.setProfissao("Queen of Lothlórien");
		eleitorTeste.setTelefoneCelular("(96) 96947-6135");
		eleitorTeste.setTelefoneFixo("(35) 99705-5444");
		eleitorTeste.setEmail("galadriel.lady@lothlorien.com");
		
		ResponseEntity<String> respostaSave = eleitorController.cadastrar(eleitorTeste);
		
		assertEquals(HttpStatus.OK, respostaSave.getStatusCode());
		assertEquals("Eleitor cadastrado com sucesso!", respostaSave.getBody()); 
		assertNotNull(respostaSave.getBody()); 
		assertEquals("Apto", eleitorTeste.getStatus().toString());//verifica se o eleitor está recebendo o status Apto
	}
	
	
	@Test
	@DisplayName("Cadastro de novo Eleitor válido sem CPF [SUCESSO]")
	void cenario02() {
		mockitoSave();
		
		Eleitor eleitorTeste = new Eleitor();
		
		eleitorTeste.setId(1L);
		eleitorTeste.setNome("Galadriel Lady");
		//eleitorTeste.setCpf("773.413.292-89");
		eleitorTeste.setProfissao("Queen of Lothlórien");
		eleitorTeste.setTelefoneCelular("(96) 96947-6135"); //faltando telefone celular
		eleitorTeste.setTelefoneFixo("(35) 99705-5444");
		eleitorTeste.setEmail("galadriel.lady@lothlorien.com");
		
		ResponseEntity<String> respostaSave = eleitorController.cadastrar(eleitorTeste);
		
		assertEquals(HttpStatus.OK, respostaSave.getStatusCode());
		assertEquals("Eleitor cadastrado com sucesso!", respostaSave.getBody()); 
		assertNotNull(respostaSave.getBody()); 
		assertEquals("Pendente", eleitorTeste.getStatus().toString()); //verifica se o eleitor está recebendo o status Apto
		
	}
	
	@Test
	@DisplayName("Cadastro de Eleitor nulo [ERRO]")
	void cenario03() {
		mockitoSave();
		Eleitor eleitorTeste = null;
		
		ResponseEntity<String> respostaSave = eleitorController.cadastrar(eleitorTeste);
		
		assertEquals(HttpStatus.BAD_REQUEST, respostaSave.getStatusCode());
		assertTrue(respostaSave.getBody().startsWith("Algo deu errado!"));
	}
	
	//TODO: descobrir pq está vindo nulo
	@Test
	@DisplayName("Encontrar todos os Eleitores [SUCESSO]")
	void cenario04() {
		mockitoFindAll();
		
		ResponseEntity<List<Eleitor>> respostaFindAll = eleitorController.findAll();
		
		//assertEquals("Eleitor cadastrado com sucesso!", respostaFindAll.getBody()); 
		assertEquals(HttpStatus.OK, respostaFindAll.getStatusCode());
		assertEquals(3, respostaFindAll.getBody().size());
	}
	
	
	@Test
	@DisplayName("Atualização de Eleitor existente [SUCESSO]")
	void cenario05() {
		mockitoUpdate();
		
		Eleitor eleitorAtualizado = new Eleitor();
		eleitorAtualizado.setId(2L);
		eleitorAtualizado.setNome("Aragorn Son of Arathorn");
		eleitorAtualizado.setCpf("431.937.300-68");
		eleitorAtualizado.setProfissao("Ranger");
		eleitorAtualizado.setTelefoneCelular("(96) 99403-7425");
		eleitorAtualizado.setTelefoneFixo("(48) 98225-6660");
		eleitorAtualizado.setEmail("aragorn.son@middleearth.com");
		
		long idExistente = 1L;
		
		ResponseEntity<String> respostaUpdate = eleitorController.atualizar(eleitorAtualizado, idExistente);
		assertEquals(HttpStatus.OK, respostaUpdate.getStatusCode());
		assertEquals("Eleitor atualizado com sucesso!", respostaUpdate.getBody());
	}
	
	@Test
	@DisplayName("Atualização nula de Eleitor [ERRO]")
	void cenario06() {
		mockitoUpdate();
		
		Eleitor eleitorAtualizado = null;
		
		long idExistente = 1L;
		
		ResponseEntity<String> respostaUpdate = eleitorController.atualizar(eleitorAtualizado, idExistente);
		assertEquals(HttpStatus.BAD_REQUEST, respostaUpdate.getStatusCode());
		assertTrue(respostaUpdate.getBody().startsWith("Algo deu errado!"));
	}
	
	@Test
	@DisplayName("Desativação de Eleitor existente [SUCESSO]")
	void cenario07() {
		mockitoFindById();
		
		long idExistente = 1L;
		
		ResponseEntity<String> respostaDesativar = eleitorController.desativar(idExistente);
		assertEquals(HttpStatus.OK, respostaDesativar.getStatusCode());
		assertEquals("Eleitor desativado com sucesso!", respostaDesativar.getBody());
	}
	
	@Test
	@DisplayName("Desativação de Eleitor que já votou [ERRO]")
	void cenario08() {
		mockitoFindById();
		
		long idExistente = 1L;
		
		eleitorService.atualizaVoto(idExistente);
		
		ResponseEntity<String> respostaDesativar = eleitorController.desativar(idExistente);
		
		assertEquals(HttpStatus.BAD_REQUEST, respostaDesativar.getStatusCode());
		assertTrue(respostaDesativar.getBody().startsWith("Algo deu errado!"));
	}
	
	@Test
	@DisplayName("Atualização de Eleitor Inativo [SUCESSO]")
	void cenario09() {
		mockitoUpdate();
		Eleitor eleitorAtualizado = new Eleitor();
		eleitorAtualizado.setId(5L);
		eleitorAtualizado.setNome("Galadriel Lady");
		eleitorAtualizado.setCpf("773.413.292-89");
		eleitorAtualizado.setProfissao("Queen of Lothlórien");
		eleitorAtualizado.setTelefoneCelular("(96) 96947-6135");
		eleitorAtualizado.setTelefoneFixo("(35) 99705-5444");
		eleitorAtualizado.setEmail("galadriel.lady@lothlorien.com");
		eleitorAtualizado.setStatus("Inativo"); 
		
		long idExistente = 1L;
		
		ResponseEntity<String> respostaUpdate = eleitorController.atualizar(eleitorAtualizado, idExistente);
		assertEquals(HttpStatus.OK, respostaUpdate.getStatusCode());
		assertEquals("Eleitor atualizado com sucesso!", respostaUpdate.getBody());
	}
	
	@Test
	@DisplayName("Atualização de Eleitor com informações incompletas [SUCESSO]")
	void cenario10() {
		mockitoUpdate();
		Eleitor eleitorAtualizado = new Eleitor();
		eleitorAtualizado.setId(5L);
		eleitorAtualizado.setNome("Galadriel Lady");
		eleitorAtualizado.setCpf(null);
		eleitorAtualizado.setProfissao("Queen of Lothlórien");
		eleitorAtualizado.setTelefoneCelular("(96) 96947-6135");
		eleitorAtualizado.setTelefoneFixo("(35) 99705-5444");
		eleitorAtualizado.setEmail("galadriel.lady@lothlorien.com");
		eleitorAtualizado.setStatus("Ativo"); 
		
		long idExistente = 1L;
		
		ResponseEntity<String> respostaUpdate = eleitorController.atualizar(eleitorAtualizado, idExistente);
		assertEquals(HttpStatus.OK, respostaUpdate.getStatusCode());
		assertEquals("Eleitor atualizado com sucesso!", respostaUpdate.getBody());
		assertEquals("Pendente", eleitorAtualizado.getStatus());
	}
}