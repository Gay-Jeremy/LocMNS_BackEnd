package com.locmns.locmns_backend.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Materiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 50)
    private String numSerie;

    @OneToMany(mappedBy = "materiel")
    private List<Emprunt> emprunts;

    @ManyToOne
    @JoinColumn(name = "statut_materiel_id")
    private StatutMateriel statutMateriel;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    @ManyToOne
    @JoinColumn(name = "modele_id")
    private Modele modele;

    @OneToMany(mappedBy = "materiel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CaracteristiqueMaterielComposant> caracteristiques = new ArrayList<>();
}
