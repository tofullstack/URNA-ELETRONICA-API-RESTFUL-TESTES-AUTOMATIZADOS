package app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.Eleitor;

public interface EleitorRepository extends JpaRepository<Eleitor, Long>{
   // Optional<Eleitor> findByCpf(String cpf); // método para buscar pelo CPF / implementação a urna-front
	  Eleitor findByCpf(String cpf);

}