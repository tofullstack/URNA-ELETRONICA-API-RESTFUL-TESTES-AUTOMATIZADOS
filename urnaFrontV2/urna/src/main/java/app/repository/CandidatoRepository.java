package app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.entity.Candidato;

public interface CandidatoRepository extends JpaRepository<Candidato, Long>{

	// buscar candidatos por função e status
    List<Candidato> findByFuncaoAndStatus(int funcao, String status);
    
    Optional<Candidato> findByNumero(Integer numero);
}