package com.vilka.hashing_service;

import com.vilka.hashing_service.init.DataInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class DataInitializerTest {

    @Autowired
    private DataInitializer dataInitializer;

    @Test
    void testHashPhoneNumber() throws NoSuchAlgorithmException {
        String phoneNumber = "380501234567";
        String hash1 = dataInitializer.hashPhoneNumber(phoneNumber);
        String hash2 = dataInitializer.hashPhoneNumber(phoneNumber);

        assertEquals(hash1, hash2);
        assertNotNull(hash1);
        assertFalse(hash1.isEmpty());
    }

    @Test
    void testGenerateUniquePhoneNumber() {
        String phone1 = DataInitializer.generateUniquePhoneNumber();
        String phone2 = DataInitializer.generateUniquePhoneNumber();

        assertNotNull(phone1);
        assertNotNull(phone2);
        assertNotEquals(phone1, phone2);
        assertTrue(phone1.matches("\\d{12}"));
    }
}