package com.simon.bank.controller;

import com.google.gson.Gson;
import com.simon.bank.services.dto.OpenAccountDTO;
import liquibase.pro.packaged.O;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.SerializationUtils;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@TestPropertySource("classpath:application.properties")
@SpringBootTest
class AccountControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void givenGetCustomersCall_whenGetCustomers_thenGetUsersFromDatabase() throws Exception {
        OpenAccountDTO openAccountDTO = new OpenAccountDTO(1L, BigDecimal.valueOf(10.5));
        Gson gson = new Gson();
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/open-account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(openAccountDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").exists())
                .andReturn();
    }
}