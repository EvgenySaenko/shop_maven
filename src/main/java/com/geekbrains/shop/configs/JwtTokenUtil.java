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
    private final String secret;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    //можем получить всю инфу из токена
    private  <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(token);//Claims - это вся инфа из токена
        return claimsResolver.apply(claims);
    }
    //можем отдать токен ввиде строки и получить имя пользователя
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }
    //можем узнать время жизни токена
    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    //можем проверить токен
    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        //проверяем что имя пользователя в токене = именю пользователя которого достали из базы, и проверяем что его срок жизни не вышел
        return Objects.equals(username, userDetails.getUsername()) && !isTokenExpired(token);
    }
    //мы можем сгенерить токен по юзердетейлсом
    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList = userDetails.getAuthorities().stream()//зашили список ролей
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("username", userDetails.getUsername());//зашили юзернайм
        claims.put("role", rolesList);//в токен зашиваем -Роли
        return doGenerateToken(claims, userDetails.getUsername());
    }
    //создание токена из списка элементов,создаем что то вроде пачки ключа значения и накидываем туда текущее время,
    // когда был создан токен, когда его время жизни закончится
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + 60 * 60 * 1000);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(expiredDate)
                .setExpiration(expiredDate)
                //когда подписываем токен указываем алгоритм хэширования и указываем сикрет который инжектится сюда из настроек
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }
    //получение из токена всего состав-го
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        Date date = getExpirationDateFromToken(token);
        return date != null && date.before(new Date());
    }
}
