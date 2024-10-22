package app.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import app.controller.CandidatoController;
import app.entity.Candidato;
import app.repository.CandidatoRepository;

@SpringBootTest
public class CandidatoControllerTest {

	@Autowired
	CandidatoController candidatoController;
	
	@MockBean
	CandidatoRepository candidatoRepository;
	
	//global
	
	Candidato candidato = new Candidato();
	Candidato candidato2  = new Candidato();
	
	//preparando o setup
	
	@BeforeEach
	void setup() {
		
		//preparando candidato
		
		candidato.setId(1L);
		candidato.setNome("Funny Valentine");
		candidato.setCpf("254.728.080-90");
		candidato.setNumero(23); //numero unico
		candidato.setFuncao(1); //prefeito
		candidato.setStatus("Ativo"); //ativo ou inativo
		candidato.setVotosApurados(0);//inicia em zero
		
		candidato2.setId(2L);
		candidato2.setNome("Diego Brando");
		candidato2.setCpf("829.444.749-87");
		candidato2.setNumero(23); //numero unico
		candidato2.setFuncao(2); //vereador
		candidato2.setStatus("Ativo"); //ativo ou inativo
		candidato2.setVotosApurados(0);//inicia em zero
		
		//mock
		//Mockito.when(candidatoRepository.save(Mockito.any(Candidato.class))).thenReturn(candidato,candidato2);
		
		//mesmo que a linha de cima faz
		Mockito.when(candidatoRepository.save(Mockito.any(Candidato.class)))
        .thenReturn(candidato)
        .thenReturn(candidato2);
	}
	
	
	// testes
	
	// ##SAVE -> OK # BAD # EXCEPTION =============================
	
	// ## OK - candidato com todos os dados válidos.
	@Test
	@DisplayName("CANDIDATO # RESPONSE: 'Sucesso!' # STATUS: OK")
	void cenarioCandidatoSalvar() {
		
		ResponseEntity<String> retorno = candidatoController.cadastrar(candidato);
		
		assertEquals(HttpStatus.OK, retorno.getStatusCode());
		assertEquals("Candidato cadastrado com sucesso!",retorno.getBody());
	}
	
	// ## EXCEPTION
	@Test
	@DisplayName("CANDIDATO # RESPONSE: 'Nome e CPF inválidos' # EXCEPTION")
	void cenarioCandidatoSalvarException() {
	    // candidato invalido
	    Candidato candidatoInvalido = new Candidato();
	    candidatoInvalido.setNome("Invalido"); // nome n tem 2 palavras
	    candidatoInvalido.setCpf("123.456.789-00"); // cpf nao valido
	    candidatoInvalido.setNumero(25); // num invalido
	    candidatoInvalido.setFuncao(1); // prefeito

	    
	    assertThrows(Exception.class,()->{
		    ResponseEntity<String> retorno = candidatoController.cadastrar(candidatoInvalido);

	    });
	}

	// ## BAD_REQUEST: envio um candidato invalido;
	
	@Test
	@DisplayName("CANDIDATO # STATUS: BAD_REQUEST")
	void cenarioCandidatoSalvarBadRequest() {
	    // candidato com dados inválidos
	    Candidato candidatoInvalido = null;

	    ResponseEntity<String> retorno = candidatoController.cadastrar(candidatoInvalido);

		assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
		assertTrue(retorno.getBody().startsWith("Algo deu errado!"));
	}
	
	@Test
	@DisplayName("CANDIDATO # VALIDAÇÃO NOME")
	void cenarioCandidatoNomeInvalido() {
	    Candidato candidatoInvalido = new Candidato();
	    candidatoInvalido.setNome("SemEspaço"); // nome inválido, precisa de pelo menos duas palavras
	    candidatoInvalido.setCpf("697.331.800-25");
	    candidatoInvalido.setNumero(23);
	    candidatoInvalido.setFuncao(1);
	    
		assertThrows(Exception.class, ()->{
			 ResponseEntity<String> retorno = candidatoController.cadastrar(candidatoInvalido);
		});
	}

	// ##FINDALL -> OK # BAD # EXCEPTION =======================
	
