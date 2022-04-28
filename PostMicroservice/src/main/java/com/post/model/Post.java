package com.post.model;

import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Posts")
public class Post {
    @Id
    private String id;
    private String ownerId;
    private String text;
    private Binary image;

    public Post() {
    }

    public Post(String ownerId, String text, Binary image) {
        this.ownerId = ownerId;
        this.text = text;
        this.image = image;
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
}
