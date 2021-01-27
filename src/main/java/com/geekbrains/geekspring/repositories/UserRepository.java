package com.geekbrains.geekspring.repositories;

import com.geekbrains.geekspring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findOneByUserName(String userName);
}
