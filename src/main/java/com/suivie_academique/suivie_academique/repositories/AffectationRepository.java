package com.suivie_academique.suivie_academique.repositories;

import com.suivie_academique.suivie_academique.entities.Affectation;
import com.suivie_academique.suivie_academique.entities.AffectationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Affectation
 * Gère les affectations de personnel aux cours
 */
@Repository
public interface AffectationRepository extends JpaRepository<Affectation, AffectationId> {

    /**
     * Trouve toutes les affectations d'un personnel
     * Requête JPQL avec navigation dans la clé composite
     */
    @Query("SELECT a FROM Affectation a WHERE a.personnel.codePersonnel = :codePersonnel")
    List<Affectation> findByPersonnel(@Param("codePersonnel") String codePersonnel);

    /**
     * Trouve toutes les affectations d'un cours
     * Requête JPQL avec navigation dans la clé composite
     */
    @Query("SELECT a FROM Affectation a WHERE a.cours.codeCours = :codeCours")
    List<Affectation> findByCours(@Param("codeCours") String codeCours);

    /**
     * Compte le nombre d'affectations d'un personnel
     */
    @Query("SELECT COUNT(a) FROM Affectation a WHERE a.personnel.codePersonnel = :codePersonnel")
    long countByPersonnel(@Param("codePersonnel") String codePersonnel);

    /**
     * Compte le nombre d'affectations d'un cours
     */
    @Query("SELECT COUNT(a) FROM Affectation a WHERE a.cours.codeCours = :codeCours")
    long countByCours(@Param("codeCours") String codeCours);
}