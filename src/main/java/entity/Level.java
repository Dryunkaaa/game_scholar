package entity;

import application.App;
import controller.level_controller.LevelResultDisplayController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public abstract class Level {

    private int questionsCount;

    private int rightAnswersCount = 0;

    private int questionPosition = 0;

    private String responseText;

    private List<String> errorList = new ArrayList<>();

    private Label selectedAnswer;

    private Label labelWithRightAnswer;

    private Label[] answers;

    private Label currentQuestionNumber;

    private ImageView correctAnswerImage;

    private ImageView wrongAnswerImage;

    private boolean waitForAnswer = true;

    protected Random random = new Random();

    public Level(int questionsCount, Label currentQuestionNumber, Label[] answers,
                 ImageView correctAnswerImage, ImageView wrongAnswerImage) {
        this.correctAnswerImage = correctAnswerImage;
        this.wrongAnswerImage = wrongAnswerImage;
        this.questionsCount = questionsCount;
        this.answers = answers;
        this.currentQuestionNumber = currentQuestionNumber;
        addLabelEvents(answers);
    }

    public abstract void restart();

    protected abstract void prepareData();

    protected void checkAnswer() {
        this.waitForAnswer = false;
        correctAnswerImage.setVisible(true);

        App.service.submit(() -> {
            getLabelWithRightAnswer(responseText, answers);

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

                if (questionPosition + 1 >= questionsCount) {
                    new LevelResultDisplayController(this).show();
                } else {
                    waitForAnswer = true;
                    questionPosition++;
                    correctAnswerImage.setVisible(false);
                    wrongAnswerImage.setVisible(false);
                    initLevelView();
                }

            });
        });
    }

    public void initLevelView() {
        String currentQuestion = (this.getQuestionPosition() + 1) + "/" + this.getQuestionsCount();
        currentQuestionNumber.setText(currentQuestion);
        prepareData();
    }

    private void addLabelEvents(Label[] labels) {
        for (Label label : labels) {
            label.setOnMouseClicked(event -> {
                if (waitForAnswer){
                    this.selectedAnswer = label;
                    checkAnswer();
                }
            });
        }
    }

    protected Label getLabelWithRightAnswer(String answerText, Label[] labels) {
        for (Label label : labels) {
            if (label.getText().equals(answerText)) {
                labelWithRightAnswer = label;
                return label;
            }
        }
        return new Label();
    }

    protected void distributeLabelsData(List<String> dataToFill) {
        List<Integer> validIndices = createValidIndexes(this.getAnswers().length);

        // рандомное присвоение значений лейблам
        for (int i = 0; i < this.getAnswers().length; i++) {
            int index = random.nextInt(validIndices.size());
            this.getAnswers()[validIndices.get(index)].setText(dataToFill.get(i));
            validIndices.remove(index);
        }
    }

    public void pause() {
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private List<Integer> createValidIndexes(int countOfIndexes) {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < countOfIndexes; i++) {
            indexes.add(i);
        }
        return indexes;
    }
}

