package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import application.App;

import java.io.IOException;

public abstract class AbstractController {

    public abstract void show();

    public void loadControllerWindow(Initializable controller, String pathToFxml) {
        FXMLLoader loader = new FXMLLoader(ClassLoader.getSystemClassLoader().getResource(pathToFxml));
        loader.setController(controller);

        try {
            Parent root1 = loader.load();
            App.getWindow().setScene(new Scene(root1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleMenuBarEvents(MenuItem closeItem, MenuItem loginItem, MenuItem mainMenuItem, MenuItem infoItem) {
        mainMenuItem.setOnAction(event -> new MainPageController().show());

        infoItem.setOnAction(event -> new InfoController().show());

        loginItem.setOnAction(event -> new LoginController().show());

        closeItem.setOnAction(event -> System.exit(0));
    }

    public static void resetLabelEvents(Label[] labels) {
        for (Label label : labels) {
            label.setOnMouseClicked(null);
        }
    }
}
