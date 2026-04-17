package com.locmns.locmns_backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.view.CategorieUtilisateurView;
import com.locmns.locmns_backend.view.EmpruntView;
import com.locmns.locmns_backend.view.PromotionView;
import com.locmns.locmns_backend.view.UtilisateurView;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    public interface OnUpdate{};
    public interface  OnCreate{};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({EmpruntView.class, UtilisateurView.class})
    protected Integer id;

    @NotBlank(groups = {OnCreate.class},message = "Le nom est obligatoire")
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50,groups = {OnCreate.class}, message = "Le nom doit faire entre 2 et 50 caractères")
    @JsonView({EmpruntView.class, PromotionView.class, CategorieUtilisateurView.class, UtilisateurView.class})
    protected String nom;

    @NotBlank(groups = {OnCreate.class},message = "Le prénom est obligatoire")
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50,groups = {OnCreate.class}, message = "Le prénom doit faire entre 2 et 50 caractères")
    @JsonView({EmpruntView.class, PromotionView.class,CategorieUtilisateurView.class,UtilisateurView.class})
    protected String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Column(nullable = false, unique = true, length = 100)
    @Size(max = 100,groups = {OnCreate.class}, message = "L'email ne doit pas dépasser 100 caractères")
    @Email(groups = {OnCreate.class}, message = "L'email est mal formé")
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
