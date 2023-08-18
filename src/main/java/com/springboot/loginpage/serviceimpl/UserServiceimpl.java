package com.springboot.loginpage.serviceimpl;

import com.springboot.loginpage.entity.Users;
import com.springboot.loginpage.repository.UserRepository;
import com.springboot.loginpage.service.Service;
import org.springframework.beans.factory.annotation.Autowired;


@org.springframework.stereotype.Service
public class UserServiceimpl implements Service {

    @Autowired
    private UserRepository Repo;

    @Override
    public void saveUser(Users User) {
       Repo.save(User);
    }

    @Override
    public void deleteUser(Long Id) {
        Repo.deleteById(Id);
    }

    @Override
    public Users findaccount(Long id){
        return Repo.findById(id).get();
    }

    @Override
    public Users finduserbyusername(String username){
        return Repo.findByUsername(username);
    }
    @Override
    public boolean isUsernameTaken(String username){
        return Repo.existsByUsername(username);
    }
    }

