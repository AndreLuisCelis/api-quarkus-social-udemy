package io.github.andrecelis.quarkussocial.rest.dto;

import io.github.andrecelis.quarkussocial.domain.model.Follower;
import lombok.Data;

import java.util.List;

@Data
public class FollowerPerUserResponse {
    private Long fallowersCount;
    private List<FollowerResponse> content;
}
