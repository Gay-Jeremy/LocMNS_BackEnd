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

    @Column(nullable = false,unique = true,length = 50)
    @Size(max = 50)
    @NotBlank
    protected String libelle;

    @ManyToMany
    @JoinTable(
            name = "composant_categorieModele",
            joinColumns = @JoinColumn(name = "composant_id"),
            inverseJoinColumns = @JoinColumn(name = "categorie_modele_id")
    )

    private List<CategorieModele> categoriesModeles = new ArrayList<>();


}
