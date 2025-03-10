package com.vilka.hashing_service.validation;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Validation {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^380\\d{9}$");

    public boolean isNotValidPhoneNumber(String phoneNumber) {
        return !PHONE_PATTERN.matcher(phoneNumber).matches() && isNull(phoneNumber);
    }

    public boolean isNull(String phoneNumberOrHash) {
        return phoneNumberOrHash == null && phoneNumberOrHash.isEmpty();
    }

}
