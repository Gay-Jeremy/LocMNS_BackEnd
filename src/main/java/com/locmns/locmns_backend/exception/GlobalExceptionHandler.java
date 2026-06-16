package com.locmns.locmns_backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //intercepte les exceptions du à un JSON qui ne respecte pas des contraintes (@NotBlank, @Size...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> constraintViolationInterceptor(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errors;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> constraintViolationDatabase(DataIntegrityViolationException ex) {

        String message = ex.getMostSpecificCause().getMessage();
        if (message != null) {

            if (message.contains("categorie_modele_libelle_key")) {
                return Map.of("message", "Cette catégorie existe déjà");
            }
            if (message.contains("modele_libelle_key")) {
                return Map.of("message", "Ce modèle existe déjà");
            }
            if (message.contains("num_serie")) {
                return Map.of("message", "Ce numéro de série existe déjà");
            }
            if (message.contains("utilisateur_email_key")) {
                return Map.of("message", "Cet email est déjà utilisé");
            }
        }

        return Map.of("message", "Cette valeur existe déjà en base de données");
    }

    @ExceptionHandler(MetierException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> metierExceptionInterceptor(MetierException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(RessourceIntrouvableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> ressourceIntrouvableInterceptor(RessourceIntrouvableException ex) {
        return Map.of("message", ex.getMessage());

    }

    @ExceptionHandler(ConflitReservationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> conflitReservationInterceptor(ConflitReservationException ex) {
        return Map.of("message", ex.getMessage());
    }

    @ExceptionHandler(ValidationMetierException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public Map<String, String> validationMetierInterceptor(ValidationMetierException ex) {
        return Map.of("message", ex.getMessage());
    }
}