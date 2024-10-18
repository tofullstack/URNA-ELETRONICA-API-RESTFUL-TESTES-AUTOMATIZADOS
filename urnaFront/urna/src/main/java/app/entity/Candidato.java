package app.entity;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Candidato {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ]+(\\s+[A-Za-zÀ-ÖØ-öø-ÿ]+)+$", message = "Tente novamente! O nome deve conter pelo menos duas palavras e um espaço.") //regex do google
	@NotBlank(message = "Tente novamente! O candidato deve ter um nome")
	private String nome;
	
	@CPF(message = "Tente novamente! O CPF do candidato deverá seguir o padrão XXX.XXX.XXX-XX")
	@NotBlank(message = "Tente novamente! O candidato deve ter um CPF")
    private String cpf;
	
	@Column(unique = true)
	@NotNull
	private int numero;
	
	@NotNull(message = "Tente novamente! O candidato deve ter uma função")
	private int funcao; //1:prefeito ou 2:vereador
	 
	private String status;
	
	@Transient
	private int votosApurados;
	
}

/*2.2 - O cadastro de um CANDIDATO deverá conter nome completo (obrigatório), CPF válido (obrigatório), número do
candidato (obrigatório e ÚNICO), função (campo obrigatório), status (processado pelo sistema, conforme regras
descritas no Item 3) e votos apurados (transiente e calculado pelo sistema).*/