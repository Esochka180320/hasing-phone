package com.vilka.hashing_service.service;

import com.vilka.hashing_service.repository.PhoneHashRepository;
import com.vilka.hashing_service.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Сервіс для обробки хешів телефонних номерів та пошуку їх у базі даних.
 */
@Service
public class HashingService {

    @Autowired
    private PhoneHashRepository repository;

    @Autowired
    private Validation validation;

    /**
     * Отримання хешу за номером телефону.
     */
    public String getHash(String phoneNumber) {
        if (validation.isNotValidPhoneNumber(phoneNumber)) {
            return "Невірний номер";
        }
        String hash = repository.findByPhoneNumber(phoneNumber).getPhoneHash();
        if (hash == null) {
            return "Не знайдено хеш";
        }
        return hash;
    }

    /**
     * Отримання номеру телефону за його хешем.
     */
    public String getPhoneNumber(String hash) {
        if (validation.isNull(hash)) {
            return "Невірний хеш";
        }
        String phoneNumber = repository.findByPhoneHash(hash).getPhoneNumber();
        if (phoneNumber == null) {
            return "Не знайдено номер";
        }
        return phoneNumber;
    }
}
