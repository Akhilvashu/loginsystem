package com.springboot.loginpage.controller;


import com.springboot.loginpage.entity.Users;
import com.springboot.loginpage.form.Changepasswordform;
import com.springboot.loginpage.serviceimpl.UserServiceimpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    public UserServiceimpl UService;

    @GetMapping("/login")
    public String login(Model model) {
        Users existingUser =  new Users();
        model.addAttribute("existingUser",existingUser);
        return "index";
    }

    @GetMapping("/registerform")
    public String register(Model model){
        Users newUser = new Users();
        model.addAttribute("newUser", newUser);
        return "register";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute Users user, Model model) {
        String username = user.getUsername();
        String password = user.getPassword();

        boolean userExists = UService.checkUserExists(username);

        if (!userExists) {
            model.addAttribute("userNotFound", true);
            return "login";
        }

        boolean passwordMatch = UService.checkPasswordMatch(username, password);

        if (passwordMatch) {
            model.addAttribute("authenticatedUsername", username);
            return "login"; // Stay on the login page
        } else {
            model.addAttribute("authenticationFailed", true);
            return "login";
        }
    }

@PostMapping("/saveAccount")
    public String saveUser(@ModelAttribute Users user, Model model) {
        String newUsername = user.getUsername();

        boolean errorUsernameTaken = UService.isUsernameTaken(newUsername);

        if (errorUsernameTaken) {
            model.addAttribute("errorUsernameTaken", true);
            return "register"; // Change this to your actual registration form view name
        }

        UService.saveUser(user);
        return "redirect:/login";
    }

    @GetMapping("/deleteAccount/{id}")
    public String deleteAccount(@PathVariable(value = "id") Long id) {
        UService.deleteUser(id);
        return "redirect:/login";
    }
    @PostMapping("/changeusername/{id}")
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
            return "redirect:/profile/" + id;
        } else {
            model.addAttribute("errorOldUsernameSame", errorOldUsernameSame);
            model.addAttribute("errorUsernameTaken", errorUsernameTaken);
            return "update_username"; // Change this to your actual view name
        }
    }


    @GetMapping("/passwordform")
    public String passform(Model model){
        Changepasswordform passform = new Changepasswordform();
        model.addAttribute("cpassform",passform);
        return "update_password";
    }


    @PostMapping("/changepassword/{id}")
    public String changepassword(@PathVariable(value="id") Long id,
                                 @RequestParam String oldpassword ,
                                 @RequestParam String password,
                                 @RequestParam String confirm_password,
                                 Model model){
        Users user = UService.findaccount(id);
        String old_password=user.getPassword();

        boolean errorOldPassword = !old_password.equals(oldpassword);
        boolean errorPasswordMismatch = !password.equals(confirm_password);

        if (password==confirm_password && oldpassword==old_password) {
        user.setPassword(password);
        UService.saveUser(user);
        return "redirect;/login";
    } else {
            model.addAttribute("errorOldPassword", errorOldPassword);
            model.addAttribute("errorPasswordMismatch", errorPasswordMismatch);
            return "update_password";
        }
    }
}