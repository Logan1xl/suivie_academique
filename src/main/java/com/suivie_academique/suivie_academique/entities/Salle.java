package com.suivie_academique.suivie_academique.entities;

import com.suivie_academique.suivie_academique.utils.SalleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Entité représentant une salle de cours
 * Table: Salle
 */
@Entity
@Table(name = "Salle")
@Getter
@Setter
@NoArgsConstructor
// IMPORTANT: Requis par JPA
@AllArgsConstructor
public class Salle {

    @Id
    @Basic(optional = false)
    private String codeSalle;

    private String descSalle;

    @Basic(optional = false)
    private int contenance;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    private SalleStatus statutSalle;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL)
    private List<Programmation> programmations;
}