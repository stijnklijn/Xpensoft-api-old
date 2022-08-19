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
@Order(3)
class TransactionControllerIntegrationTest {

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
        //Saving a new transaction without authentication should yield 401 error
    void saveNewTransactionWithoutAuthenticationYields401Error() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"transaction\",\"header\":{\"id\":1},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Order(2)
        //Saving a new transaction is successful
    void saveNewTransactionIsSuccessful() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"transaction\",\"header\":{\"id\":1},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(3)
        //Saving a new transaction without date yields 400 error
    void saveNewTransactionWithoutDateYields400Error() throws Exception {
        String transaction = "{\"date\":\"\",\"description\":\"transaction\",\"header\":{\"id\":1},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(4)
        //Saving a new transaction without description yields 400 error
    void saveNewTransactionWithoutDescriptionYields400Error() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"\",\"header\":{\"id\":1},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(5)
        //Saving a new transaction with too long description yields 400 error
    void saveNewTransactionWithTooLongDescriptionYields400Error() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"transactionxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx\",\"header\":{\"id\":1},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(6)
        //Saving a new transaction without header yields 400 error
    void saveNewTransactionWithoutHeaderYields400Error() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"transaction\",\"header\":null,\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(7)
        //Saving a new transaction without amount yields 400 error
    void saveNewTransactionWithoutAmountYields400Error() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"transaction\",\"header\":{\"id\":1},\"amount\":null}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(8)
        //Saving a new transaction with too low amount yields 400 error
    void saveNewTransactionWithTooLowAmountYields400Error() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"transaction\",\"header\":{\"id\":1},\"amount\":0.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(9)
        //Saving a new transaction with too high amount yields 400 error
    void saveNewTransactionWithTooHighAmountYields400Error() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"transaction\",\"header\":{\"id\":1},\"amount\":1000000.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Order(10)
        //Saving a new transaction with non existing header yields 404 error
    void saveNewTransactionWithNonExistingHeaderYields404Error() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"transaction\",\"header\":{\"id\":5},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(11)
        //Saving a new transaction with header of another user yields 404 error
    void saveNewTransactionWithHeaderOfAnotherUserYields404Error() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"transaction\",\"header\":{\"id\":3},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(12)
        //Saving another transaction is successful
    void saveAnotherTransactionIsSuccessful() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"transaction2\",\"header\":{\"id\":1},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(13)
        //Saving new transaction for different user is successful
    void saveNewTransactionForDifferentUserIsSuccessful() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"transaction1\",\"header\":{\"id\":3},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user2))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(14)
        //Saving another transaction for different user is successful
    void saveAnotherTransactionForDifferentUserIsSuccessful() throws Exception {
        String transaction = "{\"date\":\"2000-01-01\",\"description\":\"transaction2\",\"header\":{\"id\":3},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user2))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Order(15)
        //Updating transaction with POST request yields 403 error
    void updateTransactionWithPostRequestYields403Error() throws Exception {
        String transaction = "{\"id\":1,\"date\":\"2000-01-01\",\"description\":\"transaction1\",\"header\":{\"id\":1},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Order(16)
        //Updating transaction is successful
    void updateTransactionIsSuccessful() throws Exception {
        String transaction = "{\"id\":1,\"date\":\"2000-01-01\",\"description\":\"transaction1\",\"header\":{\"id\":1},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(17)
        //Updating non existing transaction yields 404 error
    void updateNonExistingTransactionYields404Error() throws Exception {
        String transaction = "{\"id\":5,\"date\":\"2000-01-01\",\"description\":\"transaction1\",\"header\":{\"id\":1},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(18)
        //Updating transaction of another user yields 404 error
    void updateTransactionOfAnotherUserYields404Error() throws Exception {
        String transaction = "{\"id\":3,\"date\":\"2000-01-01\",\"description\":\"transaction1\",\"header\":{\"id\":1},\"amount\":100.00}";
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/transactions")
                        .content(transaction)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(19)
        //Get transactions is successful
    void getTransactionsIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/transactions")
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("[{\"id\":1,\"date\":\"2000-01-01\",\"description\":\"transaction1\",\"header\":{\"id\":1,\"name\":\"header1\",\"income\":true},\"amount\":100.0},{\"id\":2,\"date\":\"2000-01-01\",\"description\":\"transaction2\",\"header\":{\"id\":1,\"name\":\"header1\",\"income\":true},\"amount\":100.0}]"));
    }

    @Test
    @Order(20)
        //Delete transaction is successful
    void deleteTransactionIsSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/transactions/2")
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(21)
        //Delete non existing transaction yields 404 error
    void deleteNonExistingTransactionYields404Error() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/transactions/2")
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Order(22)
        //Delete transaction of another user yields 404 error
    void deleteTransactionOfAnotherUserYields404Error() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/transactions/3")
                        .with(SecurityMockMvcRequestPostProcessors.user(new SecurityUser(user1))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}