package ru.practicum.users.model.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {

    @NotNull
    private Long id;

    @NotNull
    private String name;
}
