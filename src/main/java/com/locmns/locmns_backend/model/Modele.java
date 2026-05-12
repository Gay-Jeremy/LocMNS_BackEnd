package com.locmns.locmns_backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.view.EmpruntView;
import com.locmns.locmns_backend.view.MaterielView;
import com.locmns.locmns_backend.view.CategorieModeleView;
import com.locmns.locmns_backend.view.ModeleView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

public class Modele {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({CategorieModeleView.class, MaterielView.class, ModeleView.class})
    private Integer id;

    @NotBlank(message = "Le libellé est obligatoire")
    @Column(nullable = false,unique = true,length = 50)
    @Size(min = 2, max = 50, message = "Le libellé doit faire entre 2 et 50 caractères")
    @JsonView({EmpruntView.class, MaterielView.class, CategorieModeleView.class, ModeleView.class})
    private String libelle;

    @ManyToOne
    @NotNull(message = "La catégorie du modèle est obligatoire")
    @JoinColumn(name = "categorie_modele_id")
    @JsonView({MaterielView.class, EmpruntView.class, ModeleView.class})
    private CategorieModele categorieModele;

    @OneToMany(mappedBy = "modele")
    private List<Materiel> materiels;

    @ManyToMany
    @JoinTable(
            name = "documentation_modele",
            joinColumns = @JoinColumn(name = "modele_id"),
            inverseJoinColumns = @JoinColumn(name = "documentation_id")
    )
    private List<Documentation> documentations;

}
