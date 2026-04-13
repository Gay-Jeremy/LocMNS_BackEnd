package com.locmns.locmns_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(DroitEmpruntCategorieUtilisateurCategorieModele.CleDroitEmpruntCategorieUtilisateurCategorieModele.class)
public class DroitEmpruntCategorieUtilisateurCategorieModele {

    @Id
    private Integer categorieUtilisateurId;

    @Id
    private Integer categorieModeleId;

    private boolean peutEmprunter;

    @ManyToOne
    @MapsId("categorieUtilisateurId" )
    @JoinColumn(name = "categorie_utilisateur_id" )
    private CategorieUtilisateur categorieUtilisateur ;

    @ManyToOne
    @MapsId ("categorieModeleId" )
    @JoinColumn (name = "categorie_Modele_id" )
    private CategorieModele categorieModele ;


    @Embeddable
    public static class CleDroitEmpruntCategorieUtilisateurCategorieModele implements Serializable {

        @Column(name = "categorie_utilisateur_id")
        private Integer categorieUtilisateurId;

        @Column(name = "categorie_modele_id")
        private Integer categorieModeleId;
    }
}
