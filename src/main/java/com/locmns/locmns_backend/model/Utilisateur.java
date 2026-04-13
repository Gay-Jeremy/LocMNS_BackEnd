package com.locmns.locmns_backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.view.CategorieUtilisateurView;
import com.locmns.locmns_backend.view.EmpruntView;
import com.locmns.locmns_backend.view.PromotionView;
import com.locmns.locmns_backend.view.UtilisateurView;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({EmpruntView.class, UtilisateurView.class})
    protected Integer id;

    @JsonView({EmpruntView.class, PromotionView.class, CategorieUtilisateurView.class, UtilisateurView.class})
    protected String nom;

    @JsonView({EmpruntView.class, PromotionView.class,CategorieUtilisateurView.class,UtilisateurView.class})
    protected String prenom;

    protected String email;

    @ManyToOne
    @JoinColumn(name = "promotion_id") // clé étrangère de la table PROMOTION
    @JsonView({EmpruntView.class, UtilisateurView.class, PromotionView.class})
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "categorie_utilisateur_id")
    @JsonView(EmpruntView.class)
    private CategorieUtilisateur categorieUtilisateur;

    @OneToMany(mappedBy = "utilisateur")
    private List<Emprunt> emprunts;

}
