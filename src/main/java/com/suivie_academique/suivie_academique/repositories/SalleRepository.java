package com.suivie_academique.suivie_academique.repositories;

import com.suivie_academique.suivie_academique.entities.Salle;
import com.suivie_academique.suivie_academique.utils.SalleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Salle
 * Fournit les opérations CRUD et des requêtes personnalisées
 */
@Repository
public interface SalleRepository extends JpaRepository<Salle, String> {

    /**
     * Vérifie si une salle avec une contenance spécifique existe
     * Méthode dérivée de Spring Data JPA
     */
    Boolean existsByContenance(int contenance);

    /**
     * Trouve les salles avec une contenance >= à la valeur spécifiée
     * Méthode dérivée de Spring Data JPA
     */
    List<Salle> findByContenanceGreaterThanEqual(int contenance);

    /**
     * Recherche les salles dont le code contient la chaîne spécifiée
     * Méthode dérivée de Spring Data JPA
     */
    List<Salle> findByCodeSalleContaining(String codeSalle);

    /**
     * Trouve les salles par statut
     * Méthode dérivée de Spring Data JPA
     */
    List<Salle> findByStatutSalle(SalleStatus statutSalle);

    /**
     * Trouve les salles libres
     * Requête JPQL personnalisée
     */
    @Query("SELECT s FROM Salle s WHERE s.statutSalle = 'LIBRE'")
    List<Salle> findSallesLibres();

    /**
     * Trouve les salles occupées
     * Requête JPQL personnalisée
     */
    @Query("SELECT s FROM Salle s WHERE s.statutSalle = 'OCCUPE'")
    List<Salle> findSallesOccupees();

    /**
     * Compte les salles par statut
     * Requête JPQL personnalisée avec paramètre
     */
    @Query("SELECT COUNT(s) FROM Salle s WHERE s.statutSalle = :statut")
    long countByStatut(@Param("statut") SalleStatus statut);

    /**
     * Trouve les salles avec une contenance dans un intervalle
     * Méthode dérivée de Spring Data JPA
     */
    List<Salle> findByContenanceBetween(int min, int max);

    /**
     * Recherche avancée avec plusieurs critères optionnels
     * Utilise des paramètres nullables pour filtrage dynamique
     */
    @Query("SELECT s FROM Salle s WHERE " +
            "(:statut IS NULL OR s.statutSalle = :statut) AND " +
            "(:minContenance IS NULL OR s.contenance >= :minContenance) AND " +
            "(:maxContenance IS NULL OR s.contenance <= :maxContenance)")
    List<Salle> searchSalles(
            @Param("statut") SalleStatus statut,
            @Param("minContenance") Integer minContenance,
            @Param("maxContenance") Integer maxContenance
    );

    /**
     * Trouve les salles libres avec une contenance minimale
     * Combine plusieurs conditions
     */
    @Query("SELECT s FROM Salle s WHERE s.statutSalle = 'LIBRE' AND s.contenance >= :minContenance")
    List<Salle> findSallesLibresAvecContenance(@Param("minContenance") int minContenance);
}