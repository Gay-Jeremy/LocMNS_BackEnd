package com.locmns.locmns_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

public class Composant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @NotBlank(message = "Le libellé est obligatoire")
    @Column(nullable = false,unique = true,length = 50)
    @Size(min = 2, max = 50, message = "Le libellé doit faire entre 2 et 50 caractères")
    protected String libelle;

    @ManyToMany
    @JoinTable(
            name = "composant_categorieModele",
            joinColumns = @JoinColumn(name = "composant_id"),
            inverseJoinColumns = @JoinColumn(name = "categorie_modele_id")
    )

    private List<CategorieModele> categoriesModeles = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "composant_materiel",
            joinColumns = @JoinColumn(name = "composant_id"),
            inverseJoinColumns = @JoinColumn(name = "materiel_id")
    )
    private List<Materiel> materiels;

}
