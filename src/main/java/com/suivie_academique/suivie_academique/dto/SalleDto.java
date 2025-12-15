package com.suivie_academique.suivie_academique.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) pour l'entité Salle
 * Utilisé pour le transfert de données entre les couches de l'application
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalleDto {

    // Code unique de la salle
    private String codeSalle;

    // Description de la salle
    private String description;

    // Capacité d'accueil de la salle
    private int contenance;

    // Statut de la salle (OCCUPE, LIBRE, FERMER)
    private String statusSalle;
}