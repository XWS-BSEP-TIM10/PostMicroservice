package com.post.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "Posts")
public class Post {
    @Id
    private String id;
    private String ownerId;
    private String text;
    private Binary image;
    private Date creationDate;
    private List<String> likes;
    private List<String> dislikes;
    private List<Comment> comments;

    public Post() {
        this.likes = new ArrayList<>();
        this.dislikes = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.creationDate = new Date();
    }

    public Post(String ownerId, String text, Binary image) {
        this.ownerId = ownerId;
        this.text = text;
        this.image = image;
        this.likes = new ArrayList<>();
        this.dislikes = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.creationDate = new Date();
    }

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getText() {
        return text;
    }

    public Binary getImage() {
        return image;
    }

    public List<String> getLikes() {
        return likes;
    }

    public List<String> getDislikes() {
        return dislikes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
