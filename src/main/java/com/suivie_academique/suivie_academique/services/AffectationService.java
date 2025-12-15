package com.suivie_academique.suivie_academique.services;

import com.suivie_academique.suivie_academique.dto.AffectationDto;
import com.suivie_academique.suivie_academique.entities.Affectation;
import com.suivie_academique.suivie_academique.entities.AffectationId;
import com.suivie_academique.suivie_academique.entities.Cours;
import com.suivie_academique.suivie_academique.entities.Personnel;
import com.suivie_academique.suivie_academique.mappers.AffectationMapper;
import com.suivie_academique.suivie_academique.repositories.AffectationRepository;
import com.suivie_academique.suivie_academique.repositories.CoursRepository;
import com.suivie_academique.suivie_academique.repositories.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AffectationService {

    private final AffectationRepository affectationRepository;
    private final PersonnelRepository personnelRepository;
    private final CoursRepository coursRepository;
    private final AffectationMapper affectationMapper;

    /**
     * 🔵 Créer une affectation (assigner un personnel à un cours)
     */
    public AffectationDto createAffectation(String codePersonnel, String codeCours) {

        // Vérifier personnel
        Personnel personnel = personnelRepository.findById(codePersonnel)
                .orElseThrow(() -> new RuntimeException("Personnel non trouvé: " + codePersonnel));

        // Vérifier cours
        Cours cours = coursRepository.findById(codeCours)
                .orElseThrow(() -> new RuntimeException("Cours non trouvé: " + codeCours));

        AffectationId id = new AffectationId(codeCours, codePersonnel);

        // Vérifier si déjà existant
        if (affectationRepository.existsById(id)) {
            throw new RuntimeException("Cette affectation existe déjà.");
        }

        Affectation affectation = new Affectation(id, personnel, cours);
        Affectation saved = affectationRepository.save(affectation);

        return affectationMapper.toDto(saved);
    }

    /**
     * 🔵 Supprimer une affectation
     */
    public void deleteAffectation(String codePersonnel, String codeCours) {
        AffectationId id = new AffectationId(codeCours, codePersonnel);
        if (!affectationRepository.existsById(id)) {
            throw new RuntimeException("Cette affectation n'existe pas.");
        }
        affectationRepository.deleteById(id);
    }

    /**
     * 🔵 Liste des affectations d'un personnel
     */
    public List<AffectationDto> getAffectationsByPersonnel(String codePersonnel) {
        List<Affectation> affectations = affectationRepository.findByPersonnel(codePersonnel);
        return affectationMapper.toDtoList(affectations);
    }

    /**
     * 🔵 Liste des affectations d'un cours
     */
    public List<AffectationDto> getAffectationsByCours(String codeCours) {
        List<Affectation> affectations = affectationRepository.findByCours(codeCours);
        return affectationMapper.toDtoList(affectations);
    }

    /**
     * 🔵 Compter les affectations d'un personnel
     */
    public long countByPersonnel(String codePersonnel) {
        return affectationRepository.countByPersonnel(codePersonnel);
    }

    /**
     * 🔵 Compter les affectations d'un cours
     */
    public long countByCours(String codeCours) {
        return affectationRepository.countByCours(codeCours);
    }

    /**
     * 🔵 Obtenir toutes les affectations
     */
    public List<AffectationDto> findAll() {
        List<Affectation> affectations = affectationRepository.findAll();
        return affectationMapper.toDtoList(affectations);
    }
}