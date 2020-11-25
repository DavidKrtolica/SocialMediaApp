package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    List<User> findUserByFirstNameContaining(String firstName);
    List<User> findUserByLastNameContaining(String lastName);
    List<User> findUserByEmailAndPasswordContaining(String email, String password);
}
