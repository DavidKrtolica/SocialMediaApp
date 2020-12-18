package com.example.demo;

import com.example.demo.model.Friend;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.FriendRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserPostRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public void createsPost() {
        postRepository.deleteAll();
        Post post = new Post();
        post.setPostDescription("blabla");
        postRepository.save(post);
        assertThat(postRepository.findById(post.getPostId()));
    }

    @Test
    public void addsFriend() {
        userRepository.deleteAll();
        friendRepository.deleteAll();
        User user1 = new User("David", "David", "blabla", "dd@gmail.com", "dd123");
        User user2 = new User("Nirendra", "Rawal", "hehe", "nr@gmail.com", "nr123");
        userRepository.save(user1);
        userRepository.save(user2);
        Friend friend1 = new Friend(user1.getUserId(), user2.getUserId(), user1, user2);
        Friend friend2 = new Friend(user2.getUserId(), user1.getUserId(), user2, user1);
        friendRepository.save(friend1);
        friendRepository.save(friend2);
        assertThat(friend1.getUserByUserId().equals(user1));
    }

    @Test
    public void removesFriend() {
        userRepository.deleteAll();
        friendRepository.deleteAll();
        User user1 = new User("David", "David", "blabla", "dd@gmail.com", "dd123");
        User user2 = new User("Nirendra", "Rawal", "hehe", "nr@gmail.com", "nr123");
        userRepository.save(user1);
        userRepository.save(user2);
        Friend friend1 = new Friend(user1.getUserId(), user2.getUserId(), user1, user2);
        Friend friend2 = new Friend(user2.getUserId(), user1.getUserId(), user2, user1);
        friendRepository.save(friend1);
        friendRepository.save(friend2);
        Friend friendcheck1 = friendRepository.findByUserIdAndFriendlyId(user1.getUserId(), user2.getUserId());
        Friend friendcheck2 = friendRepository.findByUserIdAndFriendlyId(user2.getUserId(), user1.getUserId());
        friendRepository.delete(friendcheck1);
        friendRepository.delete(friendcheck2);
        List<Friend> friendList = friendRepository.findAll();
        assertThat(friendList.isEmpty());
    }

}
