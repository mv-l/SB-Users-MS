package ru.mvlsoft.users.contoller;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.mvlsoft.users.dto.ContactDto;
import ru.mvlsoft.users.entity.Contact;
import ru.mvlsoft.users.service.ContactService;

import java.util.Optional;

import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static ru.mvlsoft.users.Constants.rootPath;

@RestController
@RequestMapping(path = {rootPath + "/{userId}/contacts/{contactId}", rootPath + "/{userId}/contacts/{contactId}/"})
@RequiredArgsConstructor
public class ContactController {

    private final ContactService service;
    @Autowired
    private final ModelMapper mapper;

    @GetMapping()
    ContactDto get(@NotNull @PathVariable Long userId,
                   @NotNull @PathVariable Long contactId) {
        Optional<Contact> contact = service.getByIdAndUserId(contactId, userId);
        if (contact.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Contact not found");
        }
        return mapper.map(contact.get(), ContactDto.class);
    }

    @RequestMapping(method = {POST, PATCH})
    @ResponseStatus(METHOD_NOT_ALLOWED)
    void notAllowed() {}

    @PutMapping()
    ContactDto update(@NotNull @PathVariable Long userId,
                      @NotNull @PathVariable Long contactId,
                      @NotNull @RequestBody ContactDto contactDto) {
        Contact contact = service.update(userId, contactId, mapper.map(contactDto, Contact.class));
        return mapper.map(contact, ContactDto.class);
    }

    @DeleteMapping()
    void delete(@NotNull @PathVariable Long userId,
                @NotNull @PathVariable Long contactId) {
        if (!service.checkExists(userId, contactId)) {
            throw new ResponseStatusException(NOT_FOUND, "Contact not found");
        }
        service.deleteByIdAndUserId(contactId, userId);
    }
}
