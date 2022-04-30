package com.post.dto;

public class ReactionDTO {

    private String postId;
    private String userId;
    private boolean like;

    public ReactionDTO() {
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isLike() {
        return like;
    }
}
