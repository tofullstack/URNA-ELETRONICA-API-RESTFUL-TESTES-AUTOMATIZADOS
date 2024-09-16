package app.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Voto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	/*TODO: perguntar ao professor se o notnull irá afetar a criação do voto, uma vez que ele impede que um objeto da classe seja criado sem essa informação.*/
	//@NotNull
	@Column(nullable = false)
	private LocalDate data;
	
	/*Vários Votos podem estar atribuídos a um único Candidato*/
	@ManyToOne
  //  @JoinColumn(name = "candidato_prefeito_id")
	@NotNull
	private Candidato prefeito;
	
	
    @ManyToOne
  //  @JoinColumn(name = "candidato_vereador_id")
	@NotNull
	private Candidato vereador;
	
	/*Documentação - 3.15*/
	private String hash; //= UUID.randomUUID().toString();

}

/*2.3 - O registro de um VOTO deverá conter data e hora da votação (campo obrigatório e obtido pelo sistema), o
candidato a prefeito escolhido (objeto obrigatório), o candidato a vereador escolhido (objeto obrigatório) e um
hash que será o comprovante*/

/*Não trataremos votos brancos e nulos. Então, os objetos dos candidatos são obrigatórios dentro do voto.*/