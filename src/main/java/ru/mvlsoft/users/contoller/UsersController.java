package ru.mvlsoft.users.contoller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mvlsoft.users.dto.UserDto;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static ru.mvlsoft.users.Constants.rootPath;

@RestController
@RequestMapping(rootPath)
@RequiredArgsConstructor
public class UsersController {

    private final UserService service;

    @Autowired
    private final ModelMapper mapper;

    // Новый юзер
    @PostMapping(path = {"", "/"})
    @ResponseStatus(CREATED)
    UserDto create(@Valid @RequestBody UserDto userDto,
                   HttpServletResponse response) {
        User user = service.createUser(mapper.map(userDto, User.class));
        String location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri().toString();
        response.addHeader("Location", location);
        return mapper.map(user, UserDto.class);
    }

    // Получить список всех юзеров
    @GetMapping(path = {"", "/"})
    List<UserDto> getAll() {
        List<User> users = service.getAllUsers();
        return users.stream().map(o -> mapper.map(o, UserDto.class)).collect(Collectors.toList());
    }

    @RequestMapping(method = {HEAD, DELETE, PUT, PATCH}, path = {"", "/"})
    @ResponseStatus(METHOD_NOT_ALLOWED)
    void notAllowed() {}
}
