package com.suivie_academique.suivie_academique.mappers;

import com.suivie_academique.suivie_academique.dto.CoursDto;
import com.suivie_academique.suivie_academique.entities.Cours;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper MapStruct pour convertir entre Cours et CoursDto
 *
 * IMPORTANT: Ceci est une INTERFACE. MapStruct génère automatiquement
 * l'implémentation (CoursMapperImpl) lors de la compilation
 */
@Component
@Mapper(componentModel = "spring")
public interface CoursMapper {

    /**
     * Convertit une entité Cours en CoursDto
     *
     * @param cours l'entité source
     * @return le DTO correspondant
     */
    CoursDto toDto(Cours cours);

    /**
     * Convertit un CoursDto en entité Cours
     *
     * @param coursDto le DTO source
     * @return l'entité correspondante
     */
    @Mapping(target = "programmations", ignore = true)
    Cours toEntity(CoursDto coursDto);

    /**
     * Convertit une liste de Cours en liste de CoursDto
     *
     * @param cours liste d'entités
     * @return liste de DTOs
     */
    List<CoursDto> toDtoList(List<Cours> cours);

    /**
     * Convertit une liste de CoursDto en liste de Cours
     *
     * @param coursDtos liste de DTOs
     * @return liste d'entités
     */
    List<Cours> toEntityList(List<CoursDto> coursDtos);
}