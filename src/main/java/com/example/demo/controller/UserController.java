package com.example.demo.controller;

import com.example.demo.model.Friend;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.model.UserPost;
import com.example.demo.repository.FriendRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserPostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.List;


@Controller
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserPostRepository userPostRepository;

    //GLOBAL VARIABLES
    User loggedInUser;
    List<Friend> friendsList;
    List<User> userList;
    List<Friend> userFriendList;

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

        if (!usersByCredentials.isEmpty()){
            //SET GLOBAL VARIABLE FOR LOGGED IN USER
            loggedInUser = usersByCredentials.get(0);
            return "redirect:/userpage";
        } else {
            return "/index";
        }
    }

    //METHOD FOR RETRIEVEING THE USER PROFILE PAGE WITH ALL CONTENT
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

    //METHOD FOR RETRIEVEING A NEW PAGE WHICH SHOWS A LIST OF USERS
    @GetMapping("/usersearch")
    public String returnSearchPage(Model model, WebRequest wr) {

        String userSearch = wr.getParameter("search");
        model.addAttribute("userSearch", userSearch);

        String[] usernameArr = userSearch.split(" ");
        for(String s : usernameArr) {
            userList = userRepository.findUserByFirstNameContaining(s);
            userList.addAll(userRepository.findUserByLastNameContaining(s));
            model.addAttribute("userList", userList);
        }
        return "/usersearch";
    }

    @GetMapping("/user/{id}")
    public String returnOtherUserProfilePage(@PathVariable("id") int userId, Model model) {
        User user = userRepository.findById(userId).get();
        userFriendList = friendRepository.findByUserId(user.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("userFriendList", userFriendList);
        return "/userdetails";
    }

    @GetMapping("/remove/{id}")
    public String removeFriend(@PathVariable("id")int id, Model model) {
        Friend friend1 = friendRepository.findByUserIdAndFriendlyId(loggedInUser.getUserId(), id);
        Friend friend2 = friendRepository.findByUserIdAndFriendlyId(id, loggedInUser.getUserId());
        friendRepository.delete(friend1);
        friendRepository.delete(friend2);

        int loggedInUserId = loggedInUser.getUserId();
        List <Friend> friendsListNew = friendRepository.findByUserId(loggedInUserId);
        model.addAttribute("friendsList", friendsListNew);
        model.addAttribute("loggedInUser", loggedInUser);

        return "/userpage";
    }

    @GetMapping("/createNewPost")
    public String createPost(Model model){
        model.addAttribute("createPost", new Post());
        return "/newpost";
    }

    @PostMapping("/createNewPost/{id}")
    public String createPost(@ModelAttribute Post post,  Model model){
        // FIND THE LOGGED IN USER
        User refreshedLoggedInUser = userRepository.findById(loggedInUser.getUserId()).get();
        int postId = postRepository.save(post).getPostId();

        UserPost userPost = new UserPost();
        userPost.setPostId(postId);
        userPost.setUserId(refreshedLoggedInUser.getUserId());
        userPostRepository.save(userPost);

      //model.addAttribute("postList", friendsListNewer);

        return "/userpage";
    }

    @PostMapping("/add/{id}")
    public String addFriend(@PathVariable("id")int id, Model model) {
        User user = userRepository.findById(id).get();
        User refreshedLoggedInUser = userRepository.findById(loggedInUser.getUserId()).get();
        List <Friend> friendsListNewer = friendRepository.findByUserId(refreshedLoggedInUser.getUserId());

        Friend friend1 = new Friend();
        friend1.setUserId(refreshedLoggedInUser.getUserId());
        friend1.setFriendlyId(id);
        friend1.setUserByUserId(refreshedLoggedInUser);
        friend1.setUserByFriendlyId(user);
        friendsListNewer.add(friend1);
        friendRepository.save(friend1);

        Friend friend2 = new Friend();
        friend2.setUserId(id);
        friend2.setFriendlyId(refreshedLoggedInUser.getUserId());
        friend2.setUserByUserId(user);
        friend2.setUserByFriendlyId(refreshedLoggedInUser);
        friendRepository.save(friend2);


        model.addAttribute("friendsList", friendsListNewer);
        model.addAttribute("loggedInUser", refreshedLoggedInUser);

        return "/userpage";
    }

}