package com.geekbrains.shop.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component//формируем фильр/наследуемся от готового фильтра
//этот фильтр служит для того чтобы понять , пользователь который запрос послал
//прислал ли он нам токен?
public class JwtRequestFilter extends OncePerRequestFilter {
    private UserDetailsService userDetailsService;
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setJwtTokenUtil(JwtTokenUtil jwtTokenUtil) {
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        // Authorization Bearer h4iuhf38hg483ht834utj438jf348hf
        String username = null;
        String jwt = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {//обрезаем первые 7 символов -оставляем токен
            jwt = authHeader.substring(7);
            try {//достаем инфу о пользователе потому что знаем что она вшита в токен
                username = jwtTokenUtil.getUsernameFromToken(jwt);//если мы вытащили юзернейм
            } catch (Exception ex) {
                System.out.println("Token is invalid: " + ex.getMessage());
            }
        }
        //проверяем что юзернайм такой существует,проверяем что контекст пустой(никто не зашел /авторизовался)
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);//достаем инфу о пользователе по имени
            if (jwtTokenUtil.validateToken(jwt, userDetails)) {//проверяем токен(что инфа и те юзерДетейлс совпадают)
                //если все норм - формируем токен
                UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(token);//и закидываем его в контекст
            }
        }

        filterChain.doFilter(request, response);
    }
}
