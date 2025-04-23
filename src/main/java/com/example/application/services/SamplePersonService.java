package com.example.application.services;

import com.example.application.data.SamplePerson;
import com.example.application.data.SamplePersonRepository;
import com.example.application.data.LibraryCard;
import com.example.application.services.LibraryCardService;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class SamplePersonService {

    private final SamplePersonRepository repository;
    private final LibraryCardService libraryCardService;
    public SamplePersonService(SamplePersonRepository repository, LibraryCardService libraryCardService) {
        this.repository = repository;
        this.libraryCardService = libraryCardService;
    }

    // Henkilön hakeminen id perusteella
    public Optional<SamplePerson> get(Long id) {
        return repository.findById(id);
    }

    // Henkilön tallentaminen
    public SamplePerson save(SamplePerson entity) {
        return repository.save(entity);
    }

    // Henkilön poistaminen
    public void delete(Long id) {
        repository.deleteById(id);
    }

    // Henkilöiden listaus
    public Page<SamplePerson> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // Henkilöiden listaus suodatus
    public Page<SamplePerson> list(Pageable pageable, Specification<SamplePerson> filter) {
        return repository.findAll(filter, pageable);
    }

    // Henkilöiden määrä
    public int count() {
        return (int) repository.count();
    }

    // Kirjastokortin lisääminen henkilölle (en tehnyt loppuun)
    public void addLibraryCardToPerson(Long personId, String cardNumber) {
        Optional<SamplePerson> personOpt = repository.findById(personId);
        if (personOpt.isPresent()) {
            SamplePerson person = personOpt.get();

            // Luo uusi kirjastokortti ja liitä se henkilöön
            LibraryCard libraryCard = new LibraryCard();
            libraryCard.setCardNumber(cardNumber);
            libraryCard.setOwnerId(person.getId());  // Liittää kortin henkilön ID:hen

            // Tallenna kirjastokortti
            libraryCardService.save(libraryCard);
        }
    }

    // Kirjastokortin poistaminen henkilön id ja kortin numerolla
    public void deleteLibraryCard(Long personId, String cardNumber) {
        Optional<SamplePerson> personOpt = repository.findById(personId);
        if (personOpt.isPresent()) {
            // Hae kirjastokortti kortin numeron perusteella
            LibraryCard card = libraryCardService.findByCardNumber(cardNumber);
            if (card != null && card.getOwnerId().equals(personId)) {
                libraryCardService.delete(card.getId());
            }
        }
    }
}
