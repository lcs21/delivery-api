package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;

public class CategoriaValidator implements ConstraintValidator<ValidCategoria, String> {

    @Override
    public void initialize(ValidCategoria constraintAnnotation) {
        // Initialization logic if needed
    }

    @Override
    public boolean isValid(String categoria, jakarta.validation.ConstraintValidatorContext context) {
        // Implement the validation logic for categoria here
        // For example, check if the categoria is not null and not empty
        return categoria != null && !categoria.trim().isEmpty();
    }

}