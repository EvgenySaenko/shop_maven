package com.geekbrains.shop.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
    @ExceptionHandler//если где то возник ProductNotFoundException
    public ResponseEntity<?> handleRNFException(ProductNotFoundException e) {
        log.error(e.getMessage());
        //то мы формируем по нему ошибку ,защиваем статус туда нот фаунд и сообщение из эксепшена
        JulyMarketError err = new JulyMarketError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }//то есть теперь мы можем в любом месте приложения бросать ProductNotFoundException
    //и если такое исключание вылетело и мы его не перехватили
    //оно будет перехвачено этим ExceptionControllerAdvice оно обернется в JSOn и отправится клиенту
}
