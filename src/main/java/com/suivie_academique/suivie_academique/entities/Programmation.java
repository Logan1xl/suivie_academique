package com.suivie_academique.suivie_academique.entities;

import com.suivie_academique.suivie_academique.utils.StatutProgrammation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Entité représentant une programmation de cours
 * Table: Programmation
 *
 * CORRECTION: Ajout de la colonne name pour l'ID
 */
@Entity
@Table(name = "programmation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Programmation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") 
    private Integer id;

    @Basic(optional = false)
    @Column(name = "nb_heure")
    private int nbHeure;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_programmation")
    private Date dateProgrammation;

    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fin_programmation")
    private Date finProgrammation;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_programmation")
    private StatutProgrammation statutProgrammation;

    @ManyToOne
    @JoinColumn(name = "code_salle", referencedColumnName = "codeSalle")
    private Salle salle;

    @ManyToOne
    @JoinColumn(name = "code_cours", referencedColumnName = "codeCours")
    private Cours cours;

    @ManyToOne
    @JoinColumn(name = "code_personnel_prog", referencedColumnName = "codePersonnel")
    private Personnel personnelProg;

    @ManyToOne
    @JoinColumn(name = "code_personnel_val", referencedColumnName = "codePersonnel")
    private Personnel personnelVal;
}