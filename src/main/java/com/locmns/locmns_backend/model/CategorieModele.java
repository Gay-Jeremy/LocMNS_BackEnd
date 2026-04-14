package com.locmns.locmns_backend.model;

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
    protected Integer id;

    @NotBlank(message = "Le libellé est obligatoire")
    @Column(nullable = false, length = 50)
    @Size(min = 2, max = 50, message = "Le libellé doit faire entre 2 et 50 caractères")
    protected String libelle;

    @OneToMany(mappedBy = "categorieModele")
    private List<Modele> modeles;

}

