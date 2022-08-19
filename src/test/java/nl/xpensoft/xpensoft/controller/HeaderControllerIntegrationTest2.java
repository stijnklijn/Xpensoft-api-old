package nl.xpensoft.xpensoft.controller;

import nl.xpensoft.xpensoft.model.SecurityUser;
import nl.xpensoft.xpensoft.model.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
@Order(4)
class HeaderControllerIntegrationTest2 {

    @Autowired
    private MockMvc mockMvc;
    private User user1;

    @BeforeAll
    void setup() {
        user1 = new User();
        user1.setId(1);
    }

    @Test
    @Order(1)
        //Delete header is successful
    void deleteHeaderIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/headers/2")
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(2)
        //Delete non existing header yields 404 error
    void deleteNonExistingHeaderYields404Error() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/headers/2")
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(3)
        //Delete header of another user yields 404 error
    void deleteHeaderOfAnotherUserYields404Error() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/headers/4")
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(4)
        //Delete header which contains transactions yields 409 error
    void deleteHeaderWhichContainsTransactionsYields409Error() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/headers/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}