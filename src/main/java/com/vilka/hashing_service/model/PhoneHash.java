package com.vilka.hashing_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Модель для зберігання телефонних номерів та їхніх хешів у базі даних.
 */
@Setter
@Getter
@Entity
public class PhoneHash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "VARCHAR(256)", nullable = false)
    private String phoneNumber;

    @Column(columnDefinition = "VARCHAR(256)", nullable = false)
    private String phoneHash;

}