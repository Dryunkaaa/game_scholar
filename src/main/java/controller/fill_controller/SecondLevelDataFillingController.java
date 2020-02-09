package controller.fill_controller;

import controller.AbstractController;
import domain.Letter;
import domain.Word;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import service.jpa.*;

public class SecondLevelDataFillingController extends AbstractController implements Initializable {

    @FXML
    private MenuItem mainMenuItem;

    @FXML
    private MenuItem closeItem;

    @FXML
    private MenuItem loginItem;

    @FXML
    private MenuItem infoItem;

    @FXML
    private TextField wordField;

    @FXML
    private TextField charactersField;

    @FXML
    private Button backButton;

    @FXML
    private TextField indexesField;

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
            String[] partsOfIndexes = indexesField.getText().split("\\s+");
            String[] partsOfCharacters = charactersField.getText().split("\\s+");

            if (partsOfIndexes.length == partsOfCharacters.length && allFieldsContainsData()) {

                WordService wordService = WordService.getInstance();
                LetterService letterService = LetterService.getInstance();
                ReplaceableCharacterService replaceableCharacterService = ReplaceableCharacterService.getInstance();

                String wordValue = wordField.getText();
                wordService.create(wordValue);
                Word word1 = wordService.getByValue(wordValue);

                for (int i = 0; i < partsOfCharacters.length; i++) {
                    letterService.create(partsOfCharacters[i]);
                    Letter letter = letterService.getByValue(partsOfCharacters[i]);
                    int index = Integer.parseInt(partsOfIndexes[i]);
                    replaceableCharacterService.create(word1, letter, index);
                }
            }

            resetTextFieldsData();
        });
    }

    private void resetTextFieldsData() {
        wordField.setText("");
        indexesField.setText("");
        charactersField.setText("");
    }

    private boolean allFieldsContainsData() {
        if (wordField.getText().isEmpty() || indexesField.getText().isEmpty() ||
                charactersField.getText().isEmpty()) {

            return false;
        }
        return true;
    }

    @Override
    public void show() {
        loadControllerWindow(this, "fxml/fill/fillSecondLevel.fxml");
    }
}