	// ## OK 
	@Test
	@DisplayName("CANDIATO # STATUS: 'OK'")
	void cenarioCandidatoBuscarTodos() {
		
		List<Candidato> lista = new ArrayList<>();
		//adicionando 3 novos candidatos
		lista.add(new Candidato(2L,"Prosciutto","113.919.999-40",13,1,"Ativo",0));
		lista.add(new Candidato(3L,"Ghiaccio","829-444-749-87",14,2,"Ativo",0));
		lista.add(new Candidato(4L,"Risotto Nero","375.206.120-07",20,1,"Ativo",0));
		
		Mockito.when(candidatoRepository.findAll()).thenReturn(lista);
		ResponseEntity<List<Candidato>> retorno = candidatoController.findAll();
		
	    assertEquals(HttpStatus.OK, retorno.getStatusCode()); //retorno ok
	    assertNotNull(retorno.getBody()); //nao nulo
	    assertEquals(3, retorno.getBody().size()); //tamanho deve ser 3
		
	}
	
	// ## OK
	//TODO: manter??????????
	@Test
	@DisplayName("CANDIDATO # STATUS: OK  RESPONSE: []")
	void cenarioCandidatoBuscarTodosListaVazia() {
		//versão com nenhum candidato, retorna um []
		
		ResponseEntity<List<Candidato>>retorno = candidatoController.findAll();
		
		assertTrue(retorno.getBody().isEmpty());//verifico se ta vazio
	}
	
	// ##INATIVAR CANDIDATO -> OK # BAD # EXCEPTION =============================
	
	// ## OK
	@Test
	@DisplayName("CANDIDATO # STATUS OK")
	void cenarioCandidatoDesativar() {
		
		ResponseEntity<String> retorno = candidatoController.desativar(candidato,1L);
		assertEquals(HttpStatus.OK,retorno.getStatusCode());
		
		assertFalse(retorno.getBody().contains("Tente novamente!"));
		
	}
	
	// ## BAD_REQUEST
	@Test
	@DisplayName("CANDIDATO # STATUS BAD_REQUEST")
	void cenarioCandidatoDesativarErro() {
		
		ResponseEntity<String> retorno = candidatoController.desativar(null,99L);
		assertEquals(HttpStatus.BAD_REQUEST,retorno.getStatusCode());
		
		assertFalse(retorno.getBody().contains("Tente novamente!"));
		
	}
	
	// ## EXCEPTION
	@Test
	@DisplayName("CANDIDATO # RESPONSE: [] STATUS: EXCEPTION")
	void cenarioCandidatoDesativarException() {
	    Candidato candidatoInvalido = new Candidato();
	    candidatoInvalido.setNome("SemEspaço"); // nome inválido deve dar erro nas annonations
	    candidatoInvalido.setCpf("697.331.800-25");
	    candidatoInvalido.setNumero(23);
	    candidatoInvalido.setFuncao(1);
	    
		assertThrows(Exception.class, ()->{
			 ResponseEntity<String> retorno = candidatoController.desativar(candidatoInvalido, null);
		}); //deve cair no catch
	}
	
	// ###################################### TESTANDO OS FILTROS E FUNÇOES INTERNAS ###################################################################
	
    // ## buscar todos os candidatos ativos
    @Test
    @DisplayName("Buscar todos os candidatos ativos")
    void testBuscarTodosCandidatosAtivos() {
        List<Candidato> candidatos = new ArrayList<>();
        candidatos.add(candidato);

        when(candidatoRepository.findAll()).thenReturn(candidatos);

        List<Candidato> result = candidatoRepository.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Ativo", result.get(0).getStatus());
        
        verify(candidatoRepository, times(1)).findAll(); //verifico se o teste é chamado 1x durante o teste
     //   assertEquals(candidato, result.get(0), "O candidato retornado deve ser o mesmo que foi simulado");
    }
    
    // ## buscar todos os candidatos ativos que sao prefeitos
    @Test
    @DisplayName("Buscar candidatos por função - Prefeitos")
    void testBuscarCandidatosPrefeitos() {
        Candidato prefeito = new Candidato();
		prefeito.setId(55L);
		prefeito.setNome("Jodio Joestar");
		prefeito.setCpf("254.728.080-90");
		prefeito.setNumero(9); //numero unico
		prefeito.setFuncao(1); //prefeito
		prefeito.setStatus("Ativo"); //ativo ou inativo
		prefeito.setVotosApurados(0);//inicia em zero

        List<Candidato> candidatosAtivos = new ArrayList<>();
        candidatosAtivos.add(prefeito);

        when(candidatoRepository.findByFuncaoAndStatus(1, "Ativo")).thenReturn(candidatosAtivos);

        List<Candidato> result = candidatoRepository.findByFuncaoAndStatus(1, "Ativo");// 1 representa prefeito e Ativo é seu status

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getFuncao());
        
       // verify(candidatoRepository, times(1)).findByFuncaoAndStatus(0, null); verifico quantas vezes o método deve rodar
    }
}
