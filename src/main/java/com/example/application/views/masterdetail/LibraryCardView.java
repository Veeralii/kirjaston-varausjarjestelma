package com.example.application.views.masterdetail;

import com.example.application.data.LibraryCard;
import com.example.application.services.LibraryCardService;
import com.example.application.services.SamplePersonService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import org.springframework.beans.factory.annotation.Autowired;

@Route("kirjastokortit")
@PageTitle("Kirjastokortit")
public class LibraryCardView extends VerticalLayout {

    private final LibraryCardService libraryCardService;
    private final SamplePersonService samplePersonService;

    private TextField ownerIdField;
    private final TextField cardNumberField = new TextField("Kortin numero");

    private final Button saveButton = new Button("Tallenna");
    private final Button deleteButton = new Button("Poista");
    private final Button clearButton = new Button("Tyhjennä");

    private final Grid<LibraryCard> grid = new Grid<>(LibraryCard.class, false);
    private final BeanValidationBinder<LibraryCard> binder = new BeanValidationBinder<>(LibraryCard.class);

    private LibraryCard currentLibraryCard;

    @Autowired
    public LibraryCardView(LibraryCardService libraryCardService, SamplePersonService samplePersonService) {
        this.libraryCardService = libraryCardService;
        this.samplePersonService = samplePersonService;

        // Grid
        grid.addColumn(LibraryCard::getId).setHeader("Kortin ID");
        grid.addColumn(LibraryCard::getCardNumber).setHeader("Kortin numero");
        grid.addColumn(LibraryCard::getOwnerId).setHeader("Omistajan ID");
        grid.setItems(libraryCardService.getAllLibraryCards());

        grid.asSingleSelect().addValueChangeListener(event -> {
            currentLibraryCard = event.getValue();
            binder.readBean(currentLibraryCard);
        });

        // Lomake
        FormLayout formLayout = new FormLayout();
        formLayout.add(cardNumberField);

        ownerIdField = new TextField("Omistajan ID");
        formLayout.add(ownerIdField);

        // Napit
        saveButton.addClickListener(e -> tallennaKirjastokortti());
        deleteButton.addClickListener(e -> poistaKirjastokortti());
        clearButton.addClickListener(e -> tyhjennaLomake());

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, deleteButton, clearButton);

        add(formLayout, buttonLayout, grid);

        binder.bindInstanceFields(this);
    }

    private void tallennaKirjastokortti() {
        if (cardNumberField.isEmpty()) {
            Notification.show("Kortin numero on pakollinen");
            return;
        }

        try {
            if (currentLibraryCard == null) {
                currentLibraryCard = new LibraryCard();
            }

            currentLibraryCard.setCardNumber(cardNumberField.getValue());

            // Käytetään ownerIdFieldin arvoa, ei kirjautumista
            String ownerIdText = ownerIdField.getValue();
            if (!ownerIdText.isEmpty()) {
                currentLibraryCard.setOwnerId(Long.parseLong(ownerIdText));  // Asetetaan omistajan ID kentästä
            }

            // Tallenna kortti
            libraryCardService.save(currentLibraryCard);
            Notification.show("Kirjastokortti tallennettu onnistuneesti!");

            // Päivitä grid
            grid.setItems(libraryCardService.getAllLibraryCards());

        } catch (Exception e) {
            Notification.show("Virhe tallennuksessa.");
        }
    }

    private void poistaKirjastokortti() {
        if (currentLibraryCard != null && currentLibraryCard.getId() != null) {
            libraryCardService.delete(currentLibraryCard.getId());
            Notification.show("Kirjastokortti poistettu.");
            paivitaGridi();
            tyhjennaLomake();
        } else {
            Notification.show("Valitse poistettava kortti.");
        }
    }

    private void tyhjennaLomake() {
        binder.readBean(null);
        currentLibraryCard = null;
    }

    private void paivitaGridi() {
        grid.setItems(libraryCardService.getAllLibraryCards());
    }
}
