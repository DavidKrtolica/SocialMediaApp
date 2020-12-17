package com.example.demo;

import com.example.demo.model.Friend;
import com.example.demo.model.User;
import com.example.demo.repository.FriendRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserPostRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class jUnitTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserPostRepository userPostRepository;

    @Test
    public void returnsUserByCredentials() {
        userRepository.deleteAll();
        User user = new User("David", "David", "blabla", "dd@gmail.com", "dd123");
        userRepository.save(user);
        List<User> userCheck = userRepository.findUserByEmailAndPasswordContaining("dd@gmail.com", "dd123");
        assert(!userCheck.isEmpty());
    }

    @Test
    public void returnsUserFriends() {
        userRepository.deleteAll();
        friendRepository.deleteAll();
        User user = new User("David", "David", "blabla", "dd@gmail.com", "dd123");
        userRepository.save(user);
        List<Friend> friends = friendRepository.findAll();
        assert(friends.isEmpty());
    }

}
