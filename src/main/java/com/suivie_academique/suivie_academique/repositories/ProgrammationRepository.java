package com.suivie_academique.suivie_academique.repositories;

import com.suivie_academique.suivie_academique.entities.Programmation;
import com.suivie_academique.suivie_academique.utils.StatutProgrammation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repository pour l'entité Programmation
 * Fournit les opérations CRUD et des requêtes personnalisées
 */
@Repository
public interface ProgrammationRepository extends JpaRepository<Programmation, Integer> {

    /**
     * Trouve les programmations par statut
     * Méthode dérivée de Spring Data JPA
     */
    List<Programmation> findByStatutProgrammation(StatutProgrammation statut);

    /**
     * Trouve les programmations d'une salle spécifique
     * Requête JPQL avec navigation dans les relations
     */
    @Query("SELECT p FROM Programmation p WHERE p.salle.codeSalle = :codeSalle")
    List<Programmation> findBySalle(@Param("codeSalle") String codeSalle);

    /**
     * Trouve les programmations d'un cours spécifique
     * Requête JPQL avec navigation dans les relations
     */
    @Query("SELECT p FROM Programmation p WHERE p.cours.codeCours = :codeCours")
    List<Programmation> findByCours(@Param("codeCours") String codeCours);

    /**
     * Trouve les programmations d'un personnel (programmateur)
     * Requête JPQL avec navigation dans les relations
     */
    @Query("SELECT p FROM Programmation p WHERE p.personnelProg.codePersonnel = :codePersonnel")
    List<Programmation> findByPersonnel(@Param("codePersonnel") String codePersonnel);

    /**
     * Trouve les programmations validées par un personnel
     * Requête JPQL avec navigation dans les relations
     */
    @Query("SELECT p FROM Programmation p WHERE p.personnelVal.codePersonnel = :codePersonnel")
    List<Programmation> findByValidateur(@Param("codePersonnel") String codePersonnel);

    /**
     * Trouve les programmations entre deux dates
     * Requête JPQL avec conditions sur les dates
     */
    @Query("SELECT p FROM Programmation p WHERE " +
            "p.dateProgrammation >= :dateDebut AND p.finProgrammation <= :dateFin")
    List<Programmation> findByDateRange(
            @Param("dateDebut") Date dateDebut,
            @Param("dateFin") Date dateFin
    );

    /**
     * Vérifie si une salle est disponible pour une période donnée
     * Retourne true si la salle est disponible, false sinon
     * Vérifie les chevauchements de dates
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN false ELSE true END " +
            "FROM Programmation p WHERE p.salle.codeSalle = :codeSalle AND " +
            "((p.dateProgrammation <= :dateDebut AND p.finProgrammation >= :dateDebut) OR " +
            "(p.dateProgrammation <= :dateFin AND p.finProgrammation >= :dateFin) OR " +
            "(p.dateProgrammation >= :dateDebut AND p.finProgrammation <= :dateFin))")
    boolean isSalleDisponible(
            @Param("codeSalle") String codeSalle,
            @Param("dateDebut") Date dateDebut,
            @Param("dateFin") Date dateFin
    );

    /**
     * Compte les programmations par statut
     * Requête JPQL personnalisée
     */
    @Query("SELECT COUNT(p) FROM Programmation p WHERE p.statutProgrammation = :statut")
    long countByStatut(@Param("statut") StatutProgrammation statut);

    /**
     * Trouve les programmations en attente de validation
     * Programmations avec statut PROGRAMMER et sans validateur
     */
    @Query("SELECT p FROM Programmation p WHERE p.statutProgrammation = 'PROGRAMMER' " +
            "AND p.personnelVal IS NULL")
    List<Programmation> findEnAttenteValidation();

    /**
     * Trouve les programmations du jour pour une salle
     * Utilise CURRENT_DATE pour la date du jour
     */
    @Query("SELECT p FROM Programmation p WHERE p.salle.codeSalle = :codeSalle " +
            "AND CAST(p.dateProgrammation AS date) = CURRENT_DATE")
    List<Programmation> findProgrammationsDuJour(@Param("codeSalle") String codeSalle);

    /**
     * Trouve les programmations futures (à partir d'aujourd'hui)
     * Requête JPQL avec comparaison de dates
     */
    @Query("SELECT p FROM Programmation p WHERE p.dateProgrammation >= CURRENT_TIMESTAMP " +
            "ORDER BY p.dateProgrammation ASC")
    List<Programmation> findProgrammationsFutures();
}