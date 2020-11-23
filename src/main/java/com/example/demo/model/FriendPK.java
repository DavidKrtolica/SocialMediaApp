package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class FriendPK implements Serializable {
    private int userId;
    private int friendlyId;

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    @Id
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "friendly_id", nullable = false, insertable = false, updatable = false)
    @Id
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
        FriendPK friendPK = (FriendPK) o;
        return userId == friendPK.userId &&
                friendlyId == friendPK.friendlyId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendlyId);
    }
}
