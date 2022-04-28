package com.post.dto;

import javax.validation.constraints.NotBlank;

public class NewPostRequestDTO {
    @NotBlank
    private String ownerId;
    private String text;

    public NewPostRequestDTO() {
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getText() {
        return text;
    }
}
