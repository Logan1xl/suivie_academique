package com.suivie_academique.suivie_academique.mappers;

import com.suivie_academique.suivie_academique.dto.AffectationDto;
import com.suivie_academique.suivie_academique.entities.Affectation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper MapStruct pour convertir entre Affectation et AffectationDto
 * IMPORTANT: Ne pas ajouter @Component ici, MapStruct le gère automatiquement
 */
@Mapper(componentModel = "spring")
public interface AffectationMapper {

    /**
     * Convertit une entité Affectation en AffectationDto
     * Extrait les codes et les noms du personnel et du cours
     */
    @Mapping(source = "personnel.codePersonnel", target = "codePersonnel")
    @Mapping(source = "cours.codeCours", target = "codeCours")
    @Mapping(source = "personnel.nomPersonnel", target = "nomPersonnel")
    @Mapping(source = "cours.labelCours", target = "nomCours")
    AffectationDto toDto(Affectation affectation);

    /**
     * Convertit un AffectationDto en entité Affectation
     * Les relations doivent être définies manuellement dans le service
     */
    @Mapping(target = "codeAffectation", ignore = true)
    @Mapping(target = "personnel", ignore = true)
    @Mapping(target = "cours", ignore = true)
    Affectation toEntity(AffectationDto affectationDto);

    /**
     * Convertit une liste d'Affectation en liste d'AffectationDto
     */
    List<AffectationDto> toDtoList(List<Affectation> affectations);
}