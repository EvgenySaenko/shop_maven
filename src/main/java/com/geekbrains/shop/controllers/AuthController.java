package com.geekbrains.shop.controllers;


import com.geekbrains.shop.configs.JwtTokenUtil;
import com.geekbrains.shop.entities.dtos.JwtRequest;
import com.geekbrains.shop.entities.dtos.JwtResponse;
import com.geekbrains.shop.exceptions.JulyMarketError;
import com.geekbrains.shop.services.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {//эта часть для получения токена
    private final UsersService usersService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UsersService usersService,
                          JwtTokenUtil jwtTokenUtil,
                          AuthenticationManager authenticationManager) {
        this.usersService = usersService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/auth")//ожидаем по этому адресу пост запрос, вытягиваем JSON object
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) throws Exception {
        try {//пытаемся пройти аутентификацию с этим юзернейм и паролем
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException ex) {//если данные не коректны, вернем статус код(неавторизован и сообщение)
            return new ResponseEntity<>(new JulyMarketError(HttpStatus.UNAUTHORIZED.value(), "Incorrect username or password"), HttpStatus.UNAUTHORIZED);
        } //если все хорошо прошло -  получаем инфу о пользователе
        UserDetails userDetails = usersService.loadUserByUsername(authRequest.getUsername());
        //ее отдаем в jwtTokenUtil (который дергает все данные и формирует токен)
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));//и возвращаем токен в виде респонса
    }
}
