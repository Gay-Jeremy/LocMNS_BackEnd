package com.locmns.locmns_backend.model;

import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn (name = "materiel_id", insertable = false, updatable = false )
    private Materiel materiel ;

    @ManyToOne
    @JoinColumn (name = "composant_id", insertable = false, updatable = false )
    private Composant composant ;


    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class CleCaracteristique implements Serializable {

        @Column(name = "materiel_id")
        private Integer materielId;

        @Column(name = "composant_id")
        private Integer composantId;

    }
}
