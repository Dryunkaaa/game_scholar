package controller.level_controller;

import controller.AbstractController;
import controller.MainPageController;
import entity.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import lombok.Setter;

import java.net.URL;
import java.util.ResourceBundle;

public class LevelResultDisplayController extends AbstractController implements Initializable {

    @FXML
    private Label title;

    @FXML
    private Button errorListDisplayButton;

    @FXML
    private Button restartLevelButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private TextArea errorArea;

    @FXML
    private ImageView firstStar;

    @FXML
    private ImageView secondStar;

    @FXML
    private ImageView thirdStar;

    @FXML
    private ImageView fourthStar;

    @FXML
    private ImageView fifthStar;

    @Setter
    private Level level;

    private static final int STARS_COUNT = 5;

    private ImageView[] stars;

    public LevelResultDisplayController(Level level) {
        this.level = level;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stars = new ImageView[]{firstStar, secondStar, thirdStar, fourthStar, fifthStar};

        title.setText(title.getText() + level.getRightAnswersCount() + "/" + level.getQuestionsCount() + ".");
        mainMenuButton.setOnAction(event -> new MainPageController().show());

        displayStars();
        displayErrorList();
        handleLevelRestartEvent();
    }

    @FXML
    private void displayErrorList() {
        errorListDisplayButton.setOnAction(event -> {
            String errors = "";
            for (int i = 0; i < level.getErrorList().size(); i++) {
                if (i == 0) errors += level.getErrorList().get(i);
                else errors += "\n" + level.getErrorList().get(i);
            }
            if (level.getErrorList().size() == 0) errorArea.setText("Ошибок нет.");
            else errorArea.setText(errors);
            errorArea.setVisible(true);
        });
    }

    @FXML
    private void handleLevelRestartEvent() {
        restartLevelButton.setOnAction(event -> {
            if (level.getClass().equals(FirstLevel.class)) {
                new FirstLevelController().show();
            } else if (level.getClass().equals(SecondLevel.class)) {
                new SecondLevelController().show();
            } else if (level.getClass().equals(ThirdLevel.class)) {
                new ThirdLevelController().show();
            } else if (level.getClass().equals(FourthLevel.class)) {
                new FourthLevelController().show();
            } else if (level.getClass().equals(FifthLevel.class)) {
                new FifthLevelController().show();
            }
        });
    }

    @FXML
    private void displayStars() {
        int starsReceivedNumber = level.getRightAnswersCount() * STARS_COUNT / level.getQuestionsCount();
        int inactiveStarsNumber = STARS_COUNT - starsReceivedNumber;
        for (int i = STARS_COUNT - inactiveStarsNumber; i < STARS_COUNT; i++) {
            stars[i].setOpacity(0.1);
        }
    }

    @Override
    public void show() {
        loadControllerWindow(this, "fxml/level/levelResult.fxml");
    }
}

