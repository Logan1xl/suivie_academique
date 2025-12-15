package com.suivie_academique.suivie_academique.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Clé primaire composite pour Affectation
 * IMPORTANT: Doit implémenter equals() et hashCode() pour les clés composites
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor  // IMPORTANT: Requis
@AllArgsConstructor
@EqualsAndHashCode  // CRITIQUE: Requis pour les clés composites JPA
public class AffectationId implements Serializable {

    @Basic(optional = false)
    private String codeCours;

    @Basic(optional = false)
    private String codePersonnel;
}