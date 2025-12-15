package com.suivie_academique.suivie_academique.services;

import com.suivie_academique.suivie_academique.dto.PersonnelDto;
import com.suivie_academique.suivie_academique.entities.Personnel;
import com.suivie_academique.suivie_academique.mappers.PersonnelMapper;
import com.suivie_academique.suivie_academique.repositories.PersonnelRepository;
import com.suivie_academique.suivie_academique.utils.CodeGenerator;
import com.suivie_academique.suivie_academique.utils.RolePersonnel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PersonnelService {

    private final PersonnelRepository personnelRepository;
    private final PersonnelMapper personnelMapper;
    private final CodeGenerator codeGenerator;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Création d’un membre du personnel
     */
    public PersonnelDto creerPersonnel(PersonnelDto dto, String motDePasse) {

        // Vérification login unique
        if (personnelRepository.existsByLoginPersonnel(dto.getLoginPersonnel())) {
            throw new RuntimeException("Ce login existe déjà");
        }

        // Vérification téléphone unique
        personnelRepository.findByPhonePersonnel(dto.getPhonePersonnel())
                .ifPresent(p -> {
                    throw new RuntimeException("Ce numéro de téléphone est déjà utilisé");
                });

        // Génération du code personnel
        String codeGenere = codeGenerator.generate(dto.getRolePersonnel());

        // Conversion DTO → ENTITE
        Personnel personnel = personnelMapper.toEntity(dto);
        personnel.setCodePersonnel(codeGenere);

        // Définition du mot de passe
        personnel.setPadPersonnel(passwordEncoder.encode(motDePasse)); // Tu peux plus tard ajouter BCrypt

        // Sauvegarde
        Personnel saved = personnelRepository.save(personnel);

        return personnelMapper.toDto(saved);
    }

    /**
     * Mise à jour d’un personnel
     */
    public PersonnelDto updatePersonnel(String code, PersonnelDto dto) {

        Personnel personnel = personnelRepository.findById(code)
                .orElseThrow(() -> new RuntimeException("Personnel introuvable"));

        personnel.setNomPersonnel(dto.getNomPersonnel());
        personnel.setLoginPersonnel(dto.getLoginPersonnel());
        personnel.setSexePersonnel(dto.getSexePersonnel());
        personnel.setPhonePersonnel(dto.getPhonePersonnel());
        personnel.setRolePersonnel(RolePersonnel.valueOf(dto.getRolePersonnel()));

        Personnel updated = personnelRepository.save(personnel);

        return personnelMapper.toDto(updated);
    }

    /**
     * Suppression d’un personnel
     */
    public void deletePersonnel(String code) {
        if (!personnelRepository.existsById(code)) {
            throw new RuntimeException("Personnel introuvable");
        }
        personnelRepository.deleteById(code);
    }

    /**
     * Recherche par nom
     */
    public List<PersonnelDto> rechercherParNom(String token) {
        return personnelMapper.toDtoList(
                personnelRepository.searchByName(token)
        );
    }

    /**
     * Recherche par login
     */
    public PersonnelDto getByLogin(String login) {
        Personnel p = personnelRepository.findByLoginPersonnel(login)
                .orElseThrow(() -> new RuntimeException("Login introuvable"));

        return personnelMapper.toDto(p);
    }

    /**
     * Recherche par téléphone
     */
    public PersonnelDto getByPhone(String phone) {
        Personnel p = personnelRepository.findByPhonePersonnel(phone)
                .orElseThrow(() -> new RuntimeException("Téléphone introuvable"));
        return personnelMapper.toDto(p);
    }

    /**
     * Liste par rôle
     */
    public List<PersonnelDto> getByRole(String role) {
        return personnelMapper.toDtoList(
                personnelRepository.findByRolePersonnel(RolePersonnel.valueOf(role))
        );
    }

    /**
     * Liste des enseignants
     */
    public List<PersonnelDto> getEnseignants() {
        return personnelMapper.toDtoList(
                personnelRepository.findAllEnseignants()
        );
    }

    /**
     * Responsables académiques
     */
    public List<PersonnelDto> getResponsablesAcademiques() {
        return personnelMapper.toDtoList(
                personnelRepository.findAllResponsablesAcademiques()
        );
    }

    /**
     * Responsables du personnel
     */
    public List<PersonnelDto> getResponsablesPersonnel() {
        return personnelMapper.toDtoList(
                personnelRepository.findAllResponsablesPersonnel()
        );
    }

    /**
     * Compte par sexe
     */
    public int countBySexe(String sexe) {
        return personnelRepository.countBySexe(sexe);
    }

    /**
     * Compte par rôle
     */
    public long countByRole(String role) {
        return personnelRepository.countByRole(RolePersonnel.valueOf(role));
    }

    /**
     * Liste complète
     */
    public List<PersonnelDto> getAll() {
        return personnelMapper.toDtoList(
                personnelRepository.findAll()
        );
    }
}
