package com.suivie_academique.suivie_academique.mappers;

import com.suivie_academique.suivie_academique.dto.SalleDto;
import com.suivie_academique.suivie_academique.entities.Salle;
import com.suivie_academique.suivie_academique.utils.SalleStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper MapStruct pour convertir entre Salle et SalleDto
 *
 * @Mapper génère automatiquement l'implémentation du mapper
 * componentModel = "spring" permet l'injection de dépendance Spring
 *
 * IMPORTANT: Ceci est une INTERFACE. MapStruct génère automatiquement
 * l'implémentation (SalleMapperImpl) lors de la compilation
 */
@Component
@Mapper(componentModel = "spring")
public interface SalleMapper {

    /**
     * Convertit une entité Salle en SalleDto
     *
     * @param salle l'entité source
     * @return le DTO correspondant
     */
    @Mapping(source = "descSalle", target = "description")
    @Mapping(source = "statutSalle", target = "statusSalle", qualifiedByName = "enumToString")
    SalleDto toDto(Salle salle);

    /**
     * Convertit un SalleDto en entité Salle
     *
     * @param salleDto le DTO source
     * @return l'entité correspondante
     */
    @Mapping(source = "description", target = "descSalle")
    @Mapping(source = "statusSalle", target = "statutSalle", qualifiedByName = "stringToEnum")
    @Mapping(target = "programmations", ignore = true)
    Salle toEntity(SalleDto salleDto);

    /**
     * Convertit une liste de Salle en liste de SalleDto
     *
     * @param salles liste d'entités
     * @return liste de DTOs
     */
    List<SalleDto> toDtoList(List<Salle> salles);

    /**
     * Convertit une liste de SalleDto en liste de Salle
     *
     * @param salleDtos liste de DTOs
     * @return liste d'entités
     */
    List<Salle> toEntityList(List<SalleDto> salleDtos);

    /**
     * Méthode personnalisée pour convertir l'enum SalleStatus en String
     */
    @Named("enumToString")
    default String enumToString(SalleStatus status) {
        return status != null ? status.name() : null;
    }

    /**
     * Méthode personnalisée pour convertir String en enum SalleStatus
     */
    @Named("stringToEnum")
    default SalleStatus stringToEnum(String status) {
        return status != null ? SalleStatus.valueOf(status) : null;
    }
}