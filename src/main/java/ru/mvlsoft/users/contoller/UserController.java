package ru.mvlsoft.users.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mvlsoft.users.dto.UserDto;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.service.UserService;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static ru.mvlsoft.users.Constants.rootPath;

@RestController
@RequestMapping(path = {rootPath + "/{userId}", rootPath + "/{userId}/"})
@RequiredArgsConstructor()
public class UserController {

    public static final String USER_NOT_FOUND = "User not found";
    private final UserService service;
    @Autowired
    private final ModelMapper mapper;

    // Получить одного юзера
    @GetMapping()
    UserDto get(@NotNull @PathVariable Long userId) {
        User user = service.findUserById(userId);
        if (user == null) {
            throw new ResponseStatusException(NOT_FOUND, USER_NOT_FOUND);
        }
        return mapper.map(user, UserDto.class);
    }

    @RequestMapping(method = {POST, PATCH})
    @ResponseStatus(METHOD_NOT_ALLOWED)
    void notAllowed() {}

    // Изменить одного юзера
    @PutMapping()
    UserDto update(@NotNull @PathVariable Long userId,
                   @Valid @RequestBody UserDto userDto) {
        User user = mapper.map(userDto, User.class);
        user = service.updateUser(userId, user);
        return mapper.map(user, UserDto.class);
    }

    // Удалить юзера
    @DeleteMapping()
    void delete(@NotNull @PathVariable Long userId) {
        if (!service.checkUserExists(userId)) {
            throw new ResponseStatusException(NOT_FOUND, USER_NOT_FOUND);
        }
        service.deleteUser(userId);
    }
}
