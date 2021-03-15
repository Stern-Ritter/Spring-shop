package com.geekbrains.springbootproject.entities;

import com.geekbrains.springbootproject.validation.FieldMatch;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@FieldMatch(first = "password", second = "matchingPassword", message = "Пароли должны совпадать")
public class SystemUser {
    @NotNull(message = "Обязательное поле")
    @Size(min = 3, message = "Имя пользователя должно содержать минимум 2 символа")
    private String userName;

    @NotNull(message = "Обязательное поле")
    @Size(min = 8, message = "Длина пароля минимум 8 символов")
    private String password;

    @NotNull(message = "Обязательное поле")
    @Size(min = 8, message = "Длина пароля минимум 8 символов")
    private String matchingPassword;

    @NotNull(message = "Обязательное поле")
    @Size(min = 1, message = "Имя должно содержать минимум 1 символ")
    private String firstName;

    @NotNull(message = "Обязательное поле")
    @Size(min = 1, message = "Фамилия должна содержать минимум 1 символ")
    private String lastName;

    @NotNull(message = "Обязательное поле")
    @Email
    private String email;

    @NotNull(message = "Обязательное поле")
    @Size(min = 7, message = "Номер телефона должен содержать минимум 7 символов")
    private String phone;
}
