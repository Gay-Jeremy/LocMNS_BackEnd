package com.locmns.locmns_backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.view.CategorieUtilisateurView;
import com.locmns.locmns_backend.view.DroitEmpruntView;
import jakarta.persistence.*;
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

public class CategorieUtilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CategorieUtilisateurView.class, DroitEmpruntView.class})
    private Integer id;

    @NotBlank (message = "Le libellé est obligatoire")
    @Column(nullable = false,unique = true,length = 50)
    @Size(min = 2, max = 50, message = "Le libellé doit faire entre 2 et 50 caractères")
    @JsonView({CategorieUtilisateurView.class, DroitEmpruntView.class})
    private String libelle;

    @OneToMany(mappedBy = "categorieUtilisateur")
    @JsonView(CategorieUtilisateurView.class)
    private List<Utilisateur> utilisateurs;

}

