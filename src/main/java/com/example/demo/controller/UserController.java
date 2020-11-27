package com.example.demo.controller;

import com.example.demo.model.Friend;
import com.example.demo.model.User;
import com.example.demo.repository.FriendRepository;
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

    @Autowired
    FriendRepository friendRepository;

    //GLOBAL VARIABLE FOR THE LOGGED-IN USER AND FRIENDS LIST
    User loggedInUser;
    List<Friend> friendsList;

    @GetMapping("/index")
    public String returnIndex() {
        return "/index";
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

    //TEST METHOD FOR THE PROFILEPAGE.HTML, REMOVE LATER
    @GetMapping("/userpage")
    public String returnProfilePage(Model model) {

        //METHOD FOR GETTING ALL FRIENDS OF THE LOGGED IN USER BY USING HIS ID
        int loggedInUserId = loggedInUser.getUserId();
        //SETTING IT TO A FRIENDS LIST GLOBAL VARIABLE
        friendsList = friendRepository.findByUserId(loggedInUserId);


        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("friendsList", friendsList);
        return "/userpage";
    }

}
