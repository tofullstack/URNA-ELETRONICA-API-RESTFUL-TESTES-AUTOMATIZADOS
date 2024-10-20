package app.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
import app.controller.VotoController;
import app.entity.Apuracao;
import app.entity.Candidato;
import app.entity.Eleitor;
import app.entity.Voto;
import app.repository.CandidatoRepository;
import app.repository.VotoRepository;
import app.service.CandidatoService;
import app.service.EleitorService;

@SpringBootTest
public class VotoControllerTest {

    @Autowired
    VotoController votoController;
    
    @Autowired
    CandidatoController candidatoController;

    @MockBean
    VotoRepository votoRepository;

    @MockBean
    EleitorService eleitorService;
    
    @MockBean
    CandidatoService candidatoService;

    @MockBean
    CandidatoRepository candidatoRepository;

    Candidato prefeito = new Candidato();
    Candidato vereador = new Candidato();
    Eleitor eleitor = new Eleitor();
    Voto voto = new Voto();

    @BeforeEach
    void setup() {
        // mock prefeito
        prefeito.setId(1L);
        prefeito.setNome("Funny Valentine");
        prefeito.setCpf("254.728.080-90");
        prefeito.setNumero(23);
        prefeito.setFuncao(1); 
        prefeito.setStatus("Ativo");
        prefeito.setVotosApurados(0);

        // mock vereador
        vereador.setId(2L);
        vereador.setNome("Diego Brando");
        vereador.setCpf("829.444.749-87");
        vereador.setNumero(24);
        vereador.setFuncao(2); 
        vereador.setStatus("Ativo");
        vereador.setVotosApurados(0);

        // mock do eleitor
        eleitor.setId(5L);
        eleitor.setNome("Galadriel Lady");
        eleitor.setCpf("773.413.292-89");
        eleitor.setProfissao("Queen of Lothlórien");
        eleitor.setStatus("Apto"); 

        // mockando o voto
        voto.setId(1L);
        voto.setPrefeito(prefeito);
        voto.setVereador(vereador);
        voto.setHash(UUID.randomUUID().toString());

        // mockando o findById do eleitor
        Mockito.when(eleitorService.findById(5L)).thenReturn(eleitor);

        // mock do repository para prefeito e vereador
        Mockito.when(candidatoRepository.findById(1L)).thenReturn(Optional.of(prefeito));
        Mockito.when(candidatoRepository.findById(2L)).thenReturn(Optional.of(vereador));

        // mockando o save do mock
        Mockito.when(votoRepository.save(Mockito.any(Voto.class))).thenReturn(voto);
        
        when(candidatoRepository.findByFuncaoAndStatus(1, "Ativo"))
        .thenReturn(Collections.singletonList(prefeito));
    when(candidatoRepository.findByFuncaoAndStatus(2, "Ativo"))
        .thenReturn(Collections.singletonList(vereador));
    when(votoRepository.count()).thenReturn(10L); // Total de votos
    when(votoRepository.countByPrefeitoId(1L)).thenReturn((int) 5L); // Votos para prefeito
    when(votoRepository.countByVereadorId(2L)).thenReturn((int) 3L); // Votos para vereador
    
       
    }
    

    @Test
    @DisplayName("VOTO # RESPONSE: 'Sucesso' # STATUS: OK")
    void cenarioVotoSalvar() {
        
        ResponseEntity<String> retorno = votoController.votar(voto, 5L);

        assertEquals(HttpStatus.OK, retorno.getStatusCode());

        assertTrue(retorno.getBody().contains("Voto protocolado com sucesso!"));

        assertTrue(retorno.getBody().contains(voto.getHash()));
    }

    @Test
    @DisplayName("VOTO # RESPONSE: 'Falha' # STATUS: 400 / EXCEPTION")
    void cenarioVotoEleitorBloqueado() {
        
        Voto votoInvalido = new Voto();
        voto.setId(-29L);

        assertThrows(Exception.class, () -> {
        	ResponseEntity<String> retorno = votoController.votar(votoInvalido, -15L);
        });

    }


    @Test
    @DisplayName("VOTO # RESPONSE: 'Falha' # STATUS: 400 / BAD_REQUEST")
    void cenarioVotoBadRequest() {

    	Voto votoNulo = null;
    	ResponseEntity<String> retorno = votoController.votar(votoNulo, null);
    	assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
    }
    
 
	@Test
    @DisplayName("APURAÇÃO # Verifica a apuração correta dos votos e ordenação dos candidatos")
    void cenarioRealizarApuracao() {
 
        ResponseEntity<?> resposta = votoController.realizarApuracao(); //sem parametros 

        
        assertEquals(HttpStatus.OK, resposta.getStatusCode()); // ok?

        Object body = resposta.getBody();
        assertNotNull(body); 
        
        // verifica se o corpo é uma instância de Apuracao
        assertTrue(body instanceof Apuracao);
        Apuracao apuracao = (Apuracao) body;

        assertEquals(10, apuracao.getTotalVotos());


        List<Candidato> prefeitos = apuracao.getPrefeitos();
        assertEquals(1L, prefeitos.get(0).getId()); 


        List<Candidato> vereadores = apuracao.getVereadores();
        assertEquals(2L, vereadores.get(0).getId()); 
    }
	
