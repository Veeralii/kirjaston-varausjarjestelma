package com.example.application.views;

import com.example.application.data.SamplePerson;
import com.example.application.services.SamplePersonService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.springframework.beans.factory.annotation.Autowired;

@Route("person")
@PageTitle("Person View")
public class PersonView extends VerticalLayout {

    private final SamplePersonService samplePersonService;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private TextField phoneField;
    private TextField occupationField;
    private Button saveButton;

    @Autowired
    public PersonView(SamplePersonService samplePersonService) {
        this.samplePersonService = samplePersonService;

        firstNameField = new TextField("Etunimi");
        lastNameField = new TextField("Sukunimi");
        emailField = new TextField("Sähköposti");
        phoneField = new TextField("Puhelinnumero");
        occupationField = new TextField("Ammatti");
        saveButton = new Button("Tallenna muutokset", e -> savePerson());

        add(firstNameField, lastNameField, emailField, phoneField, occupationField, saveButton);

        Long ownerId = (Long) UI.getCurrent().getSession().getAttribute("ownerId");

        if (ownerId != null) {
            loadPersonData(ownerId);
        }
    }

    private void loadPersonData(Long ownerId) {
        // Haetaan henkilön tiedot samplepersonservicestä
        samplePersonService.get(ownerId).ifPresent(person -> {
            firstNameField.setValue(person.getFirstName());
            lastNameField.setValue(person.getLastName());
            emailField.setValue(person.getEmail());
            phoneField.setValue(person.getPhone());
            occupationField.setValue(person.getOccupation());
        });
    }

    private void savePerson() {
        // Tallentaa muutokset personservice palveluun
        String firstName = firstNameField.getValue();
        String lastName = lastNameField.getValue();
        String email = emailField.getValue();
        String phone = phoneField.getValue();
        String occupation = occupationField.getValue();

        // Haetaan henkilö tietokannasta
        Long ownerId = (Long) UI.getCurrent().getSession().getAttribute("ownerId");
        SamplePerson person = samplePersonService.get(ownerId).orElse(null);

        if (person != null) {
            // Päivitä olemassa olevan person tiedot
            person.setFirstName(firstName);
            person.setLastName(lastName);
            person.setEmail(email);
            person.setPhone(phone);
            person.setOccupation(occupation);

            // Tallenna päivitetty person takaisin tietokantaan
            samplePersonService.save(person);

            Notification.show("Muutokset tallennettu");

            // Navigoidaan takaisin päänäkymään
            UI.getCurrent().navigate("");
        } else {
            Notification.show("Henkilöä ei löytynyt.");
        }
    }
}
