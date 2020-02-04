package controller;

import controller.fill_controller.DataFillController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends AbstractController implements Initializable {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button entryButton;

    @FXML
    private Label errorLabel;

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

        entryButton.setOnAction(event -> {

            String login = loginField.getText();
            String password = passwordField.getText();

            if (login.equals("admin") && password.equals("admin"))
                new DataFillController().show();
            else
                errorLabel.setVisible(true);
        });
    }

    @Override
    public void show() {
        loadControllerWindow(this, "fxml/login.fxml");
    }
}
