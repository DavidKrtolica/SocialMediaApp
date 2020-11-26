package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    //GLOBAL VARIABLE FOR THE LOGGED-IN USER
    User loggedInUser;

    @GetMapping("/index")
    public String returnIndex() {
        return "/index";
    }

    //TEST METHOD FOR THE PROFILEPAGE.HTML, REMOVE LATER
    @GetMapping("/userpage")
    public String returnProfilePage(Model model) {
        model.addAttribute("loggedInUser", loggedInUser);
        return "/userpage";
    }

    //METHOD FOR LOGIN
    @PostMapping("/index")
    public String indexLogin(WebRequest wr) {
        String email = wr.getParameter("username");
        String password = wr.getParameter("password");

        //JPA REPOSITORY METHOD WHICH RETURNS A LIST OF USERS WITH EMAIL AND PASSWORD PASSED IN THE INDEX.HTML
        List<User> usersByCredentials = userRepository.findUserByEmailAndPasswordContaining(email, password);
        //SET THE LOGGED-IN USER TO A GLOBAL VARIABLE
        loggedInUser = userRepository.findUserByEmailAndPasswordContaining(email, password).get(0);

        if (!usersByCredentials.isEmpty()){
            return "redirect:/userpage";
        } else {
            return "/index";
        }
    }
}
