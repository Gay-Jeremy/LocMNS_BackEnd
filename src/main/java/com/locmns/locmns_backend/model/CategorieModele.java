package com.locmns.locmns_backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.view.*;
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

public class CategorieModele {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CategorieModeleView.class, EmpruntView.class, DroitEmpruntView.class, MaterielView.class, ModeleView.class})
    private Integer id;

    @NotBlank(message = "Le libellé est obligatoire")
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "Le libellé doit faire entre 2 et 50 caractères")
    @JsonView({CategorieModeleView.class, EmpruntView.class, DroitEmpruntView.class,MaterielView.class, ModeleView.class})
    private String libelle;

    @OneToMany(mappedBy = "categorieModele")
    @JsonView(CategorieModeleView.class)
    private List<Modele> modeles;

}

