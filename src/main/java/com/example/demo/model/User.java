package com.example.demo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class User {
    private int userId;
    private String firstName;
    private String lastName;
    private String userDescription;
    private String email;
    private String password;
    private Collection<Friend> friendsByUserId;
    private Collection<Friend> friendsByUserFriendlyId;
    private Collection<UserPost> userPostsByUserId;

    public User(String firstName, String lastName, String userDescription, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userDescription = userDescription;
        this.email = email;
        this.password = password;
    }

    public User() {}

    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    @Id
    @Column(name = "user_id", nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "first_name", nullable = false, length = 45)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Basic
    @Column(name = "last_name", nullable = false, length = 45)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Basic
    @Column(name = "user_description", nullable = true, length = 512)
    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    @Basic
    @Column(name = "email", nullable = false, length = 100)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName) &&
                Objects.equals(userDescription, user.userDescription) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, userDescription, email, password);
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<Friend> getFriendsByUserId() {
        return friendsByUserId;
    }

    public void setFriendsByUserId(Collection<Friend> friendsByUserId) {
        this.friendsByUserId = friendsByUserId;
    }

    @OneToMany(mappedBy = "userByFriendlyId")
    public Collection<Friend> getFriendsByUserFriendlyId() {
        return friendsByUserFriendlyId;
    }

    public void setFriendsByUserFriendlyId(Collection<Friend> friendsByUserFriendlyId) {
        this.friendsByUserFriendlyId = friendsByUserFriendlyId;
    }

    @OneToMany(mappedBy = "userByUserId")
    public Collection<UserPost> getUserPostsByUserId() {
        return userPostsByUserId;
    }

    public void setUserPostsByUserId(Collection<UserPost> userPostsByUserId) {
        this.userPostsByUserId = userPostsByUserId;
    }
}
