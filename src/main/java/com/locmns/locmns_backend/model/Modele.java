package com.locmns.locmns_backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.view.EmpruntView;
import com.locmns.locmns_backend.view.MaterielView;
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

public class Modele {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(nullable = false,unique = true,length = 50)
    @Size(max = 50)
    @NotBlank
    @JsonView({EmpruntView.class, MaterielView.class})
    protected String libelle;

    @ManyToOne
    @JoinColumn(name = "categorie_modele_id")
    private CategorieModele categorieModele;

    @OneToMany(mappedBy = "modele")
    private List<Materiel> materiels;

}
