package com.locmns.locmns_backend.dto.request;

import com.locmns.locmns_backend.dto.validation.ValidationGroups;
import jakarta.validation.constraints.*;

public record UtilisateurRequestDto(

        @NotBlank(groups = {ValidationGroups.OnCreate.class}, message = "Le nom est obligatoire")
        @Size( max = 50, groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class}, message = "Le nom ne doit pas faire plus de 50 caractères")
        String nom,

        @NotBlank(groups = {ValidationGroups.OnCreate.class}, message = "Le prénom est obligatoire")
        @Size( max = 50, groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class}, message = "Le prénom ne doit pas faire plus de 50 caractères")
        String prenom,

        @NotBlank(groups = {ValidationGroups.OnCreate.class}, message = "L'email est obligatoire")
        @Size(max = 100, groups = {ValidationGroups.OnCreate.class}, message = "L'email ne doit pas dépasser 100 caractères")
        @Email(groups = {ValidationGroups.OnCreate.class}, message = "L'email est mal formé")
        String email,

        @NotBlank(groups = {ValidationGroups.OnCreate.class}, message = "Le mot de passe est obligatoire")
        String password,

        Integer promotionId,

        @NotNull(groups = {ValidationGroups.OnCreate.class, ValidationGroups.OnUpdate.class}, message = "La catégorie utilisateur est obligatoire")
        Integer categorieUtilisateurId

) {}
