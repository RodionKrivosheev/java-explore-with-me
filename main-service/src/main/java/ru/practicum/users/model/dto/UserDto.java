package ru.practicum.users.model.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @NotNull
    @Email
    private String email;

    private Long id;

    @NotNull
    private String name;
}
