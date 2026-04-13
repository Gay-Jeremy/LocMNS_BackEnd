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

public class TypeDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(nullable = false,unique = true,length = 50)
    @Size(max = 50)
    @NotBlank
    protected String libelle;

    @OneToMany(mappedBy = "typeDocument")
    private List<Documentation> documentations;

}
