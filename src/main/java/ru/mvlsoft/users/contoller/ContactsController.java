package ru.mvlsoft.users.contoller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.mvlsoft.users.dto.ContactDto;
import ru.mvlsoft.users.entity.Contact;
import ru.mvlsoft.users.service.ContactService;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static ru.mvlsoft.users.Constants.rootPath;

@RestController
@RequestMapping(path = {rootPath + "/{userId}/contacts", rootPath + "/{userId}/contacts/"})
@RequiredArgsConstructor
public class ContactsController {

    private final ContactService service;
    @Autowired
    private final ModelMapper mapper;

    @GetMapping()
    Iterable<ContactDto> get(@NotNull @PathVariable Long userId) {
        Iterable<Contact> contacts = service.getAll(userId);
        return StreamSupport.stream(contacts.spliterator(), false).map(o -> mapper.map(o, ContactDto.class)).collect(Collectors.toList());
    }

    @RequestMapping(method = {HEAD, DELETE, PUT, PATCH})
    @ResponseStatus(METHOD_NOT_ALLOWED)
    void notAllowed() {}

    @PostMapping()
    @ResponseStatus(CREATED)
    ContactDto create(@NotNull @PathVariable Long userId, @Valid @RequestBody ContactDto contactDto, HttpServletResponse response) {
        Contact contact = service.create(userId, mapper.map(contactDto, Contact.class));
        String location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(contact.getId()).toUri().toString();
        response.addHeader("Location", location);
        return mapper.map(contact, ContactDto.class);
    }

}
