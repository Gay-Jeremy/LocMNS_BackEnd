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
    protected Integer id;

    @Column(nullable = false,unique = true,length = 50)
    @Size(max = 50)
    @NotBlank
    @JsonView({EmpruntView.class, StatutEmpruntView.class})
    protected String libelle;

    @OneToMany(mappedBy = "statutEmprunt")
    private List<Emprunt> emprunts;
}
