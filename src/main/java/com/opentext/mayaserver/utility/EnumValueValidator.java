package com.opentext.mayaserver.utility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValueValidator implements ConstraintValidator<CheckValidName, String> {
    private CheckValidName annotation;

    @Override
    public void initialize(CheckValidName annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(String valueForValidation, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = false;
        if (valueForValidation == null || valueForValidation.trim().isEmpty()){
            return false;
        }
        Object[] enumValues = this.annotation.enumClass().getEnumConstants();

        if (enumValues != null) {
            for (Object enumValue : enumValues) {
                if ((this.annotation.ignoreCase() && valueForValidation.equalsIgnoreCase(enumValue.toString())) || valueForValidation.equals(enumValue.toString())) {
                    result = true;
                    break;
                }
            }
        }

        return result;
    }
}