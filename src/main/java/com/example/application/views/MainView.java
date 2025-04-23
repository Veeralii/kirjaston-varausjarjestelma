package com.example.application.views;

import com.example.application.data.LibraryCard;
import com.example.application.services.LibraryCardService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import com.vaadin.flow.component.UI;


@Route("")
@PageTitle("Main View")
public class MainView extends VerticalLayout {

    private final LibraryCardService libraryCardService;

    private TextField cardNumberField;
    private Span resultLabel;

    @Autowired
    public MainView(LibraryCardService libraryCardService) {
        this.libraryCardService = libraryCardService;

        H1 header = new H1("Tervetuloa Kirjastokorttisovellukseen");
        add(header);

        cardNumberField = new TextField("Kirjastokortin numero");
        resultLabel = new Span();

        Button searchButton = new Button("Hae kortti", e -> searchCard());
        Button deleteButton = new Button("Poista kortti", e -> deleteCard());

        add(cardNumberField, searchButton, deleteButton, resultLabel);
    }

    private void searchCard() {
        String cardNumber = cardNumberField.getValue();
        LibraryCard card = libraryCardService.findByCardNumber(cardNumber);
        if (card != null) {
            resultLabel.setText("Löytyi kortti. ID: " + card.getId() + ", Omistajan ID: " + card.getOwnerId());

            // Tallenna ownerId sessioniin
            UI.getCurrent().getSession().setAttribute("ownerId", card.getOwnerId());

            // Luo "Muokkaa henkilötietoja" nappi
            Button editButton = new Button("Muokkaa henkilötietoja", e -> {
                // Navigoi PersonViewiin, joka on se näkymä henkilön tietojen muokkaamista varten
                getUI().ifPresent(ui -> ui.navigate("person"));
            });

            // Lisää nappi näkymään
            add(editButton);
        } else {
            resultLabel.setText("Korttia ei löytynyt.");
        }
    }

    private void deleteCard() {
        String cardNumber = cardNumberField.getValue();
        LibraryCard card = libraryCardService.findByCardNumber(cardNumber);
        if (card != null) {
            libraryCardService.delete(card.getId());
            Notification.show("Kortti poistettu.");
            resultLabel.setText("");
        } else {
            Notification.show("Korttia ei löytynyt.");
        }
    }
    private void navigateToPersonView(Long ownerId) {
        // Navigoi PersonViewiin, ja välitä ownerId:n arvo
        getUI().ifPresent(ui -> ui.navigate("person/" + ownerId));
    }
}
