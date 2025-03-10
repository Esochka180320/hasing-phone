package com.vilka.hashing_service.repository;

import com.vilka.hashing_service.model.PhoneHash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhoneHashRepository extends JpaRepository<PhoneHash, Long> {
    PhoneHash findByPhoneNumber(String phoneNumber);

    List<PhoneHash> findByPhoneHash(String hash);
}
