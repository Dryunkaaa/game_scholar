package controller.level_controller;

import controller.AbstractController;
import entity.FirstLevel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class FirstLevelController extends AbstractController implements Initializable {

    @FXML
    private Label question;

    @FXML
    private Label firstAnswer;

    @FXML
    private Label secondAnswer;

    @FXML
    private Label thirdAnswer;

    @FXML
    private Label fourthAnswer;

    @FXML
    private ImageView correctAnswerImage;

    @FXML
    private ImageView wrongAnswerImage;

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

        Label[] answers = {firstAnswer, secondAnswer, thirdAnswer, fourthAnswer};

        new FirstLevel(10, currentQuestionNumber, answers,
                correctAnswerImage, wrongAnswerImage).initLevelView();
    }

    @Override
    public void show() {
        loadControllerWindow(this, "fxml/level/firstLevel.fxml");
    }
}

