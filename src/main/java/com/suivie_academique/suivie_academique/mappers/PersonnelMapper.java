package com.suivie_academique.suivie_academique.mappers;

import com.suivie_academique.suivie_academique.dto.PersonnelDto;
import com.suivie_academique.suivie_academique.entities.Personnel;
import com.suivie_academique.suivie_academique.utils.RolePersonnel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper MapStruct pour convertir entre Personnel et PersonnelDto
 *
 * IMPORTANT: Ceci est une INTERFACE. MapStruct génère automatiquement
 * l'implémentation (PersonnelMapperImpl) lors de la compilation
 */
@Component
@Mapper(componentModel = "spring")
public interface PersonnelMapper {

    /**
     * Convertit une entité Personnel en PersonnelDto
     * Le mot de passe n'est pas exposé dans le DTO pour des raisons de sécurité
     *
     * @param personnel l'entité source
     * @return le DTO correspondant
     */
    @Mapping(source = "rolePersonnel", target = "rolePersonnel", qualifiedByName = "roleToString")
    PersonnelDto toDto(Personnel personnel);

    /**
     * Convertit un PersonnelDto en entité Personnel
     * Le mot de passe doit être défini séparément après la création
     *
     * @param personnelDto le DTO source
     * @return l'entité correspondante
     */
    @Mapping(source = "rolePersonnel", target = "rolePersonnel", qualifiedByName = "stringToRole")
    @Mapping(target = "padPersonnel", ignore = true)  // Le mot de passe ne vient jamais du DTO
    @Mapping(target = "programmations", ignore = true)
    @Mapping(target = "validations", ignore = true)
    Personnel toEntity(PersonnelDto personnelDto);

    /**
     * Convertit une liste de Personnel en liste de PersonnelDto
     *
     * @param personnels liste d'entités
     * @return liste de DTOs
     */
    List<PersonnelDto> toDtoList(List<Personnel> personnels);

    /**
     * Méthode personnalisée pour convertir RolePersonnel enum en String
     */
    @Named("roleToString")
    default String roleToString(RolePersonnel role) {
        return role != null ? role.name() : null;
    }

    /**
     * Méthode personnalisée pour convertir String en RolePersonnel enum
     */
    @Named("stringToRole")
    default RolePersonnel stringToRole(String role) {
        return role != null ? RolePersonnel.valueOf(role) : null;
    }
}