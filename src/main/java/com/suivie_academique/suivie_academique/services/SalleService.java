package com.suivie_academique.suivie_academique.services;

import com.suivie_academique.suivie_academique.dto.SalleDto;
import com.suivie_academique.suivie_academique.entities.Salle;
import com.suivie_academique.suivie_academique.mappers.SalleMapper;
import com.suivie_academique.suivie_academique.repositories.SalleRepository;
import com.suivie_academique.suivie_academique.utils.SalleStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service pour la gestion des salles
 * Démontre l'utilisation correcte de MapStruct avec les repositories
 */
@Service
@RequiredArgsConstructor  // Injection de dépendances par constructeur (Lombok)
@Transactional
public class SalleService {

    private static final Logger logger =  LoggerFactory.getLogger(SalleService.class);


    // Injection automatique par Spring
    private final SalleRepository salleRepository;
    private final SalleMapper salleMapper;  // MapStruct génère l'implémentation

    /**
     * Créer une nouvelle salle à partir d'un DTO
     *
     * @param salleDto les données de la salle
     * @return la salle créée (en DTO)
     */
    public SalleDto creerSalle(SalleDto salleDto) {
        // 1. Conversion DTO -> Entity avec MapStruct
        Salle salle = salleMapper.toEntity(salleDto);

        // 2. Sauvegarde en base de données

        if(salleDto.getCodeSalle().isEmpty() || salleDto.getContenance() <10){
            throw new RuntimeException("Donnée invalide");
        }else {
            Salle salleSauvegardee = salleRepository.save(salle);

            // 3. Conversion Entity -> DTO pour le retour
            return salleMapper.toDto(salleSauvegardee);

        }

    }

    /**
     * Récupérer toutes les salles
     *
     * @return liste de toutes les salles (en DTOs)
     */


    public List<SalleDto> getAllSalles() {
        // 1. Récupération des entités
        List<Salle> salles = salleRepository.findAll();

        // 2. Conversion List<Entity> -> List<DTO> avec MapStruct
        return salleMapper.toDtoList(salles);
    }

    /**
     * Récupérer une salle par son code
     *
     * @param codeSalle le code de la salle
     * @return la salle (en DTO)
     * @throws RuntimeException si la salle n'existe pas
     */
    public SalleDto getSalleByCode(String codeSalle) {
        // 1. Récupération de l'entité
        Salle salle = salleRepository.findById(codeSalle)
                .orElseThrow(() -> new RuntimeException("Salle introuvable avec le code: " + codeSalle));

        // 2. Conversion Entity -> DTO
        return salleMapper.toDto(salle);
    }

    /**
     * Mettre à jour une salle existante
     *
     * @param codeSalle le code de la salle à mettre à jour
     * @param salleDto les nouvelles données
     * @return la salle mise à jour (en DTO)
     */
    public SalleDto updateSalle(String codeSalle, SalleDto salleDto) {
        // 1. Vérifier que la salle existe
        Salle salleExistante = salleRepository.findById(codeSalle)
                .orElseThrow(() -> new RuntimeException("Salle introuvable avec le code: " + codeSalle));

        // 2. Mettre à jour les champs
        salleExistante.setDescSalle(salleDto.getDescription());
        salleExistante.setContenance(salleDto.getContenance());
        salleExistante.setStatutSalle(SalleStatus.valueOf(salleDto.getStatusSalle()));

        // 3. Sauvegarder
        Salle salleModifiee = salleRepository.save(salleExistante);

        // 4. Retourner le DTO
        return salleMapper.toDto(salleModifiee);
    }

    /**
     * Supprimer une salle
     *
     * @param codeSalle le code de la salle à supprimer
     */
    public void deleteSalle(String codeSalle) {

        logger.info("Recherche de la salle avec code" +codeSalle);
        // Vérifier que la salle existe
        if (!salleRepository.existsById(codeSalle)) {
            logger.error("Salle introuvable donc modification impossible");
            throw new RuntimeException("Salle introuvable avec le code: " + codeSalle);
        }else{
            salleRepository.deleteById(codeSalle);
            logger.info("Salle supprimee avec succes" +codeSalle);
        }

    }

    /**
     * Rechercher les salles libres avec une contenance minimale
     * Utilise une requête personnalisée du repository
     *
     * @param contenanceMin la contenance minimale
     * @return liste des salles libres (en DTOs)
     */
    public List<SalleDto> rechercherSallesLibres(int contenanceMin) {
        // 1. Utilisation d'une requête personnalisée
        List<Salle> salles = salleRepository.findSallesLibresAvecContenance(contenanceMin);

        // 2. Conversion automatique avec MapStruct
        return salleMapper.toDtoList(salles);
    }

    /**
     * Rechercher des salles par code (contient)
     *
     * @param codeSalle le fragment de code à rechercher
     * @return liste des salles correspondantes (en DTOs)
     */
    public List<SalleDto> rechercherParCode(String codeSalle) {
        List<Salle> salles = salleRepository.findByCodeSalleContaining(codeSalle);
        return salleMapper.toDtoList(salles);
    }

    /**
     * Rechercher des salles par statut
     *
     * @param statut le statut recherché
     * @return liste des salles avec ce statut (en DTOs)
     */
    public List<SalleDto> rechercherParStatut(String statut) {
        SalleStatus statutEnum = SalleStatus.valueOf(statut);
        List<Salle> salles = salleRepository.findByStatutSalle(statutEnum);
        return salleMapper.toDtoList(salles);
    }

    /**
     * Recherche avancée avec plusieurs critères
     *
     * @param statut le statut (optionnel)
     * @param minContenance la contenance minimale (optionnel)
     * @param maxContenance la contenance maximale (optionnel)
     * @return liste des salles correspondantes (en DTOs)
     */
    public List<SalleDto> rechercheAvancee(String statut, Integer minContenance, Integer maxContenance) {
        SalleStatus statutEnum = statut != null ? SalleStatus.valueOf(statut) : null;

        List<Salle> salles = salleRepository.searchSalles(statutEnum, minContenance, maxContenance);

        return salleMapper.toDtoList(salles);
    }

    /**
     * Compter les salles par statut
     *
     * @param statut le statut
     * @return le nombre de salles avec ce statut
     */
    public long compterParStatut(String statut) {
        SalleStatus statutEnum = SalleStatus.valueOf(statut);
        return salleRepository.countByStatut(statutEnum);
    }
}