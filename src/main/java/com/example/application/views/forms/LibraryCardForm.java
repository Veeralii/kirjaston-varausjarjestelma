package com.example.application.views.forms;

import com.example.application.data.LibraryCard;
import com.example.application.services.LibraryCardService;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class LibraryCardForm extends VerticalLayout {

    private final TextField cardNumberField = new TextField("Card Number");
    private final TextField ownerIdField = new TextField("Owner ID");

    private LibraryCard libraryCard;

    private final LibraryCardService service;

    public LibraryCardForm(LibraryCardService service) {
        this.service = service;

        FormLayout formLayout = new FormLayout();
        formLayout.add(cardNumberField, ownerIdField);

        Button saveButton = new Button("Save", e -> saveCard());
        Button cancelButton = new Button("Cancel", e -> clearForm());

        add(formLayout, saveButton, cancelButton);
    }

    public void setLibraryCard(LibraryCard libraryCard) {
        this.libraryCard = libraryCard;
        cardNumberField.setValue(libraryCard.getCardNumber());
        ownerIdField.setValue(libraryCard.getOwnerId().toString());
    }

    private void saveCard() {
        if (libraryCard == null) {
            libraryCard = new LibraryCard();
        }

        libraryCard.setCardNumber(cardNumberField.getValue());
        libraryCard.setOwnerId(Long.parseLong(ownerIdField.getValue()));

        service.save(libraryCard);
        Notification.show("Library Card saved");
    }

    private void clearForm() {
        cardNumberField.clear();
        ownerIdField.clear();
    }
}
