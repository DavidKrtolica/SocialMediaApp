package com.example.demo.repository;

import com.example.demo.model.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPostRepository extends JpaRepository<UserPost, Integer> {
    List<UserPost> findAllUserPostsByUserId(int userId);
}
