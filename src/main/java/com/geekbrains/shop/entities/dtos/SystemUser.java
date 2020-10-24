package com.geekbrains.shop.entities.dtos;


import com.geekbrains.shop.utils.validation.FieldMatch;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Data
@NoArgsConstructor
//стандартный валидатор не может сравнивать пароли - поэтому используем свой(создаем аннотацию FieldMatch которой будем размечать наши классы /поля)
//указываем два поля которые связаны и их надо проверить равны ли они, если не равны кидаем такое сообщение
@FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must match")
public class SystemUser {
    //для валидации на уровне бэкенда чтобы лишний раз не отправлять кривые запросы в базу
    @NotNull(message = "phone number is required")
    @Size(min = 8, message = "phone number length must be 8")
    private String phone;

    @NotNull(message = "is required")
    @Size(min = 4, message = "password is too short")
    private String password;

    @NotNull(message = "is required")
    @Size(min = 4, message = "password is too short")
    private String matchingPassword;

    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String firstName;

    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String lastName;

    @NotNull(message = "is required")
    @Size(min = 4, message = "is required")
    @Email
    private String email;
}
