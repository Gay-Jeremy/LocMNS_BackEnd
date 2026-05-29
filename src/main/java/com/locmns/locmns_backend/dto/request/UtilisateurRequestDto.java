package com.locmns.locmns_backend.dto.request;

import com.locmns.locmns_backend.model.Utilisateur;
import jakarta.validation.constraints.*;

public record UtilisateurRequestDto(

        @NotBlank(groups = {Utilisateur.OnCreate.class}, message = "Le nom est obligatoire")
        @Size(min = 2, max = 50, groups = {Utilisateur.OnCreate.class, Utilisateur.OnUpdate.class}, message = "Le nom doit faire entre 2 et 50 caractères")
        String nom,

        @NotBlank(groups = {Utilisateur.OnCreate.class}, message = "Le prénom est obligatoire")
        @Size(min = 2, max = 50, groups = {Utilisateur.OnCreate.class, Utilisateur.OnUpdate.class}, message = "Le prénom doit faire entre 2 et 50 caractères")
        String prenom,

        @NotBlank(groups = {Utilisateur.OnCreate.class}, message = "L'email est obligatoire")
        @Size(max = 100, groups = {Utilisateur.OnCreate.class}, message = "L'email ne doit pas dépasser 100 caractères")
        @Email(groups = {Utilisateur.OnCreate.class}, message = "L'email est mal formé")
        String email,

        @NotBlank(groups = {Utilisateur.OnCreate.class}, message = "Le mot de passe est obligatoire")
        String password,

        Integer promotionId,

        @NotNull(groups = {Utilisateur.OnCreate.class, Utilisateur.OnUpdate.class}, message = "La catégorie utilisateur est obligatoire")
        Integer categorieUtilisateurId

) {}