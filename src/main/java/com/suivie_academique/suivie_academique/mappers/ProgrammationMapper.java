package com.suivie_academique.suivie_academique.mappers;

import com.suivie_academique.suivie_academique.dto.ProgrammationDto;
import com.suivie_academique.suivie_academique.entities.Programmation;
import com.suivie_academique.suivie_academique.utils.StatutProgrammation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper MapStruct pour convertir entre Programmation et ProgrammationDto
 * Gère les relations complexes avec les entités associées
 *
 * IMPORTANT: Ceci est une INTERFACE. MapStruct génère automatiquement
 * l'implémentation (ProgrammationMapperImpl) lors de la compilation
 */
@Component
@Mapper(componentModel = "spring")
public interface ProgrammationMapper {

    /**
     * Convertit une entité Programmation en ProgrammationDto
     * Extrait automatiquement les informations des entités liées
     *
     * @param programmation l'entité source
     * @return le DTO correspondant avec toutes les informations des relations
     */
    @Mapping(source = "salle.codeSalle", target = "codeSalle")
    @Mapping(source = "cours.codeCours", target = "codeCours")
    @Mapping(source = "cours.labelCours", target = "nomCours")
    @Mapping(source = "personnelProg.codePersonnel", target = "codePersonnelProg")
    @Mapping(source = "personnelProg.nomPersonnel", target = "nomPersonnelProg")
    @Mapping(source = "personnelVal.codePersonnel", target = "codePersonnelVal")
    @Mapping(source = "personnelVal.nomPersonnel", target = "nomPersonnelVal")
    @Mapping(source = "statutProgrammation", target = "statutProgrammation", qualifiedByName = "statutToString")
    ProgrammationDto toDto(Programmation programmation);

    /**
     * Convertit un ProgrammationDto en entité Programmation
     * ATTENTION: Les entités liées (salle, cours, personnel) doivent être
     * définies séparément dans le service
     *
     * @param programmationDto le DTO source
     * @return l'entité correspondante (sans les relations)
     */
    @Mapping(source = "statutProgrammation", target = "statutProgrammation", qualifiedByName = "stringToStatut")
    @Mapping(target = "salle", ignore = true)
    @Mapping(target = "cours", ignore = true)
    @Mapping(target = "personnelProg", ignore = true)
    @Mapping(target = "personnelVal", ignore = true)
    Programmation toEntity(ProgrammationDto programmationDto);

    /**
     * Convertit une liste de Programmation en liste de ProgrammationDto
     *
     * @param programmations liste d'entités
     * @return liste de DTOs avec toutes les informations
     */
    List<ProgrammationDto> toDtoList(List<Programmation> programmations);

    /**
     * Méthode personnalisée pour convertir StatutProgrammation enum en String
     */
    @Named("statutToString")
    default String statutToString(StatutProgrammation statut) {
        return statut != null ? statut.name() : null;
    }

    /**
     * Méthode personnalisée pour convertir String en StatutProgrammation enum
     */
    @Named("stringToStatut")
    default StatutProgrammation stringToStatut(String statut) {
        return statut != null ? StatutProgrammation.valueOf(statut) : null;
    }
}