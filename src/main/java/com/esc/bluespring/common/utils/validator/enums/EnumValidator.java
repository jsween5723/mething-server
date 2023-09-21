package com.esc.bluespring.common.utils.validator.enums;

import static java.lang.String.format;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<EnumValid, Enum<?>> {

  List<Object> validList;

  private static final String VALID_ENUM_TEMPLATE = "Invalid Input for Code Field of Enum: Should be in %s";

  @Override
  public void initialize(EnumValid constraint) {
    validList = Arrays
        .stream(constraint.target().getEnumConstants())
        .map(Enum::name)
        .collect(Collectors.toList());
  }

  @Override
  public boolean isValid(Enum value, ConstraintValidatorContext context) {
    boolean isValid = value != null && validList.contains(value.name());
    if(!isValid) {
      context
          .buildConstraintViolationWithTemplate(
              format(VALID_ENUM_TEMPLATE, validList)
          )
          .addConstraintViolation()
          .disableDefaultConstraintViolation();
    }
    return isValid;
  }

}
