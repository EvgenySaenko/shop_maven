package com.geekbrains.shop.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//означает что я аннотация -  класс который занимается валидацией FieldMatchValidator.class
@Constraint(validatedBy = FieldMatchValidator.class)
//эту аннотацию можно будет вешать на классы
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)//работать будет в рантайме
public @interface FieldMatch {
    String message() default "";//дефолтное сообщение пустое
    Class<?>[] groups() default {};//никаких групп нет
    Class<? extends Payload>[] payload() default {};//полезной нагрузки нет
    String first();//нужны лиш два поля чтобы проверить их равенство
    String second();
}