package controller.fill_controller;

import controller.AbstractController;
import domain.CorrectlySpelledWord;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import service.jpa.CorrectlySpelledWordService;
import service.jpa.MisspelledWordService;

public class FirstLevelDataFillingController extends AbstractController implements Initializable {

    @FXML
    private MenuItem mainMenuItem;

    @FXML
    private MenuItem closeItem;

    @FXML
    private MenuItem loginItem;

    @FXML
    private MenuItem infoItem;

    @FXML
    private TextField firstField;

    @FXML
    private TextField thirdField;

    @FXML
    private TextField fourthField;

    @FXML
    private Button backButton;

    @FXML
    private TextField secondField;

    @FXML
    private Button saveButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handleMenuBarEvents(closeItem, loginItem, mainMenuItem, infoItem);
        handleSaveEvent();
        backButton.setOnAction(event -> new DataFillController().show());
    }

    @FXML
    private void handleSaveEvent() {
        saveButton.setOnAction(event -> {
            if (allFieldsContainsData()) {
                CorrectlySpelledWordService correctlySpelledWordService = CorrectlySpelledWordService.getInstance();
                MisspelledWordService misspelledWordService = MisspelledWordService.getInstance();

                correctlySpelledWordService.create(firstField.getText());
                CorrectlySpelledWord correctlyWord = correctlySpelledWordService.getByValue(firstField.getText());

                // не сохранять если слово хранит другие варианты написания
                if (correctlyWord.getMisspelledWords().size() != 3) {
                    misspelledWordService.create(secondField.getText(), correctlyWord);
                    misspelledWordService.create(thirdField.getText(), correctlyWord);
                    misspelledWordService.create(fourthField.getText(), correctlyWord);
                }

                resetTextFieldsData();
            }
        });
    }

    private void resetTextFieldsData() {
        firstField.setText("");
        secondField.setText("");
        thirdField.setText("");
        fourthField.setText("");
    }

    private boolean allFieldsContainsData() {
        if (firstField.getText().isEmpty() || secondField.getText().isEmpty()
                || thirdField.getText().isEmpty() || fourthField.getText().isEmpty()) {

            return false;
        }
        return true;
    }

    @Override
    public void show() {
        loadControllerWindow(this, "fxml/fill/fillFirstLevel.fxml");
    }
}
