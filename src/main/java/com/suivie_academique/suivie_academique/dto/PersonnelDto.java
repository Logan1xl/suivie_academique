package com.suivie_academique.suivie_academique.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) pour l'entité Personnel
 * N'expose pas le mot de passe pour des raisons de sécurité
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonnelDto {

    // Code unique du personnel
    private String codePersonnel;

    // Nom complet du personnel
    private String nomPersonnel;

    // Login d'authentification
    private String loginPersonnel;

    // Sexe (M/F)
    private String sexePersonnel;

    // Numéro de téléphone
    private String phonePersonnel;

    // Rôle (ENSEIGNANT, RESPONSABLE_ACADEMIQUE, RESPONSABLE_PERSONNEL)
    private String rolePersonnel;
}