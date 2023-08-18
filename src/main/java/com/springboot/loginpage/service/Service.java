package com.springboot.loginpage.service;

import com.springboot.loginpage.entity.Users;

public interface Service {

    void saveUser(Users User);
    void deleteUser(Long Id);
    Users findaccount(Long id);
    boolean isUsernameTaken(String username);
    public Users finduserbyusername(String username);
}
