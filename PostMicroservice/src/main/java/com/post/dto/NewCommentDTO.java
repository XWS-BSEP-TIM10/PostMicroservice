package com.post.dto;

public class NewCommentDTO {

    private String postId;
    private String userId;
    private String text;

    public NewCommentDTO(String postId, String userId, String text) {
        this.postId = postId;
        this.userId = userId;
        this.text = text;
    }

    public String getPostId() {
        return postId;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }
}
