package com.locmns.locmns_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Documentation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(nullable = false,unique = true,length = 50)
    @Size(max = 50)
    @NotBlank
    protected String titre;

    @Column(unique = true,length = 50)
    @Size(max = 50)
    @NotBlank
    protected String url;

    @Column(nullable = false,length = 50)
    @Size(max = 50)
    @NotBlank
    protected String format;

    @ManyToOne
    @JoinColumn(name = "type_document_id")
    private TypeDocument typeDocument;

    @ManyToMany
    @JoinTable(name = "documentation_modele",joinColumns = @JoinColumn(name = "documentation_id"),inverseJoinColumns = @JoinColumn(name = "modele_id"))
    private List<Modele> modeles = new ArrayList<>();
}
