package com.post.dto;

import com.post.model.Post;

public class PostsResponseDTO {
    private String id;
    private String text;
    private String ownerId;
    private String image;

    
    public PostsResponseDTO(Post post) {
        this.id = post.getId();
        this.text = post.getText();
        this.ownerId = post.getOwnerId();
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
