package ru.mvlsoft.users.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.mvlsoft.users.Configuration;
import ru.mvlsoft.users.contoller.ContactsController;
import ru.mvlsoft.users.dto.ContactDto;
import ru.mvlsoft.users.entity.Contact;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.service.ContactService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mvlsoft.users.Constants.rootPath;
import static ru.mvlsoft.users.entity.enums.ContactKind.EMAIL;

@WebMvcTest(ContactsController.class)
@Import(Configuration.class)
class ContactsControllerTest {
    @MockBean
    private ContactService service;
    @Autowired
    private MockMvc mvc;
    public static final String URI = rootPath + "/{userId}/contacts";

    @Test
    void getTest() throws Exception {
        Long userId = 1L;
        List<Contact> mockContacts = List.of(
                new Contact(),
                new Contact(),
                new Contact()
        );

        when(service.getAll(userId)).thenReturn(mockContacts);

        MvcResult result = mvc.perform(get(URI, userId)).andExpect(status().isOk()).andReturn();
        String json = result.getResponse().getContentAsString();
        String oneObject = "{\"id\":null,\"kind\":null,\"value\":null,\"preferred\":false}";
        String expected = "[" + oneObject + "," + oneObject + "," + oneObject + "]";
        Assertions.assertEquals(expected, json);
    }

    @Test
    void notAllowed() throws Exception {
        mvc.perform(head(URI, 1L)).andExpect(status().isMethodNotAllowed());
        mvc.perform(delete(URI, 1L)).andExpect(status().isMethodNotAllowed());
        mvc.perform(put(URI, 1L)).andExpect(status().isMethodNotAllowed());
        mvc.perform(patch(URI, 1L)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void create() throws Exception {
        Long userId = 1L;
        Long contactId = 2L;
        Contact mockContact = new Contact(contactId, new User(), EMAIL, "value");
        when(service.create(any(Long.class), any(Contact.class))).thenReturn(mockContact);
        String body = new ObjectMapper().writeValueAsString(new ContactDto());
        mvc.perform(post(URI, userId).content(body).contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }
}
