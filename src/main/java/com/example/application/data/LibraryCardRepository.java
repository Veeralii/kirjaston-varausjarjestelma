package com.example.application.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryCardRepository extends JpaRepository<LibraryCard, Long> {

    // Etsii kirjastokortin numeron perusteella
    LibraryCard findByCardNumber(String cardNumber);

    // Etsii kirjastokortin omistajan id perusteella
    LibraryCard findByOwnerId(Long ownerId);
}
