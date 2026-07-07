package com.locmns.locmns_backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 50)
    @Size( max = 50, message = "Le nom ne doit pas dépasser 50 caractères")
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @Column(nullable = false, length = 50)
    @Size(max = 50, message = "Le prénom ne doit pas dépasser 50 caractères")
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Column(nullable = false, unique = true, length = 100)
    @Size(max = 100, message = "L'email' ne doit pas dépasser 50 caractères")
    @Email(message = "L'email n'a pas un format correct")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @Column(length = 255)
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "categorie_utilisateur_id")
    private CategorieUtilisateur categorieUtilisateur;

    @OneToMany(mappedBy = "utilisateur")
    private List<Emprunt> emprunts;
}

