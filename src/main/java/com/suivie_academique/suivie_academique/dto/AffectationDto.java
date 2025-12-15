package com.suivie_academique.suivie_academique.dto;

import com.suivie_academique.suivie_academique.entities.AffectationId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour l'entité Affectation
 * Contient les codes et noms pour affichage
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffectationDto {

    private AffectationId codeAffectation;

    // Codes pour les relations
    private String codePersonnel;
    private String codeCours;

    // Informations supplémentaires pour l'affichage
    private String nomPersonnel;
    private String nomCours;
}