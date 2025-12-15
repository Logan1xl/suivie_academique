package com.suivie_academique.suivie_academique.entities;

import com.suivie_academique.suivie_academique.utils.RolePersonnel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Entité représentant un membre du personnel
 * Table: Personnel
 */
@Entity
@Table(name = "Personnel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Personnel {

    @Id
    @Basic(optional = false)
    @Column(unique = true)
    private String codePersonnel;

    @Basic(optional = false)
    private String nomPersonnel;

    @Basic(optional = false)
    private String loginPersonnel;

    @Basic(optional = false)
    private String padPersonnel;

    @Basic(optional = false)
    private String sexePersonnel;

    @Basic(optional = false)
    private String phonePersonnel;

    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    private RolePersonnel rolePersonnel;

    @OneToMany(mappedBy = "personnelProg", cascade = CascadeType.ALL)
    private List<Programmation> programmations;

    @OneToMany(mappedBy = "personnelVal", cascade = CascadeType.ALL)
    private List<Programmation> validations;
}