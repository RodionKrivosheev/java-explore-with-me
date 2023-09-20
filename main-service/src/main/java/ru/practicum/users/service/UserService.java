package ru.practicum.users.service;

import ru.practicum.users.model.dto.NewUserRequest;
import ru.practicum.users.model.dto.UserDto;
import ru.practicum.users.model.entity.User;

import java.util.List;

public interface UserService {

    UserDto createUser(NewUserRequest newUserRequest);

    List<UserDto> getUsersByIds(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long id);

    void userExists(Long id);

    User getUserModelById(Long id);
}
