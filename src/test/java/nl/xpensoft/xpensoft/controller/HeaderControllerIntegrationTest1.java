package nl.xpensoft.xpensoft.controller;

import nl.xpensoft.xpensoft.model.SecurityUser;
import nl.xpensoft.xpensoft.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Order(2)
class HeaderControllerIntegrationTest1 {

    @Autowired
    private MockMvc mockMvc;
    private User user1;
    private User user2;

    @BeforeAll
    void setup() {
        user1 = new User();
        user1.setId(1);
        user2 = new User();
        user2.setId(2);
    }

    @Test
    @Order(1)
        //Saving a new header without authentication should yield 401 error
    void saveNewHeaderWithoutAuthenticationYields401Error() throws Exception {
        String header = "{\"name\":\"header\",\"income\":true}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Order(2)
        //Saving a new header is successful
    void saveNewHeaderIsSuccessful() throws Exception {
        String header = "{\"name\":\"header\",\"income\":true}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(3)
        //Saving a new header without name should yield 400 error
    void saveNewHeaderWithoutNameYields400Error() throws Exception {
        String header = "{\"name\":\"\",\"income\":true}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(4)
        //Saving a new header with too long name should yield 400 error
    void saveNewHeaderWithTooLongNameYields400Error() throws Exception {
        String header = "{\"name\":\"headerxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\",\"income\":true}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(5)
        //Saving a new header when name already exists yields 409 error
    void saveNewHeaderWhenNameAlreadyExistsYields409Error() throws Exception {
        String header = "{\"name\":\"header\",\"income\":true}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @Order(6)
        //Saving another header is successful
    void saveAnotherHeaderIsSuccessful() throws Exception {
        String header = "{\"name\":\"header2\",\"income\":false}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(7)
        //Saving new header for different user is successful
    void saveNewHeaderForDifferentUserIsSuccessful() throws Exception {
        String header = "{\"name\":\"header1\",\"income\":true}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user2))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(8)
        //Saving another header for different user is successful
    void saveAnotherHeaderForDifferentUserIsSuccessful() throws Exception {
        String header = "{\"name\":\"header2\",\"income\":false}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user2))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(9)
        //Updating header with POST request yields 403 error
    void updateHeaderWithPostRequestYields403Error() throws Exception {
        String header = "{\"id\":1,\"name\":\"header1\",\"income\":false}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Order(10)
        //Updating header is successful
    void updateHeaderIsSuccessful() throws Exception {
        String header = "{\"id\":1,\"name\":\"header1\",\"income\":true}";
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(11)
        //Updating header name when name already exists yields 409 error
    void updateHeaderNameWhenNameAlreadyExistsYields409Error() throws Exception {
        String header = "{\"id\":1,\"name\":\"header2\",\"income\":false}";
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @Order(12)
        //Updating non existing header yields 404 error
    void updateNonExistingHeaderYields404Error() throws Exception {
        String header = "{\"id\":5,\"name\":\"header2\",\"income\":false}";
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(13)
        //Updating header of another user yields 404 error
    void updateHeaderOfAnotherUserYields404Error() throws Exception {
        String header = "{\"id\":3,\"name\":\"header2\",\"income\":false}";
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/headers")
                        .content(header)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(14)
        //Get headers is successful
    void getHeadersIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/headers")
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[{\"id\":1,\"name\":\"header1\",\"income\":true},{\"id\":2,\"name\":\"header2\",\"income\":false}]"));
    }
}