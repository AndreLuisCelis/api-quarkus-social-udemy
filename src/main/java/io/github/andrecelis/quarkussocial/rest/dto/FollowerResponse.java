package io.github.andrecelis.quarkussocial.rest.dto;

import lombok.Data;

@Data
public class FollowerResponse {
    private Long followerId;
    private Long userId;
    private String name;
}
