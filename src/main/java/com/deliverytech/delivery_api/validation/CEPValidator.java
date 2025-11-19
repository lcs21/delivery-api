package com.deliverytech.delivery_api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CEPValidator implements ConstraintValidator<ValidCEP, String> {

    private static final Pattern CEP_PATTERN =
            Pattern.compile("^\\d{5}-\\d{3}$");

    @Override
    public void initialize(ValidCEP constraintAnnotation) {
        // Inicialização se necessária
    }

    @Override
    public boolean isValid(String cep, ConstraintValidatorContext context) {
        if (cep == null || cep.trim().isEmpty()) {
            return false;
        }

        String cleanCep = cep.trim().replaceAll("\s", "");
        return CEP_PATTERN.matcher(cleanCep).matches();
    }
}
