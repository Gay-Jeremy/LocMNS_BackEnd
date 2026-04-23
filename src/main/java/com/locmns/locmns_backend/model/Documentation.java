package com.locmns.locmns_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank(message = "Le titre est obligatoire")
    @Column(nullable = false, length = 100)
    @Size(min = 2, max = 100, message = "Le titre doit faire entre 2 et 100 caractères")
    protected String titre;

    @NotBlank(message = "L'URL est obligatoire")
    @Size(max = 255, message = "L'URL ne doit pas dépasser 255 caractères")
    protected String url;

    @Column(nullable = false,length = 50)
    @Size(max = 50, message = "Le format ne doit pas dépasser 50 caractères")
    protected String format;

    @ManyToOne
    @NotNull(message = "Le type de document est obligatoire")
    @JoinColumn(name = "type_document_id")
    private TypeDocument typeDocument;

    @ManyToMany
    @JoinTable(name = "documentation_modele",joinColumns = @JoinColumn(name = "documentation_id"),inverseJoinColumns = @JoinColumn(name = "modele_id"))
    private List<Modele> modeles = new ArrayList<>();
}
