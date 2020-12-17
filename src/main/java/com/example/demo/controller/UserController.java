package com.example.demo.controller;

import com.example.demo.model.Friend;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.model.UserPost;
import com.example.demo.repository.FriendRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserPostRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.Collection;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/index")
    public String returnIndex() {
        return "/index";
    }

    //METHOD FOR LOGIN
    @PostMapping("/index")
    public String indexLogin(WebRequest wr) {
        return userService.login(wr);
    }

    //METHOD FOR RETRIEVEING THE USER PROFILE PAGE WITH ALL CONTENT
    @GetMapping("/userpage")
    public String returnProfilePage(Model model) {
        return userService.profilePage(model);
    }

    //METHOD FOR RETRIEVEING A NEW PAGE WHICH SHOWS A LIST OF USERS
    @GetMapping("/usersearch")
    public String returnSearchPage(Model model, WebRequest wr) {
        return userService.searchPage(model, wr);
    }

    //METHOD FOR RETRIEVING A PROFILE PAGE OF ANOTHER USER
    @GetMapping("/user/{id}")
    public String returnOtherUserProfilePage(@PathVariable("id") int userId, Model model) {
        return userService.otherProfilePage(userId, model);
    }

    //METHOD FOR FRIEND-REMOVAL
    @GetMapping("/remove/{id}")
    public String removeFriend(@PathVariable("id")int id, Model model) {
        return userService.removeFriends(id, model);
    }

    //METHOD FOR GOING TO "CREATE-NEW-POST" PAGE
    @GetMapping("/createNewPost")
    public String createPost(Model model){
        model.addAttribute("createPost", new Post());
        return "/newpost";
    }

    //METHOD FOR CREATING A NEW PAGE
    @PostMapping("/createNewPost")
    public String createPost(WebRequest wr, Model model){
        return userService.createNewPost(wr, model);
    }

    //METHOD FOR ADDING A FRIEND
    @PostMapping("/add/{id}")
    public String addFriend(@PathVariable("id")int id, Model model) {
        return userService.addNewFriend(id, model);
    }
}