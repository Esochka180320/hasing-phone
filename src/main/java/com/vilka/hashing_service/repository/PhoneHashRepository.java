package com.vilka.hashing_service.repository;

import com.vilka.hashing_service.model.PhoneHash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneHashRepository extends JpaRepository<PhoneHash, Long> {
    PhoneHash findByPhoneNumber(String phoneNumber);

    PhoneHash findByPhoneHash(String hash);
}
