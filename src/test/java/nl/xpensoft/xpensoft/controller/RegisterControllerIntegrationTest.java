package nl.xpensoft.xpensoft.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
class RegisterControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
        //Saving a new user should succeed
    void saveNewUserIsSuccessful() throws Exception {
        String user = "{\"email\":\"user1@domain.com\",\"password\":\"validPassword\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(2)
        //Saving a user whose email already exists in the database should yield a 409 error
    void saveNewUserWhenEmailAlreadyExistsYields409Error() throws Exception {
        String user = "{\"email\":\"user1@domain.com\",\"password\":\"validPassword\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @Order(3)
        //Saving a user without email should yield 400 error
    void saveNewUserWithoutEmailYields400Error() throws Exception {
        String user = "{\"email\":\"\",\"password\":\"validPassword\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(4)
        //Saving a user with invalid email should yield 400 error
    void saveNewUserWithInvalidEmailYields400Error() throws Exception {
        String user = "{\"email\":\"user1domaincom\",\"password\":\"validPassword\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(5)
        //Saving a user with too long email should yield 400 error
    void saveNewUserWithTooLongEmailYields400Error() throws Exception {
        String user = "{\"email\":\"user1xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx@domain.com\",\"password\":\"validPassword\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(6)
        //Saving a user without password should yield 400 error
    void saveNewUserWithoutPasswordYields400Error() throws Exception {
        String user = "{\"email\":\"user1@domain.com\",\"password\":\"\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(7)
        //Saving a user with too long password should yield 400 error
    void saveNewUserWithTooLongPasswordYields400Error() throws Exception {
        String user = "{\"email\":\"user1@domain.com\",\"password\":\"invalidPasswordxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(8)
        //Saving another new user should succeed
    void saveAnotherUserIsSuccessful() throws Exception {
        String user = "{\"email\":\"user2@domain.com\",\"password\":\"validPassword\"}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/register")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
}