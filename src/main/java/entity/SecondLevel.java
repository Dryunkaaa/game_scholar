package entity;

import controller.AbstractController;
import controller.level_controller.LevelResultDisplayController;
import controller.level_controller.SecondLevelController;
import domain.ReplaceableCharacter;
import domain.Word;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import service.jpa.WordService;
import start.Main;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SecondLevel extends Level {

    protected Word selectedWord;

    protected ReplaceableCharacter replaceableChar;

    protected List<Word> words;

    protected Label wordLabel;

    public SecondLevel(int countOfQuestions, Label currentQuestionNumber,
                       Label[] answers, Label wordLabel, ImageView correctAnswerImage, ImageView wrongAnswerImage) {
        super(countOfQuestions, currentQuestionNumber, answers, correctAnswerImage, wrongAnswerImage);
        this.wordLabel = wordLabel;
    }

    @Override
    public void start() {
        initWords();
        prepareDataToShow();
    }

    @Override
    public void restart() {
        new SecondLevelController().show();
    }

    protected void initWords() {
        WordService wordService = WordService.getInstance();
        words = wordService.getAllWithOneMissedChar();
    }

    @Override
    protected void checkAnswer() {
        //сброс событий нажатия, дабы счетчик номера вопроса не изменялся во время паузы
        AbstractController.resetLabelEvents(this.getAnswers());
        wordLabel.setText(selectedWord.getValue());

        Main.service.submit(() -> {

            if (isRightAnswer()) { // выбран правильный елемент
                int countOfRightAnswers = this.getRightAnswersCount();
                this.setRightAnswersCount(countOfRightAnswers + 1);
                Platform.runLater(() -> wordLabel.setTextFill(Color.GREEN));
            } else {
                this.getErrorList().add(selectedWord.getValue());
                Platform.runLater(() -> wordLabel.setTextFill(Color.RED));
            }

            pause();

            Platform.runLater(() -> {

                if (this.getCurrentQuestionPosition() + 1 >= this.getQuestionsCount()) {
                    new LevelResultDisplayController(this).show();
                } else {
                    // возврат событий мыши
                    addEventsToLabels(this.getAnswers());
                    this.setCurrentQuestionPosition(this.getCurrentQuestionPosition() + 1);
                    wordLabel.setTextFill(Color.WHITE);
                    prepareDataToShow();
                }

            });
        });
    }

    @Override
    protected void prepareData() {
        Word word = getRandomWord();
        int missingCharacterIndex = 0;

        for (ReplaceableCharacter replaceableCharacter : word.getReplaceableCharacters()) {
            missingCharacterIndex = replaceableCharacter.getCharacterIndex();
            replaceableChar = replaceableCharacter;
        }

        wordLabel.setText(StringUtils.deleteStringCharacter(word.getValue(), missingCharacterIndex));

        String rightCharacter = String.valueOf(word.getValue().charAt(missingCharacterIndex));
        String replaceableCharacter = replaceableChar.getLetter().getValue();

        List<String> characters = new ArrayList<>();
        characters.add(rightCharacter);
        characters.add(replaceableCharacter);

        labelAssignment(characters);
    }

    protected Word getRandomWord() {
        Random random = new Random();
        int index = random.nextInt(words.size());
        Word result = words.get(index);
        words.remove(index);
        selectedWord = result;
        return result;
    }

    private boolean isRightAnswer() {
        String text = this.getSelectedAnswer().getText();
        String character = replaceableChar.getLetter().getValue();
        if (!text.equals(character)) return true;
        return false;
    }
}
