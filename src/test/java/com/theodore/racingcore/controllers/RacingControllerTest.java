package com.theodore.racingcore.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theodore.racingcore.models.racing.requests.CreateNewDriverRequestDto;
import com.theodore.racingcore.services.RacingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RacingController.class)
@Import(ControllerTestConfig.class)
class RacingControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    RacingService racingService;

    private static final String URL = "/racing/driver/create";

    private static final String ALIAS = "Morizo";
    private static final String USER_ID = "asdf-12345";

    @Test
    @WithMockUser(roles = "SIMPLE_USER")
    void givenSimpleUserRole_whenCreateDriver_returnCreatedAndLocationHeader() throws Exception {
        // given
        when(racingService.createNewDriver(ALIAS)).thenReturn(USER_ID);

        var dto = new CreateNewDriverRequestDto(ALIAS);

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        "http://localhost" + "/racing/driver/by-id/" + USER_ID));

        verify(racingService).createNewDriver(ALIAS);
        verifyNoMoreInteractions(racingService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void givenIncorrectRole_whenCreateDriver_returnForbidden() throws Exception {
        // given
        var dto = new CreateNewDriverRequestDto(ALIAS);

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(racingService);
    }

    @Test
    @WithMockUser(roles = "SIMPLE_USER")
    void givenNoCsrf_whenCreateDriver_returnForbidden() throws Exception {
        // given
        var dto = new CreateNewDriverRequestDto(ALIAS);

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verifyNoInteractions(racingService);
    }

    @Test
    @WithMockUser(roles = "SIMPLE_USER")
    void givenInvalidRequest_whenCreateDriver_returnBadRequest() throws Exception {
        // given
        var dto = new CreateNewDriverRequestDto("");

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(racingService);
    }

    @Test
    void givenUnauthenticated_whenCreateDriver_returnUnauthorizedOrForbidden_dependsOnConfig() throws Exception {
        // given
        var dto = new CreateNewDriverRequestDto(ALIAS);

        // when and then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().is3xxRedirection());

        verifyNoInteractions(racingService);
    }

}
