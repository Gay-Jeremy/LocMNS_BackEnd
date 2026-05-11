package com.locmns.locmns_backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.view.EmpruntView;
import com.locmns.locmns_backend.view.StatutEmpruntView;
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
public class StatutEmprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({EmpruntView.class, StatutEmpruntView.class})
    private Integer id;

    @NotBlank(message = "Le libellé est obligatoire")
    @Column(nullable = false,unique = true,length = 50)
    @Size(min = 2, max = 50, message = "Le libellé doit faire entre 2 et 50 caractères")
    @JsonView({EmpruntView.class, StatutEmpruntView.class})
    private String libelle;

    @Column(nullable = false, unique = true, updatable = false)
    private String code;

    @OneToMany(mappedBy = "statutEmprunt")
    private List<Emprunt> emprunts;
}
