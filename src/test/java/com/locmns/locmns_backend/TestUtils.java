package com.locmns.locmns_backend;

import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class TestUtils {

    public static boolean constraintViolationExist(
            Set<ConstraintViolation<Object>> constraintsViolations,
            String fieldName,
            String annotationName
    ) {
        return constraintsViolations.stream()
                .anyMatch(contrainte -> {
                    String champ = contrainte.getPropertyPath().toString();
                    String annotation = contrainte
                            .getConstraintDescriptor()
                            .getAnnotation()
                            .annotationType()
                            .getSimpleName();
                    return champ.equals(fieldName) && annotation.equals(annotationName);
                });
    }
}