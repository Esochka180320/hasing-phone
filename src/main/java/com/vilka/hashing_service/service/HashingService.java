package com.vilka.hashing_service.service;

import com.vilka.hashing_service.model.PhoneHash;
import com.vilka.hashing_service.repository.PhoneHashRepository;
import com.vilka.hashing_service.validation.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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
        PhoneHash phoneHash = repository.findByPhoneNumber(phoneNumber);
        if (phoneHash == null) {
            return "Не знайдено хеш";
        }
        return phoneHash.getPhoneHash();
    }

    /**
     * Отримання номеру телефону за його хешем.
     */
    public String getPhoneNumber(String hash) {
        if (validation.isNull(hash)) {
            return "Невірний хеш";
        }

        List<PhoneHash> phoneHashes = repository.findByPhoneHash(hash);

        if (phoneHashes.isEmpty()) {
            return "Не знайдено номер";
        }

        return phoneHashes.stream()
                .map(PhoneHash::getPhoneNumber)
                .collect(Collectors.joining(", "));
    }
}
