package com.suivie_academique.suivie_academique.repositories;

import com.suivie_academique.suivie_academique.entities.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Cours
 * Fournit les opérations CRUD et des requêtes personnalisées
 */
@Repository
public interface CoursRepository extends JpaRepository<Cours, String> {

    /**
     * Recherche les cours par label (nom)
     * Méthode dérivée de Spring Data JPA
     */
    List<Cours> findByLabelCoursContaining(String labelCours);

    /**
     * Recherche les cours par nombre de crédits
     * Méthode dérivée de Spring Data JPA
     */
    List<Cours> findByNbCreditCours(String nbCreditCours);

    /**
     * Recherche les cours avec un nombre d'heures supérieur ou égal
     * Requête JPQL avec CAST pour conversion de type
     */
    @Query("SELECT c FROM Cours c WHERE CAST(c.nbHeureCours AS int) >= :nbHeures")
    List<Cours> findCoursAvecNbHeuresMin(@Param("nbHeures") int nbHeures);

    /**
     * Compte le nombre de cours par nombre de crédits
     * Requête JPQL personnalisée
     */
    @Query("SELECT COUNT(c) FROM Cours c WHERE c.nbCreditCours = :credits")
    long countByCredits(@Param("credits") String credits);

    /**
     * Recherche avancée dans label et description
     * Recherche le mot-clé dans plusieurs champs
     */
    @Query("SELECT c FROM Cours c WHERE " +
            "c.labelCours LIKE CONCAT('%', :keyword, '%') OR " +
            "c.descCours LIKE CONCAT('%', :keyword, '%')")
    List<Cours> searchCours(@Param("keyword") String keyword);

    /**
     * Trouve tous les cours triés par nombre d'heures décroissant
     * Requête JPQL avec ORDER BY et CAST
     */
    @Query("SELECT c FROM Cours c ORDER BY CAST(c.nbHeureCours AS int) DESC")
    List<Cours> findAllOrderByNbHeuresDesc();

    /**
     * Vérifie si un cours existe par son label
     * Méthode dérivée de Spring Data JPA
     */
    boolean existsByLabelCours(String labelCours);
}