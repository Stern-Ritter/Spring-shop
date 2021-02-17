package com.geekbrains.geekspring.services;

import com.geekbrains.geekspring.entities.SystemUser;
import com.geekbrains.geekspring.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    User findByUserName(String userName);

    void save(SystemUser systemUser);

    List<User> findAll();

    Optional<User> findById(Long id);

    void save(User user);

    void deleteById(Long id);
}
