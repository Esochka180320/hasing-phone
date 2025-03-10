package com.vilka.hashing_service.init;

import com.vilka.hashing_service.model.PhoneHash;
import com.vilka.hashing_service.repository.PhoneHashRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Ініціалізація бази даних тестовими значеннями телефонів та їхніх хешів.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private PhoneHashRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${batch.size}")
    private int batchSize; // Зчитування розміру пакета

    @Value("${totalCount}")
    private int totalCount; // кількість номерів

    @Value("${phone.prefixes}")
    private String[] prefixes; // Зчитування префіксів телефонних номерів

    @Value("${hashing.salt}")
    private String salt; // Сіль для хешування

    @Value("${hashing.algorithm}")
    private String algorithm; // Алгоритм хешування

    private static final AtomicInteger counter = new AtomicInteger(0);

    private static String[] staticPrefixes;

    @PostConstruct
    public void init() {
        staticPrefixes = prefixes;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            generateAndSavePhoneHashesInParallel(totalCount);
        }
    }

    /**
     * Генерації та збереження номерів та хешів.
     */
    private void generateAndSavePhoneHashesInParallel(int totalCount) {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors * 2);
        int batchCount = totalCount / batchSize;

        for (int i = 0; i < batchCount; i++) {
            int start = i * batchSize;
            int end = (i + 1) * batchSize;
            executorService.submit(() -> generateAndSaveBatch(start, end));
        }

        executorService.shutdown();
    }

    /**
     * Генерує унікальні телефонні номери, хешує їх та зберігає в БД.
     */
    private void generateAndSaveBatch(int start, int end) {
        List<PhoneHash> batch = new ArrayList<>();

        try {
            for (int i = start; i < end; i++) {
                String phoneNumber = generateUniquePhoneNumber();
                String hash = hashPhoneNumber(phoneNumber);

                PhoneHash phoneHash = new PhoneHash();
                phoneHash.setPhoneNumber(phoneNumber);
                phoneHash.setPhoneHash(hash);
                batch.add(phoneHash);
            }

            insertBatch(batch);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Помилка при хешуванні номерів: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Генерує унікальний номер телефону.
     */
    public static String generateUniquePhoneNumber() {
        int number = counter.incrementAndGet();
        String prefix = staticPrefixes[number % staticPrefixes.length];

        return prefix + String.format("%07d", number % 10000000);
    }

    /**
     * Пакетне вставлення згенерованих телефонних номерів у базу даних.
     */
    private void insertBatch(List<PhoneHash> batch) {
        jdbcTemplate.batchUpdate("INSERT INTO phone_hash (phone_hash, phone_number) VALUES(?, ?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                preparedStatement.setString(1, batch.get(i).getPhoneHash());
                preparedStatement.setString(2, batch.get(i).getPhoneNumber());
            }

            @Override
            public int getBatchSize() {
                return batch.size();
            }
        });
    }

    /**
     * Хешує телефонний номер з додаванням солі.
     */
    public String hashPhoneNumber(String phoneNumber) throws NoSuchAlgorithmException {
        String saltedPhoneNumber = phoneNumber + salt;
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] hashBytes = digest.digest(saltedPhoneNumber.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
