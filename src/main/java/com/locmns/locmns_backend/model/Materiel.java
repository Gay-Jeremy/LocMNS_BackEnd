package com.locmns.locmns_backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.view.EmpruntView;
import com.locmns.locmns_backend.view.MaterielView;
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

public class Materiel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({EmpruntView.class, MaterielView.class})
    protected Integer id;

    @NotBlank(message = "Le numéro de série est obligatoire")
    @Column(nullable = false,unique = true,length = 50)
    @Size(min = 2, max = 100, message = "Le numéro de série doit faire entre 2 et 100 caractères")
    @JsonView({EmpruntView.class, MaterielView.class})
    protected String numSerie;


    @OneToMany(mappedBy = "materiel")
    @JsonView(MaterielView.class)
    private List<Emprunt> emprunts;

    @ManyToOne
    @JoinColumn(name = "statut_materiel_id")
    @JsonView(MaterielView.class)
    private StatutMateriel statutMateriel;

    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

    @ManyToOne
    @NotNull(message = "Le modèle est obligatoire")
    @JoinColumn(name = "modele_id")
    @JsonView({EmpruntView.class, MaterielView.class})
    private Modele modele;
}
