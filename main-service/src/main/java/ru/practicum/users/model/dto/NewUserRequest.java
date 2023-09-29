package ru.practicum.users.model.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {

    @NotBlank
    @Email
    @Size(max = 254, min = 6)
    private String email;

    @NotBlank
    @Size(max = 250, min = 2)
    private String name;
}
