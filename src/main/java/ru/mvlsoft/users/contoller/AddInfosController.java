package ru.mvlsoft.users.contoller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mvlsoft.users.dto.AddInfoDto;
import ru.mvlsoft.users.entity.AdditionalInfo;
import ru.mvlsoft.users.service.AddInfoService;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static ru.mvlsoft.users.Constants.rootPath;

@RestController
@RequestMapping(path = {rootPath + "/{userId}/addinfo", rootPath + "/{userId}/addinfo/"})
@RequiredArgsConstructor
public class AddInfosController {

    private final AddInfoService service;
    @Autowired
    private final ModelMapper mapper;

    @PostMapping()
    @ResponseStatus(CREATED)
    AddInfoDto create(@NotNull @PathVariable Long userId,
                      @Valid @RequestBody AddInfoDto addInfoDto,
                      HttpServletResponse response) {
        AdditionalInfo addInfo = service.create(userId, mapper.map(addInfoDto, AdditionalInfo.class));
        String location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(addInfo.getId())
                .toUri()
                .toString();
        response.addHeader("Location", location);
        return mapper.map(addInfo, AddInfoDto.class);
    }

    @GetMapping()
    Iterable<AddInfoDto> getAll(@NotNull @PathVariable Long userId) {
        Iterable<AdditionalInfo> infos = service.getAll(userId);
        return StreamSupport.stream(infos.spliterator(), false)
                .map(o -> mapper.map(o, AddInfoDto.class))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = {HEAD, DELETE, PUT, PATCH})
    @ResponseStatus(METHOD_NOT_ALLOWED)
    void notAllowed() {}
}
