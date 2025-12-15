package com.suivie_academique.suivie_academique.repositories;

import com.suivie_academique.suivie_academique.entities.Personnel;
import com.suivie_academique.suivie_academique.utils.RolePersonnel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Personnel
 * Fournit les opérations CRUD et des requêtes personnalisées
 */
@Repository
public interface PersonnelRepository extends JpaRepository<Personnel, String> {

    /**
     * Recherche du personnel par nom (LIKE)
     *
     * CORRECTION IMPORTANTE:
     * - Utiliser 'p' minuscule (alias de l'entité)
     * - :token sans quotes pour le paramètre
     * - CONCAT pour ajouter les % avant et après le token
     */
    @Query("SELECT p FROM Personnel p WHERE p.nomPersonnel LIKE CONCAT('%', :token, '%')")
    List<Personnel> searchByName(@Param("token") String token);

    /**
     * Compte le nombre de personnel par sexe
     * Utilise une requête native SQL
     *
     * CORRECTION: sexe en String au lieu de char pour compatibilité
     */
    @Query(value = "SELECT COUNT(*) FROM personnel WHERE sexe_personnel = :sexe", nativeQuery = true)
    int countBySexe(@Param("sexe") String sexe);

    /**
     * Recherche par rôle
     * Méthode dérivée de Spring Data JPA
     */
    List<Personnel> findByRolePersonnel(RolePersonnel rolePersonnel);

    /**
     * Recherche par login
     * Méthode dérivée de Spring Data JPA
     */
    Optional<Personnel> findByLoginPersonnel(String loginPersonnel);

    /**
     * Vérifie si un login existe déjà
     * Méthode dérivée de Spring Data JPA
     */
    boolean existsByLoginPersonnel(String loginPersonnel);

    /**
     * Recherche par téléphone
     * Méthode dérivée de Spring Data JPA
     */
    Optional<Personnel> findByPhonePersonnel(String phonePersonnel);

    /**
     * Compte le nombre de personnel par rôle
     * Requête JPQL personnalisée
     */
    @Query("SELECT COUNT(p) FROM Personnel p WHERE p.rolePersonnel = :role")
    long countByRole(@Param("role") RolePersonnel role);

    /**
     * Recherche les enseignants uniquement
     * Requête JPQL personnalisée
     */
    @Query("SELECT p FROM Personnel p WHERE p.rolePersonnel = 'ENSEIGNANT'")
    List<Personnel> findAllEnseignants();

    /**
     * Recherche les responsables académiques
     * Requête JPQL personnalisée
     */
    @Query("SELECT p FROM Personnel p WHERE p.rolePersonnel = 'RESPONSABLE_ACADEMIQUE'")
    List<Personnel> findAllResponsablesAcademiques();

    /**
     * Recherche les responsables du personnel
     * Requête JPQL personnalisée
     */
    @Query("SELECT p FROM Personnel p WHERE p.rolePersonnel = 'RESPONSABLE_PERSONNEL'")
    List<Personnel> findAllResponsablesPersonnel();
}