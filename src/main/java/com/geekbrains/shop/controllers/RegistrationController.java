package com.geekbrains.shop.controllers;


import com.geekbrains.shop.entities.User;
import com.geekbrains.shop.entities.dtos.SystemUser;
import com.geekbrains.shop.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class RegistrationController {
    private UsersService usersService;

    @Autowired
    public void setUserService(UsersService usersService) {
        this.usersService = usersService;
    }

    @InitBinder
    //обработчик данных, когда в этот контроллер приходят строки в качестве аргументов(составных частей объектов)
    //он считает все пробелы по краям как несуществующие - и обрубает их
    //то есть не смогут региться с телефоном(5 цифр и 3 пробела) он обрубит пробелы и скажет давай еще 3 символа
    //@NotBlank такое пропустит так как (она проверяет лишь что строка не пустая, длиною > 0 и там не пробелы)
    //поэтому используем биндер
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/register")
    public String showMyLoginPage(Model model) {
        model.addAttribute("systemUser", new SystemUser());
        return "registration-form";
    }

    @PostMapping("/register/process")//указываем объект которому нужно провести валидацию,и обязательно указвыаем BindingResult(именно расположить сразу за ним)
    public String processRegistrationForm(@ModelAttribute("systemUser") @Validated SystemUser systemUser, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {//если в bindingResult есть какие то ошибки
            return "registration-form";//возвращаем форму и показываем их(например короткий пароль итд)
            //Spring умеет из bindingResult закидывать ошибки в модель
        }
        //ищем пользователя с таким номером телефона
        Optional<User> existing = usersService.findByPhone(systemUser.getPhone());
        if (existing.isPresent()) {//если такой уже есть
            //кладем ошибку в модель на фронтенд - указываем номер телефона - который уже есть
            model.addAttribute("registrationError", "User with phone number: [" + systemUser.getPhone() + "] is already exist");
            systemUser.setPhone(null);//зануляем это поле
            //юзера в модель, чтобы сохранить данные которые вбивались(чтобы чел не вводил по 100 раз одно и тоже)
            model.addAttribute("systemUser", systemUser);
            return "registration-form";//и показываем форму с ошибками
        }
        //сохраняем если не нашли пользователя с такми номером
        usersService.save(systemUser);
        return "registration-confirmation";//показываем страницу с успешной регистрацией
    }
}
