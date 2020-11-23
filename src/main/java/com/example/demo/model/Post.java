package com.example.demo.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Post {
    private int postId;
    private String postDescription;
    private Collection<UserPost> userPostsByPostId;

    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    @Id
    @Column(name = "post_id", nullable = false)
    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    @Basic
    @Column(name = "post_description", nullable = false, length = 512)
    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return postId == post.postId &&
                Objects.equals(postDescription, post.postDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, postDescription);
    }

    @OneToMany(mappedBy = "postByPostId")
    public Collection<UserPost> getUserPostsByPostId() {
        return userPostsByPostId;
    }

    public void setUserPostsByPostId(Collection<UserPost> userPostsByPostId) {
        this.userPostsByPostId = userPostsByPostId;
    }
}
