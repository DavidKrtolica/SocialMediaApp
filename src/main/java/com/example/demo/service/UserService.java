package com.example.demo.service;

import com.example.demo.model.Friend;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.model.UserPost;
import com.example.demo.repository.FriendRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserPostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.context.request.WebRequest;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

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

    //METHOD USED FOR LOGGING IN
    public String login(WebRequest wr) {
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

    //METHOD USED FOR RETURNING THE USER PAGE OF THE LOGGED IN USER
    public String profilePage(Model model) {
        //METHOD FOR GETTING ALL FRIENDS OF THE LOGGED IN USER BY USING HIS ID
        int loggedInUserId = loggedInUser.getUserId();
        //SETTING IT TO A FRIENDS LIST GLOBAL VARIABLE
        friendsList = friendRepository.findByUserId(loggedInUserId);

        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("friendsList", friendsList);
        Collection<UserPost> userPostList = userPostRepository.findAllUserPostsByUserId(loggedInUser.getUserId());
        model.addAttribute("userPostList", userPostList);
        return "/userpage";
    }

    //METHOD USED FOR RETURNING THE SEARCH PAGE ONCE YOU SEARCH FOR ANOTHER USER
    public String searchPage(Model model, WebRequest wr) {
        User refreshedLoggedInUser = userRepository.findById(loggedInUser.getUserId()).get();
        List<Friend> refreshedFriendList = (List<Friend>) refreshedLoggedInUser.getFriendsByUserId();

        String userSearch = wr.getParameter("search");
        model.addAttribute("userSearch", userSearch);

        String[] usernameArr = userSearch.split(" ");
        for(String s : usernameArr) {
            userList = userRepository.findUserByFirstNameContaining(s);
            userList.addAll(userRepository.findUserByLastNameContaining(s));
            model.addAttribute("userList", userList);
        }

        for(User user : userList) {
            Friend friend = new Friend(refreshedLoggedInUser.getUserId(), user.getUserId(), refreshedLoggedInUser, user);
            boolean checkFriend = refreshedFriendList.contains(friend);
            model.addAttribute("checkFriend", checkFriend);
        }

        return "/usersearch";
    }

    //METHOD USED FOR RETRIEVING THE PROFILE PAGE OF THE USER YOU SEARCHED FOR
    public String otherProfilePage(int userId, Model model) {
        User user = userRepository.findById(userId).get();
        userFriendList = friendRepository.findByUserId(user.getUserId());
        model.addAttribute("user", user);
        model.addAttribute("userFriendList", userFriendList);
        Collection<UserPost> userPostList = userPostRepository.findAllUserPostsByUserId(user.getUserId());
        model.addAttribute("userPostList", userPostList);
        return "/userdetails";
    }

    //METHOD FOR REMOVING A FRIEND
    public String removeFriends(int id, Model model) {
        Friend friend1 = friendRepository.findByUserIdAndFriendlyId(loggedInUser.getUserId(), id);
        Friend friend2 = friendRepository.findByUserIdAndFriendlyId(id, loggedInUser.getUserId());
        friendRepository.delete(friend1);
        friendRepository.delete(friend2);

        int loggedInUserId = loggedInUser.getUserId();
        List <Friend> friendsListNew = friendRepository.findByUserId(loggedInUserId);
        model.addAttribute("friendsList", friendsListNew);
        model.addAttribute("loggedInUser", loggedInUser);
        Collection<UserPost> userPostList = userPostRepository.findAllUserPostsByUserId(loggedInUser.getUserId());
        model.addAttribute("userPostList", userPostList);

        return "/userpage";
    }

    //METHOD FOR CREATING A NEW POST AS LOGGED IN USER
    public String createNewPost(WebRequest wr, Model model) {
        // FIND THE LOGGED IN USER AND FRIEND LIST
        User refreshedLoggedInUser = userRepository.findById(loggedInUser.getUserId()).get();

        String postDesc = wr.getParameter("description");
        Post post = new Post();
        post.setPostDescription(postDesc);

        postRepository.save(post);
        int postId = post.getPostId();

        UserPost userPost = new UserPost();
        userPost.setPostId(postId);
        userPost.setUserId(refreshedLoggedInUser.getUserId());
        userPost.setPostByPostId(post);
        userPost.setUserByUserId(refreshedLoggedInUser);
        userPostRepository.save(userPost);

        List <Friend> friendsListNewer = friendRepository.findByUserId(refreshedLoggedInUser.getUserId());
        model.addAttribute("loggedInUser", refreshedLoggedInUser);
        model.addAttribute("friendsList", friendsListNewer);

        Collection<UserPost> userPostList = userPostRepository.findAllUserPostsByUserId(refreshedLoggedInUser.getUserId());
        model.addAttribute("userPostList", userPostList);

        return "/userpage";
    }

    //METHOD FOR ADDING A NEW FRIEND
    public String addNewFriend(int id, Model model) {
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
        Collection<UserPost> userPostList = userPostRepository.findAllUserPostsByUserId(refreshedLoggedInUser.getUserId());
        model.addAttribute("userPostList", userPostList);

        return "/userpage";
    }
}
