package controller.level_controller;

import controller.AbstractController;
import entity.ThirdLevel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class ThirdLevelController extends AbstractController implements Initializable {

    @FXML
    private Label title;

    @FXML
    private Label word;

    @FXML
    private Label firstAnswer;

    @FXML
    private Label secondAnswer;

    @FXML
    private Label currentQuestionNumber;

    @FXML
    private MenuItem mainMenuItem;

    @FXML
    private MenuItem closeItem;

    @FXML
    private MenuItem loginItem;

    @FXML
    private MenuItem infoItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handleMenuBarEvents(closeItem, loginItem, mainMenuItem, infoItem);

        Label[] answers = {firstAnswer, secondAnswer};

        new ThirdLevel( 10, currentQuestionNumber, answers, word,
                null, null).start();
    }

    @Override
    public void show() {
        loadControllerWindow(this, "fxml/level/thirdLevel.fxml");
    }
}
