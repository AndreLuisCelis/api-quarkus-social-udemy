package io.github.andrecelis.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateFollowerRequest {
//    @NotBlank(message = "userId is Required")
//    private Long userId;
    @NotNull(message = "followerId is Required")
    private Long followerId;
}
