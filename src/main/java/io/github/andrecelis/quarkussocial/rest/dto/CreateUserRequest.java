package io.github.andrecelis.quarkussocial.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank(message = "name is Required")
    private String name;
    @NotNull(message = "age is Required")
    private Integer age;

}
