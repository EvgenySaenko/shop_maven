package com.geekbrains.shop.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String secret;
    //можем получить всю инфу из токена
    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    //можем отдать токен ввиде строки и получить имя пользователя
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //    private Date getExpirationDateFromToken(String token) {//в фильтре стоит уже валидация - выключаем пока нам не нужно
//        return getClaimFromToken(token, Claims::getExpiration);
//    }
//
//    public boolean validateToken(String token) {
//        return !isTokenExpired(token);
//    }
//
//    public boolean validateToken(String token, UserDetails userDetails) {
//        String username = getUsernameFromToken(token);
//        return Objects.equals(username, userDetails.getUsername()) && !isTokenExpired(token);
//    }
//мы можем сгенерить токен по юзердетейлсом
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("role", rolesList);
        return doGenerateToken(claims, userDetails.getUsername());
    }
    //создание токена из списка элементов,создаем что то вроде пачки ключа значения и накидываем туда текущее время,
    // когда был создан токен, когда его время жизни закончится
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() - 60 * 60 * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issuedDate)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    //инфа о юзере
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
//
//    private boolean isTokenExpired(String token) {
//        Date date = getExpirationDateFromToken(token);
//        return date != null && date.before(new Date());
//    }
}
