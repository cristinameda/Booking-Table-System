package com.nagarro.af.bookingtablesystem.annotation;

import com.nagarro.af.bookingtablesystem.annotation.validator.UniqueUsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUsernameValidator.class)
public @interface UniqueUsername {

    String message() default "username must be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
