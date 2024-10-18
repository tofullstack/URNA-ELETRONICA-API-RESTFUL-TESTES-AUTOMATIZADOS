package app.entity;

import java.util.List;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Aqui omitiremos a Annotation Entity pois não iremos persistir a classe no Banco de Dados
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Apuracao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private int totalVotos;
	
	private List<Candidato> prefeitos;
	
	private List<Candidato> vereadores;
	
}

//Query de count na Apuracao para calcular o total de votos é feito pelo ID do candidato. ##TODO

/*2.4 - O sistema deverá ter uma classe de APURAÇÃO que não deverá ser persistida. Portanto, é uma classe que não
deverá ter annotation @Entity e não terá tabela no banco de dados. Ela servirá apenas para representar um objeto
com o resultado das eleições que será totalmente calculado.*/