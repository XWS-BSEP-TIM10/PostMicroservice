package com.post.dto;

import com.post.model.Comment;
import com.post.model.Post;

import java.util.List;

public class PostsResponseDTO {
    private String id;
    private String text;
    private String ownerId;
    private List<String> likes;
    private List<String> dislikes;
    private List<Comment> comments;
    private String creationDate;
    private String image;


    public PostsResponseDTO(Post post) {
        this.id = post.getId();
        this.text = post.getText();
        this.ownerId = post.getOwnerId();
        this.likes = post.getLikes();
        this.dislikes = post.getDislikes();
        this.comments = post.getComments();
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

    public List<String> getLikes() {
        return likes;
    }

    public void setLikes(List<String> likes) {
        this.likes = likes;
    }

    public List<String> getDislikes() {
        return dislikes;
    }

    public void setDislikes(List<String> dislikes) {
        this.dislikes = dislikes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
