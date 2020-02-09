package entity;

import application.App;
import controller.level_controller.LevelResultDisplayController;
import controller.level_controller.SecondLevelController;
import domain.ReplaceableCharacter;
import domain.Word;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import service.jpa.WordService;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SecondLevel extends Level {

    protected Word selectedWord;

    protected ReplaceableCharacter replaceableChar;

    protected List<Word> words;

    protected Label wordLabel;

    public SecondLevel(int countOfQuestions, Label currentQuestionNumber,
                       Label[] answers, Label wordLabel, ImageView correctAnswerImage, ImageView wrongAnswerImage) {
        super(countOfQuestions, currentQuestionNumber, answers, correctAnswerImage, wrongAnswerImage);
        this.wordLabel = wordLabel;
        words = WordService.getInstance().getAllWithOneMissedChar();
    }

    @Override
    public void restart() {
        new SecondLevelController().show();
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

        distributeLabelsData(characters);
    }

    @Override
    protected void checkAnswer() {
        this.setWaitForAnswer(false);
        wordLabel.setText(selectedWord.getValue());

        App.service.submit(() -> {

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

                if (this.getQuestionPosition() + 1 >= this.getQuestionsCount()) {
                    new LevelResultDisplayController(this).show();
                } else {
                    this.setWaitForAnswer(true);
                    this.setQuestionPosition(this.getQuestionPosition() + 1);
                    wordLabel.setTextFill(Color.WHITE);
                    initLevelView();
                }

            });
        });
    }

    protected Word getRandomWord() {
        int index = random.nextInt(words.size());
        selectedWord = words.get(index);
        words.remove(index);
        return selectedWord;
    }

    private boolean isRightAnswer() {
        String text = this.getSelectedAnswer().getText();
        String character = replaceableChar.getLetter().getValue();
        if (!text.equals(character)) return true;
        return false;
    }
}
