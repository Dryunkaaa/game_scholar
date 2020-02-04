package controller.fill_controller;

import controller.AbstractController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

import java.net.URL;
import java.util.ResourceBundle;

public class DataFillController extends AbstractController implements Initializable {

    @FXML
    private MenuItem mainMenuItem;

    @FXML
    private MenuItem closeItem;

    @FXML
    private MenuItem loginItem;

    @FXML
    private MenuItem infoItem;

    @FXML
    private Button firstLevelButton;

    @FXML
    private Button secondLevelButton;

    @FXML
    private Button fourthLevelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handleMenuBarEvents(closeItem, loginItem, mainMenuItem, infoItem);

        firstLevelButton.setOnAction(event -> new FirstLevelDataFillingController().show());

        secondLevelButton.setOnAction(event -> new SecondLevelDataFillingController().show());

        fourthLevelButton.setOnAction(event -> new FourthLevelDataFillingController().show());
    }

    @Override
    public void show() {
        loadControllerWindow(this, "fxml/fill/fillData.fxml");
    }
}
