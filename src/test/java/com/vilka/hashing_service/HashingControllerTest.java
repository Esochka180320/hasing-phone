package com.vilka.hashing_service;

import com.vilka.hashing_service.controller.HashingController;
import com.vilka.hashing_service.service.HashingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class HashingControllerTest {

    @InjectMocks
    private HashingController hashingController;

    @Mock
    private HashingService hashingService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(hashingController).build();
    }

    @Test
    void testGetHash() throws Exception {
        String phoneNumber = "380671234567";
        String hash = "someHash123";

        when(hashingService.getHash(phoneNumber)).thenReturn(hash);

        mockMvc.perform(get("/api/getHash")
                        .param("phoneNumber", phoneNumber))
                .andExpect(status().isOk());
    }

    @Test
    void testGetPhoneNumber() throws Exception {
        String phoneNumber = "380671234567";
        String hash = "someHash123";

        when(hashingService.getPhoneNumber(phoneNumber)).thenReturn(hash);

        mockMvc.perform(get("/api/getPhoneNumber")
                        .param("hash", hash))
                .andExpect(status().isOk());
    }
}
