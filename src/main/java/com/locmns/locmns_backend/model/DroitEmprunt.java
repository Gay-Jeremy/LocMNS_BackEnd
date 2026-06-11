package com.locmns.locmns_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(DroitEmprunt.CleDroitEmpruntCategorieUtilisateurCategorieModele.class)
public class DroitEmprunt {

    @Id
    private Integer categorieUtilisateurId;

    @Id
    private Integer categorieModeleId;

    private boolean peutEmprunter;

    @ManyToOne
    @JoinColumn(name = "categorie_utilisateur_id", insertable = false, updatable = false )
    private CategorieUtilisateur categorieUtilisateur ;

    @ManyToOne
    @JoinColumn (name = "categorie_Modele_id", insertable = false, updatable = false )
    private CategorieModele categorieModele ;

    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class CleDroitEmpruntCategorieUtilisateurCategorieModele implements Serializable {

        @Column(name = "categorie_utilisateur_id")
        private Integer categorieUtilisateurId;

        @Column(name = "categorie_modele_id")
        private Integer categorieModeleId;
    }
}
