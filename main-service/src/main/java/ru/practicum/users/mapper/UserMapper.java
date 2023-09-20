package ru.practicum.users.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.users.model.dto.NewUserRequest;
import ru.practicum.users.model.dto.UserDto;
import ru.practicum.users.model.dto.UserShortDto;
import ru.practicum.users.model.entity.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class UserMapper {

    public UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public List<UserDto> toUserDto(Iterable<User> users) {
        List<UserDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(toUserDto(user));
        }

        return result;
    }

    public User toUser(NewUserRequest newUserRequest) {
        return User.builder()
                .name(newUserRequest.getName())
                .email(newUserRequest.getEmail())
                .build();
    }

    public UserShortDto toUserShortDto(User user) {
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    public List<UserShortDto> toUserShortDto(Iterable<User> users) {
        List<UserShortDto> result = new ArrayList<>();

        for (User user : users) {
            result.add(toUserShortDto(user));
        }

        return result;
    }
}
