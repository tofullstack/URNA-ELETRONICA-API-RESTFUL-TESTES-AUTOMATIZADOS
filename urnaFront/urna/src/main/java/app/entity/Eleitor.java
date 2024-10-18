package app.entity;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Eleitor {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	//regexp = "^\\w+(\\s+\\w+)+$\r\n"
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ]+(\\s+[A-Za-zÀ-ÖØ-öø-ÿ]+)+$", message = "Tente novamente! O nome deve conter pelo menos duas palavras e um espaço.") //regex do google
	@NotBlank(message = "Tente novamente! O eleitor deve ter um nome")
	private String nome;
    
    @CPF
    private String cpf;
    
    @NotBlank
    private String profissao;
    
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{4,5}-\\d{4}$", message = "Tente novamente! O telefone celular deve seguir o padrão (XX) XXXX-XXXX ou (XX) XXXXX-XXXX.")
    @NotBlank(message = "Tente novamente! O eleitor deve ter um telefone celular")
    private String telefoneCelular;
    
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{4,5}-\\d{4}$", message = "Tente novamente! O telefone fixo deve seguir o padrão (XX) XXXX-XXXX ou (XX) XXXXX-XXXX.")
    private String telefoneFixo;
   
    @Email
    private String email;
    
    //TODO: Perguntar ao professor
    /*Quando adicionamos a @Transient aqui, ele prejudica o findAll. Acredito que não seja necessário adicionar, pois a validação dentro das funções de save e update já sobrescrevem qualquer entrada.*/ 
    private String status; //Ativo, Inativo, Pendente, Bloqueado
}


/*2.1 - O cadastro de um ELEITOR deverá conter nome completo (obrigatório), CPF válido (mas não obrigatório – pode
ser nulo inicialmente), profissão (obrigatório), telefone celular válido (obrigatório), telefone fixo válido (mas não
obrigatório), endereço de e-mail válido (mas não obrigatório – pode ser nulo inicialmente) e status (processado
pelo sistema)*/