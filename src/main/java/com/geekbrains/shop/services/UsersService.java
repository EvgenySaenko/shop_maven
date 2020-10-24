package com.geekbrains.shop.services;


import com.geekbrains.shop.entities.Role;
import com.geekbrains.shop.entities.User;
import com.geekbrains.shop.entities.dtos.SystemUser;
import com.geekbrains.shop.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsersService implements UserDetailsService {
    private UsersRepository usersRepository;
    private RolesService rolesService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Autowired
    public void setRolesService(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    public Optional<User> findByPhone(String phone) {
        return usersRepository.findOneByPhone(phone);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = usersRepository.findOneByPhone(username).orElseThrow(() -> new UsernameNotFoundException("Invalid username or password"));
        return new org.springframework.security.core.userdetails.User(user.getPhone(), user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    @Transactional//когда на фронтенде вбивают данные
    public User save(SystemUser systemUser) {//мы получаем систем юзера
        User user = new User();//создаем нашего юзера
        findByPhone(systemUser.getPhone()).ifPresent((u) -> {//проверяем если такой юзер существует => кидаем ексепшен
            throw new RuntimeException("User with phone " + systemUser.getPhone() + " is already exist");
        });
        //если такого нет то начинаем создавать нового пользователя(в рамках транзакции- чтобы паралельно никто не создал)
        user.setPhone(systemUser.getPhone());//данные из которых собираем нового юзера - из формы
        user.setPassword(passwordEncoder.encode(systemUser.getPassword()));//пароль хешируем и записываем
        user.setFirstName(systemUser.getFirstName());
        user.setLastName(systemUser.getLastName());
        user.setEmail(systemUser.getEmail());
        user.setRoles(Arrays.asList(rolesService.findByName("ROLE_CUSTOMER")));//пока все авторизуемые - получают роль CUSTOMER
        return usersRepository.save(user);//сохраняем юзера и возвращаем
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}