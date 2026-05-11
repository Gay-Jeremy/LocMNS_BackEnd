package com.locmns.locmns_backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.view.EmpruntView;
import com.locmns.locmns_backend.view.PromotionView;
import com.locmns.locmns_backend.view.UtilisateurView;
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

public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({PromotionView.class,UtilisateurView.class})
    private Integer id;

    @NotBlank(message = "Le libellé est obligatoire")
    @Column(nullable = false,unique = true,length = 50)
    @Size(min = 2, max = 50, message = "Le libellé doit faire entre 2 et 50 caractères")
    @JsonView({EmpruntView.class, PromotionView.class, UtilisateurView.class})
    private String libelle;

    // Une promotion contient plusieurs utilisateurs
    @OneToMany(mappedBy = "promotion")
    @JsonView(PromotionView.class)
    private List<Utilisateur> utilisateurs;

}
