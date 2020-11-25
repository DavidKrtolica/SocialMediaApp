package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/index")
    public String returnIndex() {
        return "/index";
    }

    //TEST METHOD FOR THE PROFILEPAGE.HTML, REMOVE LATER
    @GetMapping("/userpage")
    public String returnProfilePage() {
        return "/userpage";
    }

    //METHOD FOR LOGIN
    @PostMapping("/index")
    public String indexLogin(WebRequest wr) {
        String email = wr.getParameter("username");
        String password = wr.getParameter("password");

        //JPA REPOSITORY METHOD WHICH RETURNS A LIST OF USERS WITH EMAIL AND PASSWORD PASSED IN THE INDEX.HTML
        List<User> usersByCredentials = userRepository.findUserByEmailAndPasswordContaining(email, password);

        if (!usersByCredentials.isEmpty()){
            return "redirect:/userpage"; //ADD CORRECT LINK WHEN PROFILEPAGE.HTML IS IMPLEMENTED
        } else {
            return "/index";
        }
    }
}
