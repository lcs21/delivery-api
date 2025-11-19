package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TelefoneValidator implements ConstraintValidator<ValidTelefone, String> {

    @Override
    public void initialize(ValidTelefone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        return telefone != null && !telefone.trim().isEmpty();
    }
}