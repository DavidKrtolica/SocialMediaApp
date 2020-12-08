package com.example.demo.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(FriendPK.class)
public class Friend {
    private int userId;
    private int friendlyId;
    private User userByUserId;
    private User userByFriendlyId;

    public Friend() {}

    public Friend(int userId, int friendlyId, User userByUserId, User userByFriendlyId) {
        this.userId = userId;
        this.friendlyId = friendlyId;
        this.userByUserId = userByUserId;
        this.userByFriendlyId = userByFriendlyId;
    }

    @Id
    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Id
    @Column(name = "friendly_id", nullable = false, insertable = false, updatable = false)
    public int getFriendlyId() {
        return friendlyId;
    }

    public void setFriendlyId(int friendlyId) {
        this.friendlyId = friendlyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friend friend = (Friend) o;
        return userId == friend.userId &&
                friendlyId == friend.friendlyId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendlyId);
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    public User getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }

    @ManyToOne
    @JoinColumn(name = "friendly_id", referencedColumnName = "user_id", nullable = false)
    public User getUserByFriendlyId() {
        return userByFriendlyId;
    }

    public void setUserByFriendlyId(User userByFriendlyId) {
        this.userByFriendlyId = userByFriendlyId;
    }
}
