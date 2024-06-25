package ru.mvlsoft.users.contoller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mvlsoft.users.dto.AddInfoDto;
import ru.mvlsoft.users.entity.AdditionalInfo;
import ru.mvlsoft.users.service.AddInfoService;

import java.util.Optional;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static ru.mvlsoft.users.Constants.rootPath;

@RestController
@RequestMapping(path = {rootPath + "/{userId}/addinfo/{infoId}", rootPath + "/{userId}/addinfo/{infoId}/"})
@RequiredArgsConstructor
public class AddInfoController {

    public static final String ADDITIONAL_INFORMATION_NOT_FOUND = "Additional information not found";
    private final AddInfoService service;

    @Autowired
    private final ModelMapper mapper;

    @GetMapping()
    AddInfoDto get(@NotNull @PathVariable Long userId, @NotNull @PathVariable Long infoId) {
        Optional<AdditionalInfo> addInfo = service.getByIdAndUserId(infoId, userId);
        if (addInfo.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, ADDITIONAL_INFORMATION_NOT_FOUND);
        }
        return mapper.map(addInfo.get(), AddInfoDto.class);
    }

    @RequestMapping(method = {POST, PATCH})
    @ResponseStatus(METHOD_NOT_ALLOWED)
    void notAllowed() {}

    @PutMapping()
    AddInfoDto update(@NotNull @PathVariable Long userId, @NotNull @PathVariable Long infoId, @NotNull @RequestBody AddInfoDto infoDto) {
        AdditionalInfo info = mapper.map(infoDto, AdditionalInfo.class);
        info = service.update(userId, infoId, info);
        if (info == null) {
            throw new ResponseStatusException(NOT_FOUND, ADDITIONAL_INFORMATION_NOT_FOUND);
        }
        return mapper.map(info, AddInfoDto.class);
    }

    @DeleteMapping()
    void delete(@NotNull @PathVariable Long userId, @NotNull @PathVariable Long infoId) {
        if (!service.checkExists(userId, infoId)) {
            throw new ResponseStatusException(NOT_FOUND, ADDITIONAL_INFORMATION_NOT_FOUND);
        }
        service.deleteByIdAndUserId(infoId, userId);
    }
}
