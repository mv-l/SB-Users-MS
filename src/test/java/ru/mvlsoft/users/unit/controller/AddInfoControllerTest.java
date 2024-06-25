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
import ru.mvlsoft.users.contoller.AddInfoController;
import ru.mvlsoft.users.dto.AddInfoDto;
import ru.mvlsoft.users.entity.AdditionalInfo;
import ru.mvlsoft.users.entity.User;
import ru.mvlsoft.users.service.AddInfoService;
import ru.mvlsoft.users.unit.Configuration;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.mvlsoft.users.Constants.rootPath;
import static ru.mvlsoft.users.entity.enums.UserAdditionalInfoKind.HOBBY;

@WebMvcTest(AddInfoController.class)
@Import(Configuration.class)
class AddInfoControllerTest {
    @MockBean
    private AddInfoService infoService;
    @Autowired
    private MockMvc mvc;
    public static final String URI = rootPath + "/{userId}/addinfo/{infoId}";

    private void assertJsonHasAllFields(String json) {
        Assertions.assertTrue(json.contains("id") &&
                json.contains("userId") &&
                json.contains("kind") &&
                json.contains("value") &&
                json.contains("validFromDate") &&
                json.contains("validToDate"));
    }

    @Test
    void getTest() throws Exception {
        Long userId = 1L;
        Long infoId = 2L;
        AdditionalInfo mockInfo = new AdditionalInfo(infoId, new User(), HOBBY, "value");
        when(infoService.getByIdAndUserId(infoId, userId)).thenReturn(Optional.of(mockInfo));

        MvcResult result = mvc.perform(get(URI, userId, infoId)).andExpect(status().isOk()).andReturn();
        String json = result.getResponse().getContentAsString();
        assertJsonHasAllFields(json);

        when(infoService.getByIdAndUserId(infoId, userId)).thenReturn(Optional.empty());
        mvc.perform(get(URI, userId, infoId)).andExpect(status().isNotFound());
    }

    @Test
    void notAllowed() throws Exception {
        mvc.perform(post(URI, 1L, 2L)).andExpect(status().isMethodNotAllowed());
        mvc.perform(patch(URI, 1L, 2L)).andExpect(status().isMethodNotAllowed());
    }

    @Test
    void updateTest() throws Exception {
        Long userId = 1L;
        Long infoId = 2L;
        AdditionalInfo info = new AdditionalInfo(infoId, new User(), HOBBY, "value");
        String body = new ObjectMapper().writeValueAsString(new AddInfoDto());
        when(infoService.update(any(Long.class), any(Long.class), any(AdditionalInfo.class))).thenReturn(info);
        MvcResult result = mvc.perform(put(URI, userId, infoId).content(body).contentType(APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
        String json = result.getResponse().getContentAsString();
        assertJsonHasAllFields(json);

        when(infoService.update(any(Long.class), any(Long.class), any(AdditionalInfo.class))).thenReturn(null);
        mvc.perform(put(URI, userId, infoId).content(body).contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
    }

    @Test
    void deleteTest() throws Exception {
        Long userId = 1L;
        Long infoId = 2L;
        when(infoService.checkExists(userId, infoId)).thenReturn(true);
        doNothing().when(infoService).deleteByIdAndUserId(any(Long.class), any(Long.class));
        mvc.perform(delete(URI, userId, infoId)).andExpect(status().isOk());

        when(infoService.checkExists(any(Long.class), any(Long.class))).thenReturn(false);
        mvc.perform(delete(URI, userId, infoId)).andExpect(status().isNotFound());
    }
}
