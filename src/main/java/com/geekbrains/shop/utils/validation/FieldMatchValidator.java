package com.geekbrains.shop.utils.validation;

import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//имплементим стандартный ConstraintValidator и указываем аннотацию и с какими обьектами работаем
public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    private String firstFieldName;//создаем поля
    private String secondFieldName;
    private String message;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();//вытаскиваем из аннотации и кладем в наши поля
        secondFieldName = constraintAnnotation.second();
        message = constraintAnnotation.message();
    }

    @Override//обычный предикат, имеем ссылку на объект и на контекст
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        boolean valid = true;//по умолчанию говорим что все объекты хорошие
        try {//достаем значения -первого поля(а именно что пользователь указал при регистрации в поле)
            final Object firstObj = new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
            final Object secondObj = new BeanWrapperImpl(value).getPropertyValue(secondFieldName);
            //если первое поле не нулевое то сравниваем// если нуль-смысла нет сравнивать
            valid = firstObj != null && firstObj.equals(secondObj);
        } catch (final Exception ignore) {
        }//если наш объект не валиден прилепливаем сообщение message = "The password fields must match" - указанное в SystemUser
        if (!valid) {//говорим что такое и такое поле должны совпадать
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(firstFieldName)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(secondFieldName)
                    .addConstraintViolation()
                    .disableDefaultConstraintViolation();
        }
        return valid;//возвращаем true or false
    }
}