package com.example.application.services;

import com.example.application.data.LibraryCard;
import com.example.application.data.LibraryCardRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class LibraryCardService {

    private final LibraryCardRepository repository;

    public LibraryCardService(LibraryCardRepository repository) {
        this.repository = repository;
    }

    public LibraryCard save(LibraryCard card) {
        return repository.save(card);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public LibraryCard findByCardNumber(String cardNumber) {
        return repository.findByCardNumber(cardNumber);
    }

    public List<LibraryCard> getAllLibraryCards() {
        return repository.findAll();
    }

    public Optional<LibraryCard> get(Long id) {
        return repository.findById(id);
    }
}
