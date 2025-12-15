package com.suivie_academique.suivie_academique.dto;

import com.suivie_academique.suivie_academique.entities.Cours;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * DTO (Data Transfer Object) pour l'entité Cours
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CoursDto {

    @Length(min = 1, max = 100)
    // Code unique du cours
    private String codeCours;

    // Libellé/nom du cours
    @Length(min = 1, max = 100)
    private String labelCours;

    // Description détaillée du cours
    private String descCours;

    // Nombre de crédits du cours
    private String nbCreditCours;

    // Nombre d'heures du cours
    private String nbHeureCours;


}