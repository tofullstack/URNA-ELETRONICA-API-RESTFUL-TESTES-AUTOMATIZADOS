package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import app.entity.Voto;

public interface VotoRepository extends JpaRepository<Voto, Long>{

	/*filtro para contagem de votos 
	 
    int countByCandidatoPrefeitoId(Long id);
    
    */
	/*
    //versão com sql nativo
    @Query(value = "SELECT COUNT(*) FROM voto v WHERE v.candidato_prefeito_id = :id", nativeQuery = true)
    int countByCandidatoPrefeitoId(@Param("id") Long id);
    
    //versão com jpql
    int countByCandidatoVereadorId(Long id);*/

    int countByPrefeitoId(Long candidatoId);

    int countByVereadorId(Long candidatoId);
}