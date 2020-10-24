package com.geekbrains.shop.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@Profile("js")
@Order(90)
public class JsSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("profile/*").authenticated()
                .antMatchers("/orders/*").hasRole("ADMIN")
                .antMatchers("/api/v1/**").authenticated()
                .and()
                .httpBasic()//форму логина используем бейсик
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .csrf().disable()//при работе с REST API у нас на формах нет токенов мы их не генерим и не запрашиваем(поэтому отключаем)
                //используем протокол STATELESS - мы не запоминаем сессию( общения в REST API - сервер не должен создавать сессии)
                //в роли сессии в  REST API используется пользовательская сессия в виде токенов
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
