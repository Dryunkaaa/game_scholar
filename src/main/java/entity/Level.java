package entity;

import controller.level_controller.LevelResultDisplayController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;
import application.App;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public abstract class Level {

    private int questionsCount;

    private int rightAnswersCount = 0;

    private int currentQuestionPosition = 0;

    private String responseText;

    private List<String> errorList = new ArrayList<>();

    private Label selectedAnswer;

    private Label labelWithRightAnswer;

    private Label[] answers;

    private Label currentQuestionNumber;

    private ImageView correctAnswerImage;

    private ImageView wrongAnswerImage;

    private boolean waitForAnswer = true;

    public Level(int questionsCount, Label currentQuestionNumber, Label[] answers,
                 ImageView correctAnswerImage, ImageView wrongAnswerImage) {
        this.correctAnswerImage = correctAnswerImage;
        this.wrongAnswerImage = wrongAnswerImage;
        this.questionsCount = questionsCount;
        this.answers = answers;
        this.currentQuestionNumber = currentQuestionNumber;
        addEventsToLabels(answers);
    }

    protected void checkAnswer() {
        this.waitForAnswer = false;
        correctAnswerImage.setVisible(true);

        App.service.submit(() -> {
            searchLabelWithRightAnswer(responseText, answers);

            if (selectedAnswer == labelWithRightAnswer) {
                rightAnswersCount++;
                Platform.runLater(() -> correctAnswerImage.setLayoutY(selectedAnswer.getLayoutY()));
            } else {
                errorList.add(labelWithRightAnswer.getText());

                Platform.runLater(() -> {
                    wrongAnswerImage.setVisible(true);
                    correctAnswerImage.setLayoutY(labelWithRightAnswer.getLayoutY());
                    wrongAnswerImage.setLayoutY(selectedAnswer.getLayoutY());
                });
            }

            pause();

            Platform.runLater(() -> {

                if (currentQuestionPosition + 1 >= questionsCount) {
                    new LevelResultDisplayController(this).show();
                } else {
                    waitForAnswer = true;
                    currentQuestionPosition++;
                    correctAnswerImage.setVisible(false);
                    wrongAnswerImage.setVisible(false);
                    prepareDataToShow();
                }

            });
        });
    }

    protected void prepareDataToShow() {
        String currentQuestion = (this.getCurrentQuestionPosition() + 1) + "/" + this.getQuestionsCount();
        currentQuestionNumber.setText(currentQuestion);
        prepareData();
    }


    protected abstract void prepareData();

    private void addEventsToLabels(Label[] labels) {
        for (Label label : labels) {
            label.setOnMouseClicked(event -> {
                if (waitForAnswer){
                    this.selectedAnswer = label;
                    checkAnswer();
                }
            });
        }
    }

    protected Label searchLabelWithRightAnswer(String text, Label[] labels) {
        for (Label label : labels) {
            if (label.getText().equals(text)) {
                labelWithRightAnswer = label;
                return label;
            }
        }
        return new Label();
    }

    protected void labelAssignment(List<String> dataToFill) {
        List<Integer> validIndexes = createValidIndexes(this.getAnswers().length);
        Random random = new Random();

        // рандомное присвоение значений лейблам
        for (int i = 0; i < this.getAnswers().length; i++) {
            int index = random.nextInt(validIndexes.size());
            this.getAnswers()[validIndexes.get(index)].setText(dataToFill.get(i));
            validIndexes.remove(index);
        }
    }

    public abstract void start();

    public void pause() {
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> createValidIndexes(int countOfIndexes) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < countOfIndexes; i++) {
            indexes.add(i);
        }
        return indexes;
    }

    public abstract void restart();
}

