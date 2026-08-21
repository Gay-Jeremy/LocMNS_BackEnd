package com.locmns.locmns_backend.model;

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
    private Integer id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDate dateCreation;

    @Column(nullable = false)
    @NotNull
    private LocalDate dateDebut;

    @Column(nullable = false)
    @NotNull
    private LocalDate dateFin;


    private LocalDate dateRetourReel;


    private LocalDate dateProlongation;

    private LocalDate dateRetourAnticipe;

    private String note;

    @ManyToOne
    @NotNull(message = "L'utilisateur est obligatoire")
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @ManyToOne
    @NotNull(message = "Le statut est obligatoire")
    @JoinColumn(name = "statut_emprunt_id", nullable = false)
    private StatutEmprunt statutEmprunt;

    @ManyToOne
    @NotNull(message = "Le matériel est obligatoire")
    @JoinColumn(name = "materiel_id", nullable = false)
    private Materiel materiel;



}
