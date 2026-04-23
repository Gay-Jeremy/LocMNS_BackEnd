package com.locmns.locmns_backend.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.locmns.locmns_backend.view.EmpruntView;
import com.locmns.locmns_backend.view.MaterielView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Emprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({EmpruntView.class, MaterielView.class})
    protected Integer id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    @JsonView({EmpruntView.class, MaterielView.class})
    protected LocalDate dateCreation;

    @Column(nullable = false)
    @NotNull
    @JsonView(EmpruntView.class)
    protected LocalDate dateDebut;

    @Column(nullable = false)
    @NotNull
    @JsonView(EmpruntView.class)
    protected LocalDate dateFin;


    protected LocalDate dateRetourReel;


    protected LocalDate dateProlongation;


    protected LocalDate dateRetourAnticipe;

    @ManyToOne
    @NotNull(message = "L'utilisateur est obligatoire")
    @JoinColumn(name = "utilisateur_id")
    @JsonView(EmpruntView.class)
    private Utilisateur utilisateur;

    @ManyToOne
    @NotNull(message = "Le statut est obligatoire")
    @JoinColumn(name = "statut_emprunt_id")
    @JsonView(EmpruntView.class)
    private StatutEmprunt statutEmprunt;

    @ManyToOne
    @NotNull(message = "Le matériel est obligatoire")
    @JoinColumn(name = "materiel_id")
    @JsonView(EmpruntView.class)
    private Materiel materiel;

}
