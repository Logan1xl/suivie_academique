package com.suivie_academique.suivie_academique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * DTO (Data Transfer Object) pour l'entité Programmation
 * Contient les informations simplifiées sur les relations
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgrammationDto {

    // Identifiant de la programmation
    private Integer id;

    // Nombre d'heures de la programmation
    private int nbHeure;

    // Date de début de la programmation
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date dateProgrammation;

    // Date de fin de la programmation
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
    private Date finProgrammation;

    // Statut (PROGRAMMER, VALIDER, NON_VALIDER)
    private String statutProgrammation;

    // Code de la salle
    private String codeSalle;

    // Code du cours
    private String codeCours;

    // Code du personnel qui programme
    private String codePersonnelProg;

    // Code du personnel qui valide (optionnel)
    private String codePersonnelVal;

    // Nom du cours (pour affichage)
    private String nomCours;

    // Nom du personnel programmateur (pour affichage)
    private String nomPersonnelProg;

    // Nom du personnel validateur (pour affichage)
    private String nomPersonnelVal;
}