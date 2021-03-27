package application;

import controller.MainPageController;
import domain.Word;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;
import storage.AbstractDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {

    @Getter
    private static Stage window;

    public static ExecutorService service = Executors.newFixedThreadPool(15);

    public static void main(String[] args) {
        // initial connect to DB
        service.submit(() -> new AbstractDao<>(Word.class));
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        new MainPageController().show();
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> System.exit(0));
    }
}
