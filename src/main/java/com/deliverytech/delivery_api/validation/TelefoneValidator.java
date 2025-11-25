package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;

public class TelefoneValidator implements ConstraintValidator<ValidTelefone, String> {

    @Override
    public void initialize(ValidTelefone constraintAnnotation) {

    }

    @Override
    public boolean isValid(String telefone, jakarta.validation.ConstraintValidatorContext context) {
        return telefone != null && telefone.matches("\\(\\d{2}\\) \\d{4,5}-\\d{4}");
    }

}