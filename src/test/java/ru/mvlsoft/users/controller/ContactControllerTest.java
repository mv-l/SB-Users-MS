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
import ru.mvlsoft.users.contoller.ContactController;
import ru.mvlsoft.users.dto.ContactDto;
import ru.mvlsoft.users.entity.Contact;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.service.ContactService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mvlsoft.users.Constants.rootPath;
import static ru.mvlsoft.users.entity.enums.ContactKind.EMAIL;

@WebMvcTest(ContactController.class)
@Import(Configuration.class)
class ContactControllerTest {
    @MockBean
    private ContactService service;
    @Autowired
    private MockMvc mvc;
    public static final String URI = rootPath + "/{userId}/contacts/{contactId}";

    private void assertJsonHasAllFields(String json) {
        Assertions.assertTrue(json.contains("id") &&
                json.contains("kind") &&
                json.contains("value") &&
                json.contains("preferred"));
    }

    @Test
    void getTest() throws Exception {
        Long userId = 1L;
        Long contactId = 2L;
        Contact mockContact = new Contact(contactId, new User(), EMAIL, "value");
        when(service.getByIdAndUserId(contactId, userId)).thenReturn(Optional.of(mockContact));
        MvcResult result = mvc.perform(get(URI, userId, contactId)).andExpect(status().isOk()).andReturn();
        String json = result.getResponse().getContentAsString();
        assertJsonHasAllFields(json);

        when(service.getByIdAndUserId(contactId, userId)).thenReturn(Optional.empty());
        mvc.perform(get(URI, userId, contactId)).andExpect(status().isNotFound());
    }

    @Test
    void notAllowed() throws Exception {
        mvc.perform(post(URI, 1L, 2L)).andExpect(status().isMethodNotAllowed());
        mvc.perform(patch(URI, 1L, 2L)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void updatetest() throws Exception {
        Long userId = 1L;
        Long infoId = 2L;
        Contact mockContact = new Contact(infoId, new User(), EMAIL, "value");
        String body = new ObjectMapper().writeValueAsString(new ContactDto());
        when(service.update(any(Long.class), any(Long.class), any(Contact.class))).thenReturn(mockContact);
        MvcResult result = mvc.perform(put(URI, userId, infoId).content(body).contentType(APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String json = result.getResponse().getContentAsString();
        assertJsonHasAllFields(json);
    }

    @Test
    void deletetest() throws Exception {
        Long userId = 1L;
        Long contactId = 2L;
        when(service.checkExists(userId, contactId)).thenReturn(true);
        doNothing().when(service).deleteByIdAndUserId(contactId, userId);
        mvc.perform(delete(URI, userId, contactId)).andExpect(status().isOk());

        when(service.checkExists(userId, contactId)).thenReturn(false);
        mvc.perform(delete(URI, userId, contactId)).andExpect(status().isNotFound());
    }
}
