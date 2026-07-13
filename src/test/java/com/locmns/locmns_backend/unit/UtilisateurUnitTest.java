package com.locmns.locmns_backend.unit;

import com.locmns.locmns_backend.TestUtils;
import com.locmns.locmns_backend.dto.request.UtilisateurRequestDto;
import com.locmns.locmns_backend.dto.validation.ValidationGroups;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilisateurUnitTest {

    protected static Validator validator;

    @BeforeAll
    public static void init() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    // ── NOM ──────────────────────────────────────────────

    @Test
    public void creerUtilisateurAvecNomVide_devrait_EtreInvalide() {
        UtilisateurRequestDto utilisateur = new UtilisateurRequestDto(
                "", "Prenom", "test@example.com", "password", null, 1
        );

        Set<ConstraintViolation<Object>> violations =
                validator.validate(utilisateur, ValidationGroups.OnCreate.class);

        assertTrue(
                TestUtils.constraintViolationExist(violations, "nom", "NotBlank"),
                "La contrainte NotBlank sur nom n'a pas fonctionné"
        );
    }

    // ── PRÉNOM ───────────────────────────────────────────

    @Test
    public void creerUtilisateurAvecPrenomVide_devrait_EtreInvalide() {
        UtilisateurRequestDto utilisateur = new UtilisateurRequestDto(
                "Nom", "", "test@example.com", "password", null, 1
        );

        Set<ConstraintViolation<Object>> violations =
                validator.validate(utilisateur, ValidationGroups.OnCreate.class);

        assertTrue(
                TestUtils.constraintViolationExist(violations, "prenom", "NotBlank"),
                "La contrainte NotBlank sur prenom n'a pas fonctionné"
        );
    }

    // ── EMAIL ────────────────────────────────────────────

    @Test
    public void creerUtilisateurAvecEmailVide_devrait_EtreInvalide() {
        UtilisateurRequestDto utilisateur = new UtilisateurRequestDto(
                "Nom", "Prenom", "", "password", null, 1
        );

        Set<ConstraintViolation<Object>> violations =
                validator.validate(utilisateur, ValidationGroups.OnCreate.class);

        assertTrue(
                TestUtils.constraintViolationExist(violations, "email", "NotBlank"),
                "La contrainte NotBlank sur email n'a pas fonctionné"
        );
    }

    @Test
    public void creerUtilisateurAvecEmailMalFormate_devrait_EtreInvalide() {
        UtilisateurRequestDto utilisateur = new UtilisateurRequestDto(
                "Nom", "Prenom", "PasUnEmail", "password", null, 1
        );

        Set<ConstraintViolation<Object>> violations =
                validator.validate(utilisateur, ValidationGroups.OnCreate.class);

        assertTrue(
                TestUtils.constraintViolationExist(violations, "email", "Email"),
                "La contrainte Email sur email n'a pas fonctionné"
        );
    }

    @Test
    public void creerUtilisateurAvecEmailTropLong_devrait_EtreInvalide() {
        UtilisateurRequestDto utilisateur = new UtilisateurRequestDto(
                "Nom", "Prenom", "a".repeat(92) + "@test.com", "password", null, 1
        );

        Set<ConstraintViolation<Object>> violations =
                validator.validate(utilisateur, ValidationGroups.OnCreate.class);

        assertTrue(
                TestUtils.constraintViolationExist(violations, "email", "Size"),
                "La contrainte Size sur email n'a pas fonctionné"
        );
    }
}
