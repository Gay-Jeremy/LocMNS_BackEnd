package com.locmns.locmns_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(CaracteristiqueMaterielComposant.CleCaracteristique.class)
public class CaracteristiqueMaterielComposant {

    @Id
    private Integer materielId;

    @Id
    private Integer composantId;

    private String valeur;

    @ManyToOne
    @MapsId ("materielId" )
    @JoinColumn (name = "materiel_id" )
    private Materiel materiel ;

    @ManyToOne
    @MapsId ("composantId" )
    @JoinColumn (name = "composant_id" )
    private Composant composant ;


    @Embeddable
    public static class CleCaracteristique implements Serializable {

        @Column(name = "materiel_id")
        private Integer materielId;

        @Column(name = "composant_id")
        private Integer composantId;

    }
}
