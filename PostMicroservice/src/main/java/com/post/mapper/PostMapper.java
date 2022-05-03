package com.post.mapper;

import com.post.dto.PostsResponseDTO;
import com.post.model.Post;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Base64;

@Component
public class PostMapper {

    private static final SimpleDateFormat iso8601Formater = new SimpleDateFormat("dd/MM/yyyy");

    public static PostsResponseDTO toDTO(Post post) {
        PostsResponseDTO postsResponseDTO = new PostsResponseDTO(post);
        postsResponseDTO.setImage(Base64.getEncoder().encodeToString(post.getImage().getData()));
        postsResponseDTO.setCreationDate(iso8601Formater.format(post.getCreationDate()));
        return postsResponseDTO;
    }
}
