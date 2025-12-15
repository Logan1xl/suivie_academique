package com.suivie_academique.suivie_academique.services;


import com.suivie_academique.suivie_academique.dto.CoursDto;
import com.suivie_academique.suivie_academique.dto.SalleDto;
import com.suivie_academique.suivie_academique.entities.Cours;
import com.suivie_academique.suivie_academique.entities.Salle;
import com.suivie_academique.suivie_academique.mappers.CoursMapper;
import com.suivie_academique.suivie_academique.mappers.SalleMapper;
import com.suivie_academique.suivie_academique.repositories.CoursRepository;
import com.suivie_academique.suivie_academique.repositories.SalleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor  // Injection de dépendances par constructeur (Lombok)
@Transactional
public class CourService {
    private final CoursRepository coursRepository;
    private final CoursMapper coursMapper;  // MapStruct génère l'implémentation

    public CoursDto creerCour(CoursDto coursDto) {
        // 1. Conversion DTO -> Entity avec MapStruct
        Cours cours = coursMapper.toEntity(coursDto);

        // 2. Sauvegarde en base de données

        if(coursDto.getCodeCours().isEmpty()){
            throw new RuntimeException("Donnée invalide");
        }else {
            Cours cours1 = coursRepository.save(cours);

            // 3. Conversion Entity -> DTO pour le retour
            return coursMapper.toDto(cours1);

        }

    }

    /**
     * Récupérer toutes les salles
     *
     * @return liste de toutes les salles (en DTOs)
     */


    public List<CoursDto> getAllCours() {
        // 1. Récupération des entités
        List<Cours> cours =  coursRepository.findAll()  ;

        // 2. Conversion List<Entity> -> List<DTO> avec MapStruct
        return coursMapper.toDtoList(cours);
    }

    public void deleteCours(String codeCours) {
        // Vérifier que la salle existe
        if (!coursRepository.existsById(codeCours)) {
            throw new RuntimeException("Salle introuvable avec le code: " + codeCours);
        }

        coursRepository.deleteById(codeCours);
    }







}
