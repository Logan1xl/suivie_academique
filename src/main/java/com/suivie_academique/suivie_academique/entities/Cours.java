package com.suivie_academique.suivie_academique.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * Entité représentant un cours
 * Table: Cours
 */
@Entity
@Table(name = "Cours")
@Getter
@Setter
@NoArgsConstructor  // IMPORTANT: Requis par JPA
@AllArgsConstructor
public class Cours {

    @Id
    @Basic(optional = false)
    @Column(unique = true)
    @Length(min = 2, max = 100)
    private String codeCours;

    @Basic(optional = false)
    private String labelCours;

    @Basic(optional = false)
    private String descCours;

    @Basic(optional = false)
    private String nbCreditCours;

    @Basic(optional = false)
    private String nbHeureCours;

    @OneToMany(mappedBy = "cours", cascade = CascadeType.ALL)
    private List<Programmation> programmations;
}