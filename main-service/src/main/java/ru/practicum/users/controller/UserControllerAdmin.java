package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.model.dto.NewUserRequest;
import ru.practicum.users.model.dto.UserDto;
import ru.practicum.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserControllerAdmin {

    private final UserService userService;

    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("POST '/admin/users' создание user с body={}", newUserRequest.toString());
        return userService.createUser(newUserRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getAllUsersByIds(
            @RequestParam(required = false) List<Long> ids,
            @Valid @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Valid @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GET at '/admin/users' получить все users с параметрами: ids={}, from={}, size={}",
                ids, from, size);
        return userService.getUsersByIds(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {
        log.info("DELETE at '/admin/users/{}' удаление user с id={}", userId, userId);
        userService.deleteUser(userId);
    }
}
