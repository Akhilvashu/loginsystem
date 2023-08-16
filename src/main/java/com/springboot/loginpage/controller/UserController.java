package com.springboot.loginpage.controller;


import com.springboot.loginpage.entity.Users;
import com.springboot.loginpage.form.Changepasswordform;
import com.springboot.loginpage.repository.UserRepository;
import com.springboot.loginpage.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    public Service UService;
    @Autowired
    public UserRepository Repo;

    @GetMapping("/registerform")
    public String register(Model model){
        Users newUser = new Users();
        model.addAttribute("newUser", newUser);
        return "register";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "index";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String username, @RequestParam String password,Model model) {
        Users user = Repo.findByUsername(username);
        if (user != null) {
            if (user.getPassword().equals(password)) {
                model.addAttribute("user",user);
            } else {
                model.addAttribute("passworderror",true);
            }
        } else {
            model.addAttribute("usernameerror",true);
        }
        return "index";
    }


    @PostMapping("/saveAccount")
    public String saveUser(@ModelAttribute Users user, Model model) {
        String newUsername = user.getUsername();

        boolean errorUsernameTaken = UService.isUsernameTaken(newUsername);

        if (errorUsernameTaken) {
            model.addAttribute("errorUsernameTaken", true);
            return "register";
        }

        UService.saveUser(user);
        return "index";
    }

    @GetMapping("/deleteAccount/{id}")
    public String deleteAccount(@PathVariable(value = "id") Long id) {
        UService.deleteUser(id);
        return "index";
    }
    @GetMapping("/changeusername/{id}")
    public String changeUsername(@PathVariable(value="id") Long id,
                                 @RequestParam String newusername,
                                 Model model) {
        Users user = UService.findaccount(id);
        String old_username = user.getUsername();

        boolean errorOldUsernameSame = old_username.equals(newusername);
        boolean errorUsernameTaken = UService.isUsernameTaken(newusername);

        if (!errorOldUsernameSame && !errorUsernameTaken) {
            user.setUsername(newusername);
            UService.saveUser(user);
        } else {
            model.addAttribute("errorOldUsernameSame", errorOldUsernameSame);
            model.addAttribute("errorUsernameTaken", errorUsernameTaken);
        }
        return "index";
    }


    @GetMapping("/passwordform")
    public String passform(Model model){
        Changepasswordform changepass = new Changepasswordform();
        Users user = new Users();
        model.addAttribute("Changepass", changepass);
        model.addAttribute("user",user);
        return "update_password";
    }


    @GetMapping("/changepassword/{id}")
    public String changepassword(@PathVariable(value="id") Long id,
                                 @RequestParam String oldpassword ,
                                 @RequestParam String new_password,
                                 @RequestParam String confirm_password,
                                 Model model){
        Users user = UService.findaccount(id);
        String old_password=user.getPassword();
        if (old_password.equals(oldpassword)){
            if (new_password.equals(confirm_password)){
                user.setPassword(new_password);
                UService.saveUser(user);
                return "index";
            } else {
                model.addAttribute("wrongconfirmpassword",true);
            }
        } else {
            model.addAttribute("wrongoldpassword",true);
        }
        return "index";
    }
}