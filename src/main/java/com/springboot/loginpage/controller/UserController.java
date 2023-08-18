package com.springboot.loginpage.controller;


import com.springboot.loginpage.entity.Users;
import com.springboot.loginpage.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    public Service UService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


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
        try{
            Users user = UService.finduserbyusername(username);
            if (passwordEncoder.matches(password, user.getPassword())) {
                model.addAttribute("user",user);
                return "dashboard";
            } else {
                model.addAttribute("passworderror",true);
                return "index";
            }
        } catch (NullPointerException e){
            model.addAttribute("usernameerror",true);
            return "index";
        }
    }


    @PostMapping("/saveAccount")
    public String saveUser(@ModelAttribute Users user,@RequestParam String Confirm_password, Model model) {
        String newUsername = user.getUsername();
        model.addAttribute("newUser", user);

        boolean errorUsernameTaken = UService.isUsernameTaken(newUsername);

        if (errorUsernameTaken) {
            model.addAttribute("errorUsernameTaken", true);
            return "register";
        }
        if (user.getPassword().equals(Confirm_password)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            UService.saveUser(user);
            model.addAttribute("accountcreate",true);
            return "index";
        } else {
            model.addAttribute("newandconfirmerror",true);
            return "register";
        }
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
        boolean usernameTaken = UService.isUsernameTaken(newusername);
        model.addAttribute("user",user);

        if (!usernameTaken) {
            user.setUsername(newusername);
            UService.saveUser(user);
            model.addAttribute("usernameChanged", true); // Set the attribute
            return "dashboard";
        } else {
            model.addAttribute("errorUsernameTaken", true);
            return "dashboard";
        }
    }



    @GetMapping("/passwordform/{id}")
    public String passform(@PathVariable(value="id")Long id,Model model){
        Users user = UService.findaccount(id);
        model.addAttribute("user", user);
        return "update_password";}



    @PostMapping("/passwordform/{id}")
    public String changepassword(@PathVariable(value="id") Long id,
                                 @RequestParam String old_password ,
                                 @RequestParam String new_password,
                                 @RequestParam String confirm_password,
                                 Model model){
        Users user = UService.findaccount(id);
        String oldpassword = user.getPassword();
        model.addAttribute("user",user);

        if (passwordEncoder.matches(old_password, oldpassword)) {
            if (!new_password.equals(old_password)) {
                if (new_password.equals(confirm_password)){
                    user.setPassword(passwordEncoder.encode(new_password));
                    UService.saveUser(user);
                    return "index";
                } else {
                    model.addAttribute("wrongconfirmpassword", true);
                    return "update_password";
                }
            } else {
                    model.addAttribute("oldandnewmatch",true);
                    return "update_password";
            }
        } else {
            model.addAttribute("wrongoldpassword",true);
            return "update_password";
        }
    }
}