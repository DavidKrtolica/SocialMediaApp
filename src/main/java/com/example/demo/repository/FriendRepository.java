package com.example.demo.repository;

import com.example.demo.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
    List<Friend> findByUserId(Integer userId);
    Friend findByUserIdAndFriendlyId(Integer userId, Integer friendlyId);

}
