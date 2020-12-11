package com.example.demo;

import com.example.demo.model.Friend;
import com.example.demo.model.User;
import com.example.demo.repository.FriendRepository;
import com.example.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IntegrationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    FriendRepository friendRepository;

    @BeforeEach
    public void init() {
        userRepository.deleteAll();
        friendRepository.deleteAll();
    }

    //FOR THIS TESTS TO WORK - IN THE FRIEND TABLE IN MYSQL SET FOREIGN KEY RESTRICTIONS TO CASCADE

    @Test
    public void should_find_no_users() {
        Iterable<User> users = userRepository.findAll();
        assertThat(users).isEmpty();
    }

    @Test
    public void should_find_no_friends() {
        Iterable<Friend> friends = friendRepository.findAll();
        assertThat(friends).isEmpty();
    }

    @Test
    public void should_find_by_first_name_containing() {
        userRepository.save(new User("David", "Krtolica", "bla", "dk@gmail.com", "dk123"));
        Iterable<User> findUsers = userRepository.findUserByFirstNameContaining("David");
        assertThat(findUsers).isNotEmpty();
    }

    @Test
    public void should_find_by_last_name_containing() {
        userRepository.save(new User("David", "Krtolica", "bla", "dk@gmail.com", "dk123"));
        Iterable<User> findUsers = userRepository.findUserByLastNameContaining("Krtolica");
        assertThat(findUsers).isNotEmpty();
    }

    @Test
    public void should_find_by_email_and_password_containing() {
        userRepository.save(new User("David", "Krtolica", "bla", "dk@gmail.com", "dk123"));
        Iterable<User> findUsers = userRepository.findUserByEmailAndPasswordContaining("dk@gmail.com", "dk123");
        assertThat(findUsers).isNotEmpty();
    }

    @Test
    public void should_find_by_user_id() {
        User user1 = userRepository.save(new User("David", "Krtolica", "bla", "dk@gmail.com", "dk123"));
        User user2 = userRepository.save(new User("David", "Haring", "blabla", "dh@gmail.com", "dh123"));
        friendRepository.save(new Friend(user1.getUserId(), user2.getUserId(), user1, user2));
        Iterable<Friend> findFriends = friendRepository.findByUserId(user1.getUserId());
        assertThat(findFriends).isNotEmpty();
    }

    @Test
    public void should_find_by_user_and_friendly_id() {
        User user1 = userRepository.save(new User("David", "Krtolica", "bla", "dk@gmail.com", "dk123"));
        User user2 = userRepository.save(new User("David", "Haring", "blabla", "dh@gmail.com", "dh123"));
        friendRepository.save(new Friend(user1.getUserId(), user2.getUserId(), user1, user2));
        Friend findFriend = friendRepository.findByUserIdAndFriendlyId(user1.getUserId(), user2.getUserId());
        assertThat(findFriend).hasFieldOrPropertyWithValue("userId", user1.getUserId());

    }

}
