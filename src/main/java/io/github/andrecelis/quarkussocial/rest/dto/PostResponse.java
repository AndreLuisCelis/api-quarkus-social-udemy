package io.github.andrecelis.quarkussocial.rest.dto;

import io.github.andrecelis.quarkussocial.domain.model.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponse {
    private String text;
    private LocalDateTime dateTime;
    private Long id;

    public static PostResponse fromEntity( Post post){
        PostResponse response = new PostResponse();
        response.text = post.getText();
        response.dateTime = post.getDateTime();
        response.id = post.getId();
        return response;
    }
}
