package controller;

import controller.level_controller.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPageController extends AbstractController implements Initializable {

    @FXML
    private ImageView firstLevel;

    @FXML
    private ImageView secondLevel;

    @FXML
    private ImageView thirdLevel;

    @FXML
    private ImageView fourthLevel;

    @FXML
    private ImageView fifthLevel;

    @FXML
    private MenuItem closeItem;

    @FXML
    private MenuItem loginItem;

    @FXML
    private MenuItem infoItem;

    @FXML
    private MenuItem mainMenuItem;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handleMenuBarEvents(closeItem, loginItem, mainMenuItem, infoItem);

        firstLevel.setOnMouseClicked(event -> new FirstLevelController().show());

        secondLevel.setOnMouseClicked(event -> new SecondLevelController().show());

        thirdLevel.setOnMouseClicked(event -> new ThirdLevelController().show());

        fourthLevel.setOnMouseClicked(event -> new FourthLevelController().show());

        fifthLevel.setOnMouseClicked(event -> new FifthLevelController().show());
    }

    @Override
    public void show() {
        loadControllerWindow(this, "fxml/main.fxml");
    }
}
