package com.locmns.locmns_backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.view.DroitEmpruntView;
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
    @JsonView(DroitEmpruntView.class)
    private Integer categorieUtilisateurId;

    @Id
    @JsonView(DroitEmpruntView.class)
    private Integer categorieModeleId;

    @JsonView(DroitEmpruntView.class)
    private boolean peutEmprunter;

    @ManyToOne
    @JoinColumn(name = "categorie_utilisateur_id", insertable = false, updatable = false )
    @JsonView(DroitEmpruntView.class)
    private CategorieUtilisateur categorieUtilisateur ;

    @ManyToOne
    @JoinColumn (name = "categorie_Modele_id", insertable = false, updatable = false )
    @JsonView(DroitEmpruntView.class)
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
