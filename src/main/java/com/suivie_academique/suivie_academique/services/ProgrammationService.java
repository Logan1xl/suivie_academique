package com.suivie_academique.suivie_academique.services;

import com.suivie_academique.suivie_academique.dto.ProgrammationDto;
import com.suivie_academique.suivie_academique.entities.*;
import com.suivie_academique.suivie_academique.mappers.ProgrammationMapper;
import com.suivie_academique.suivie_academique.repositories.*;
import com.suivie_academique.suivie_academique.utils.StatutProgrammation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProgrammationService {

    private final ProgrammationRepository programmationRepository;
    private final ProgrammationMapper programmationMapper;

    private final SalleRepository salleRepository;
    private final CoursRepository coursRepository;
    private final PersonnelRepository personnelRepository;

    /**
     * Création d’une programmation
     */
    public ProgrammationDto creerProgrammation(ProgrammationDto dto) {

        // ——————————————————————————
        // 1. Vérifications simples
        // ——————————————————————————

        if (dto.getCodeCours() == null || dto.getCodeCours().isEmpty())
            throw new RuntimeException("Le code du cours est obligatoire");

        if (dto.getCodeSalle() == null || dto.getCodeSalle().isEmpty())
            throw new RuntimeException("Le code de la salle est obligatoire");

        if (dto.getCodePersonnelProg() == null || dto.getCodePersonnelProg().isEmpty())
            throw new RuntimeException("Le programmateur est obligatoire");

        if (dto.getDateProgrammation() == null || dto.getFinProgrammation() == null)
            throw new RuntimeException("Les dates sont obligatoires");

        if (dto.getDateProgrammation().after(dto.getFinProgrammation()))
            throw new RuntimeException("La date de début doit être avant la date de fin");


        // ——————————————————————————
        // 2. Vérification de disponibilité de la salle
        // ——————————————————————————
        boolean dispo = programmationRepository.isSalleDisponible(
                dto.getCodeSalle(),
                dto.getDateProgrammation(),
                dto.getFinProgrammation()
        );

        if (!dispo)
            throw new RuntimeException("La salle est occupée pour cette période");


        // ——————————————————————————
        // 3. Conversion DTO → ENTITE
        // ——————————————————————————

        Programmation programmation = programmationMapper.toEntity(dto);

        // ——————————————————————————
        // 4. Injection manuelle des ENTITÉS associées
        // ——————————————————————————
        Salle salle = salleRepository.findById(dto.getCodeSalle())
                .orElseThrow(() -> new RuntimeException("Salle introuvable"));

        Cours cours = coursRepository.findById(dto.getCodeCours())
                .orElseThrow(() -> new RuntimeException("Cours introuvable"));

        Personnel programmateur = personnelRepository.findById(dto.getCodePersonnelProg())
                .orElseThrow(() -> new RuntimeException("Programmateur introuvable"));

        programmation.setSalle(salle);
        programmation.setCours(cours);
        programmation.setPersonnelProg(programmateur);

        // statut par défaut
        programmation.setStatutProgrammation(StatutProgrammation.PROGRAMMER);

        // ——————————————————————————
        // 5. Sauvegarde
        // ——————————————————————————
        Programmation sauvegardee = programmationRepository.save(programmation);

        // ——————————————————————————
        // 6. Retour DTO
        // ——————————————————————————
        return programmationMapper.toDto(sauvegardee);
    }

    /**
     * Mise à jour d’une programmation
     */
    public ProgrammationDto updateProgrammation(Integer id, ProgrammationDto dto) {

        Programmation prog = programmationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programmation introuvable"));

        // mise à jour simple
        prog.setNbHeure(dto.getNbHeure());
        prog.setDateProgrammation(dto.getDateProgrammation());
        prog.setFinProgrammation(dto.getFinProgrammation());

        // contrôle de disponibilité lors du changement de salle/date
        boolean dispo = programmationRepository.isSalleDisponible(
                dto.getCodeSalle(),
                dto.getDateProgrammation(),
                dto.getFinProgrammation()
        );

        if (!dispo)
            throw new RuntimeException("La salle est indisponible pour cette période");

        // relations
        Salle salle = salleRepository.findById(dto.getCodeSalle())
                .orElseThrow(() -> new RuntimeException("Salle introuvable"));
        prog.setSalle(salle);

        Cours cours = coursRepository.findById(dto.getCodeCours())
                .orElseThrow(() -> new RuntimeException("Cours introuvable"));
        prog.setCours(cours);

        // statut
        prog.setStatutProgrammation(StatutProgrammation.valueOf(dto.getStatutProgrammation()));

        Programmation modifiee = programmationRepository.save(prog);

        return programmationMapper.toDto(modifiee);
    }

    /**
     * Validation d’une programmation
     */
    public ProgrammationDto validerProgrammation(Integer id, String codePersonnelVal) {

        Programmation prog = programmationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programmation introuvable"));

        Personnel validateur = personnelRepository.findById(codePersonnelVal)
                .orElseThrow(() -> new RuntimeException("Validateur introuvable"));

        prog.setPersonnelVal(validateur);
        prog.setStatutProgrammation(StatutProgrammation.VALIDER);

        return programmationMapper.toDto(prog);
    }

    /**
     * Suppression d’une programmation
     */
    public void deleteProgrammation(Integer id) {
        if (!programmationRepository.existsById(id))
            throw new RuntimeException("Programmation introuvable");

        programmationRepository.deleteById(id);
    }

    // ——————————————————————————
    //      RECHERCHES
    // ——————————————————————————

    public List<ProgrammationDto> getByStatut(String statut) {
        return programmationMapper.toDtoList(
                programmationRepository.findByStatutProgrammation(StatutProgrammation.valueOf(statut))
        );
    }

    public List<ProgrammationDto> getBySalle(String codeSalle) {
        return programmationMapper.toDtoList(
                programmationRepository.findBySalle(codeSalle)
        );
    }

    public List<ProgrammationDto> getByCours(String codeCours) {
        return programmationMapper.toDtoList(
                programmationRepository.findByCours(codeCours)
        );
    }

    public List<ProgrammationDto> getByPersonnel(String codePersonnel) {
        return programmationMapper.toDtoList(
                programmationRepository.findByPersonnel(codePersonnel)
        );
    }

    public List<ProgrammationDto> getByValidateur(String codePersonnel) {
        return programmationMapper.toDtoList(
                programmationRepository.findByValidateur(codePersonnel)
        );
    }

    public List<ProgrammationDto> getProgrammationsEntre(Date debut, Date fin) {
        return programmationMapper.toDtoList(
                programmationRepository.findByDateRange(debut, fin)
        );
    }

    public List<ProgrammationDto> getEnAttenteValidation() {
        return programmationMapper.toDtoList(
                programmationRepository.findEnAttenteValidation()
        );
    }

    public List<ProgrammationDto> getProgrammationsDuJour(String codeSalle) {
        return programmationMapper.toDtoList(
                programmationRepository.findProgrammationsDuJour(codeSalle)
        );
    }

    public List<ProgrammationDto> getProgrammationsFutures() {
        return programmationMapper.toDtoList(
                programmationRepository.findProgrammationsFutures()
        );
    }

    public long countByStatut(String statut) {
        return programmationRepository.countByStatut(StatutProgrammation.valueOf(statut));
    }

    // Ajoutez ces méthodes dans votre classe ProgrammationService

    /**
     * Obtenir toutes les programmations
     */
    public List<ProgrammationDto> getAllProgrammations() {
        return programmationMapper.toDtoList(
                programmationRepository.findAll()
        );
    }

    /**
     * Obtenir une programmation par ID
     */
    public ProgrammationDto getProgrammationById(Integer id) {
        Programmation prog = programmationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Programmation introuvable avec l'ID: " + id));

        return programmationMapper.toDto(prog);
    }
}
