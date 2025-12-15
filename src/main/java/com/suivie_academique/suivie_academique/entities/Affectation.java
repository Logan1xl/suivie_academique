package com.suivie_academique.suivie_academique.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * Entité représentant l'affectation d'un personnel à un cours
 * Utilise une clé primaire composite (AffectationId)
 * Table: Affectation
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Affectation implements Serializable {

    @EmbeddedId
    private AffectationId codeAffectation;

    @MapsId("codePersonnel")
    @JoinColumn(name = "code_personnel", referencedColumnName = "codePersonnel")
    @Basic(optional = false)
    @ManyToOne(optional = false)
    private Personnel personnel;

    @MapsId("codeCours")
    @JoinColumn(name = "code_cours", referencedColumnName = "codeCours")
    @Basic(optional = false)
    @ManyToOne(optional = false)
    private Cours cours;
}