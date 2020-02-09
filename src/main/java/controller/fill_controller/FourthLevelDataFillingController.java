package controller.fill_controller;

import controller.AbstractController;
import domain.Author;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import service.jpa.AuthorService;
import service.jpa.BookService;
import service.jpa.QuoteService;

import java.net.URL;
import java.util.ResourceBundle;

public class FourthLevelDataFillingController extends AbstractController implements Initializable {


    @FXML
    private MenuItem mainMenuItem;

    @FXML
    private MenuItem closeItem;

    @FXML
    private MenuItem loginItem;

    @FXML
    private MenuItem infoItem;

    @FXML
    private TextField authorNameField;

    @FXML
    private TextField bookField;

    @FXML
    private Button backButton;

    @FXML
    private TextField quoteField;

    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handleMenuBarEvents(closeItem, loginItem, mainMenuItem, infoItem);
        backButton.setOnAction(event -> new DataFillController().show());
        handleSaveEvent();
    }

    @FXML
    private void handleSaveEvent() {
        saveButton.setOnAction(event -> {
            if (!authorNameField.getText().isEmpty()) {
                AuthorService authorService = AuthorService.getInstance();

                String[] nameParts = authorNameField.getText().split("\\s+");
                // в поле имени запрещено вписывать 3 слова
                if (nameParts.length > 2) {
                    return;
                }

                String name = "";
                String lastName = "";

                // если поле имени хранит только 1 слово, то это фамилия
                if (nameParts.length == 1) {
                    lastName = nameParts[0];
                } else {
                    name = nameParts[0];
                    lastName = nameParts[1];
                }

                authorService.create(name, lastName);
                Author author = authorService.getByLastName(lastName);

                if (!quoteField.getText().isEmpty()) {
                    QuoteService quoteService = QuoteService.getInstance();
                    quoteService.create(quoteField.getText(), author);
                }

                if (!bookField.getText().isEmpty()) {
                    BookService bookService = BookService.getInstance();
                    bookService.create(bookField.getText(), author);
                }

                resetTextFieldsData();
            }
        });
    }

    private void resetTextFieldsData() {
        quoteField.setText("");
        bookField.setText("");
    }

    @Override
    public void show() {
        loadControllerWindow(this, "fxml/fill/fillFourthLevel.fxml");
    }
}
