package com.springboot.loginpage.repository;

import com.springboot.loginpage.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
        boolean existsByUsername(String username);
        Users findByUsername(String username);
    }

