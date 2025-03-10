package com.vilka.hashing_service.controller;

import com.vilka.hashing_service.service.HashingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Отримання хешу або номера", description = "Контролер для отримання інформації (хешу або номера)")
public class HashingController {

    @Autowired
    private HashingService hashingService;

    @GetMapping("/getHash")
    @Operation(summary = "Отримання хешу", description = "Отримання хешу з бд")
    public String getHash(@RequestParam(name = "phoneNumber") String phoneNumber) {
        return hashingService.getHash(phoneNumber);
    }

    @GetMapping("/getPhoneNumber")
    @Operation(summary = "Отримання номера", description = "Отримання номера з бд")
    public String getPhoneNumber(@RequestParam(name = "hash") String hash) {
        return hashingService.getPhoneNumber(hash);
    }
}
