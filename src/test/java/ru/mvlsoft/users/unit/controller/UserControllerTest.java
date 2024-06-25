package ru.mvlsoft.users.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.mvlsoft.users.contoller.UserController;
import ru.mvlsoft.users.dto.UserDto;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.service.UserService;
import ru.mvlsoft.users.unit.Configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mvlsoft.users.Constants.rootPath;

@WebMvcTest(UserController.class)
@Import(Configuration.class)
class UserControllerTest {
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;
    public static final String URI = rootPath + "/{userId}";

    private void assertJsonHasAllFields(String json) {
        Assertions.assertTrue(json.contains("id") &&
                json.contains("userName") &&
                json.contains("firstName") &&
                json.contains("middleName") &&
                json.contains("lastName") &&
                json.contains("title") &&
                json.contains("birthDate") &&
                json.contains("sex") &&
                json.contains("avatarId") &&
                json.contains("status") &&
                json.contains("registrationDate"));
    }

    @Test
    void getTest() throws Exception {
        Long userId = 1L;
        User user = new User(1L, "username", "a", "b");
        when(userService.findUserById(1L)).thenReturn(user);
        MvcResult result = mvc.perform(get(URI, 1L))
                /*.andDo(print())*/
                .andExpect(status().isOk())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        //UserDto dto = new ObjectMapper().readValue(json, UserDto.class);
        assertJsonHasAllFields(json);

        when(userService.findUserById(1L)).thenReturn(null);
        mvc.perform(get(URI, 1L)).andExpect(status().isNotFound());
    }

    @Test
    void notAllowed() throws Exception {
        mvc.perform(post(URI, 1L)).andExpect(status().isMethodNotAllowed());
        mvc.perform(patch(URI, 1L)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void update() throws Exception {
        User user = new User(1L, "username", "a", "b");
        String body = new ObjectMapper().writeValueAsString(new UserDto());
        when(userService.updateUser(any(Long.class), any(User.class))).thenReturn(user);
        MvcResult result = mvc.perform(put(URI, 1L).content(body).contentType(APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String json = result.getResponse().getContentAsString();
        assertJsonHasAllFields(json);
    }

    @Test
    void deleteTest() throws Exception {
        when(userService.checkUserExists(1L)).thenReturn(true);
        doNothing().when(userService).deleteUser(any(Long.class));
        mvc.perform(delete(URI, 1L)).andExpect(status().isOk());

        when(userService.checkUserExists(1L)).thenReturn(false);
        mvc.perform(delete(URI, 1L)).andExpect(status().isNotFound());
    }
}
