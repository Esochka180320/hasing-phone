package com.vilka.hashing_service;


import com.vilka.hashing_service.model.PhoneHash;
import com.vilka.hashing_service.repository.PhoneHashRepository;
import com.vilka.hashing_service.service.HashingService;
import com.vilka.hashing_service.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HashingServiceTest {

    @Mock
    private PhoneHashRepository repository;

    @Mock
    private Validation validation;

    @InjectMocks
    private HashingService hashingService;

    private final String validPhoneNumber = "+380671234567";
    private final String validHash = "abcd1234";

    private PhoneHash phoneHash;

    @BeforeEach
    void setUp() {
        phoneHash = new PhoneHash();
        phoneHash.setPhoneNumber(validPhoneNumber);
        phoneHash.setPhoneHash(validHash);
    }

    @Test
    void testGetHash_ValidPhoneNumber_ReturnsHash() {
        when(validation.isNotValidPhoneNumber(validPhoneNumber)).thenReturn(false);
        when(repository.findByPhoneNumber(validPhoneNumber)).thenReturn(phoneHash);

        String result = hashingService.getHash(validPhoneNumber);

        assertEquals(validHash, result);
        verify(repository, times(1)).findByPhoneNumber(validPhoneNumber);
    }

    @Test
    void testGetHash_InvalidPhoneNumber_ReturnsErrorMessage() {
        when(validation.isNotValidPhoneNumber(validPhoneNumber)).thenReturn(true);

        String result = hashingService.getHash(validPhoneNumber);

        assertEquals("Невірний номер", result);
        verify(repository, never()).findByPhoneNumber(anyString());
    }

    @Test
    void testGetHash_HashNotFound_ReturnsErrorMessage() {
        when(validation.isNotValidPhoneNumber(validPhoneNumber)).thenReturn(false);
        when(repository.findByPhoneNumber(validPhoneNumber)).thenReturn(null);

        String result = hashingService.getHash(validPhoneNumber);

        assertEquals("Не знайдено хеш", result);
    }

    @Test
    void testGetPhoneNumber_ValidHash_ReturnsPhoneNumber() {
        when(validation.isNull(validHash)).thenReturn(false);
        when(repository.findByPhoneHash(validHash)).thenReturn(List.of(phoneHash));

        String result = hashingService.getPhoneNumber(validHash);

        assertEquals(validPhoneNumber, result);
        verify(repository, times(1)).findByPhoneHash(validHash);
    }

    @Test
    void testGetPhoneNumber_HashNotFound_ReturnsErrorMessage() {
        when(validation.isNull(validHash)).thenReturn(false);
        when(repository.findByPhoneHash(validHash)).thenReturn(List.of());

        String result = hashingService.getPhoneNumber(validHash);

        assertEquals("Не знайдено номер", result);
    }

    @Test
    void testGetPhoneNumber_NullHash_ReturnsErrorMessage() {
        when(validation.isNull(null)).thenReturn(true);

        String result = hashingService.getPhoneNumber(null);

        assertEquals("Невірний хеш", result);
        verify(repository, never()).findByPhoneHash(anyString());
    }

    @Test
    void testGetPhoneNumber_MultipleMatches_ReturnsCommaSeparatedNumbers() {
        PhoneHash phoneHash2 = new PhoneHash();
        phoneHash2.setPhoneNumber("+380501234567");
        phoneHash2.setPhoneHash(validHash);

        when(validation.isNull(validHash)).thenReturn(false);
        when(repository.findByPhoneHash(validHash)).thenReturn(List.of(phoneHash, phoneHash2));

        String result = hashingService.getPhoneNumber(validHash);

        assertEquals("+380671234567, +380501234567", result);
    }
}