	@Test
	@DisplayName("APURAÇÃO # Sem prefeitos")
	void cenarioApuracaoPrefeitoVazio() {
		List<Candidato> prefeitosVazio = new ArrayList<>();
		//List<Candidato> vereadoresVazio = new ArrayList<>();
		
		Mockito.when(candidatoRepository.findByFuncaoAndStatus(1, "Ativo")).thenReturn(prefeitosVazio);
		//Mockito.when(candidatoRepository.findByFuncaoAndStatus(2, "Ativo")).thenReturn(vereadoresVazio);
		
		ResponseEntity<?> resposta = votoController.realizarApuracao(); 
		
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
		assertTrue(resposta.getBody().toString().startsWith("Algo deu errado!"));
	}
	
	@Test
	@DisplayName("APURAÇÃO # Sem vereadores")
	void cenarioApuracaoVereadorVazio() {
		List<Candidato> vereadoresVazio = new ArrayList<>();
		
		Mockito.when(candidatoRepository.findByFuncaoAndStatus(2, "Ativo")).thenReturn(vereadoresVazio);
		
		ResponseEntity<?> resposta = votoController.realizarApuracao(); 
		
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
		assertTrue(resposta.getBody().toString().startsWith("Algo deu errado!"));
	}
	
	@Test
	@DisplayName("APURAÇÃO # Sem votos")
	void cenarioApuracaoSemVotos() {
		
		Mockito.when(votoRepository.count()).thenReturn(0l);
		
		ResponseEntity<?> resposta = votoController.realizarApuracao();
		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
		assertTrue(resposta.getBody().toString().equals("Algo deu errado! A apuração não pôde ser completada pois não existem votos a serem contados"));
	}
	
	@Test
	@DisplayName("VOTO # Segunda tentantiva de Voto")
	void cenarioVotarNovamente() {
		eleitor.setStatus("Votou");
		
		ResponseEntity<String> retorno = votoController.votar(voto, 5L);

        assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
        //assertEquals(retorno.getBody(), null);
		assertTrue(retorno.getBody().toString().equals("Algo deu errado! Este eleitor já votou e está impossibilitado de votar novamente."));

	}
	
	@Test
	@DisplayName("VOTO # Tentativa de voto de Eleitor pendente")
	void cenarioVotoEleitorPendente() {
		eleitor.setStatus("Pendente");
		
		ResponseEntity<String> retorno = votoController.votar(voto, 5L);

        assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
        assertEquals("Bloqueado",eleitor.getStatus());
		assertTrue(retorno.getBody().toString().equals("Algo deu errado! Este eleitor está impossibilitado de votar e recebeu o status Bloqueado."));

	}
	
	@Test
	@DisplayName("VOTO # Tentativa de voto de Eleitor inativo")
	void cenarioVotoEleitorInativo() {
		eleitor.setStatus("Inativo");
		
		ResponseEntity<String> retorno = votoController.votar(voto, 5L);

        assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
        assertEquals("Inativo", eleitor.getStatus());
		assertTrue(retorno.getBody().toString().equals("Algo deu errado! Este eleitor está Inativo e impossibilitado de votar."));

	}
	
	@Test
	@DisplayName("VOTO # Tentativa de voto de Eleitor Bloqueado")
	void cenarioTentativaVotoEleitorBloqueado() {
		eleitor.setStatus("Bloqueado");
		
		ResponseEntity<String> retorno = votoController.votar(voto, 5L);

        assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
        assertEquals("Bloqueado", eleitor.getStatus());
		assertTrue(retorno.getBody().toString().equals("Algo deu errado! Este eleitor já foi bloqueado e continua impossibilitado de votar."));

	}
	
	@Test
	@DisplayName("VOTO # Tentativa de voto para Prefeito inativo")
	void cenarioTentativaVotoPrefeitoInativo() {
		prefeito.setStatus("Inativo");
		
		ResponseEntity<String> retorno = votoController.votar(voto, 5L);

        assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
        assertEquals("Inativo", prefeito.getStatus());
		assertTrue(retorno.getBody().toString().equals("Algo deu errado! O candidato selecionado está inativo e não poderá receber um voto."));

	}
	
	@Test
	@DisplayName("VOTO # Tentativa de voto com informações incorretas")
	void cenarioTentativaVotoIncorreto() {
		prefeito.setFuncao(2);
		
		ResponseEntity<String> retorno = votoController.votar(voto, 5L);

        assertEquals(HttpStatus.BAD_REQUEST, retorno.getStatusCode());
		assertTrue(retorno.getBody().toString().equals("Algo deu errado! O voto fornecido contém informações incorretas e não será processado."));

	}
	
	

}
