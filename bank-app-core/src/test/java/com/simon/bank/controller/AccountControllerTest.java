package com.simon.bank.controller;

import com.google.gson.Gson;
import com.simon.bank.services.AccountService;
import com.simon.bank.services.dto.OpenAccountDTO;
import com.simon.bank.services.exception.AccountNotCreatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@TestPropertySource("classpath:application.properties")
@SpringBootTest
class AccountControllerTest {

    @MockBean
    AccountService accountService;

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private final Gson gson = new Gson();


    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void testOpenNewAccount() throws Exception {
        OpenAccountDTO openAccountDTO = new OpenAccountDTO(1L, BigDecimal.valueOf(10.5));
        Gson myGson = new Gson();

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/open-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(myGson.toJson(openAccountDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    void testGetAccounts() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").exists())
                .andReturn();
    }

    @Test
    void testAccountNotCreatedException_whenOpenNewAccount() throws Exception {
        String message = "Exception";
        OpenAccountDTO openAccountDTO = new OpenAccountDTO(1L, BigDecimal.valueOf(10.5));
        when(accountService.openNewCurrentAccountAndSendTransfer(any(OpenAccountDTO.class)))
                .thenThrow(new AccountNotCreatedException(message));

        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/open-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(openAccountDTO)))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof AccountNotCreatedException))
                .andReturn();
    }
}