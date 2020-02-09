package entity;

import controller.level_controller.LevelResultDisplayController;
import controller.level_controller.ThirdLevelController;
import domain.ReplaceableCharacter;
import domain.Word;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import service.jpa.ReplaceableCharacterService;
import service.jpa.WordService;
import application.App;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ThirdLevel extends SecondLevel {

    private List<ReplaceableCharacter> characters = new ArrayList<>();

    private List<ReplaceableCharacter> excludedCharsList = new ArrayList<>();

    private int missingCharacterPosition = 0;

    public ThirdLevel(int countOfQuestions, Label currentQuestionLabel, Label[] answers,
                      Label wordLabel, ImageView correctAnswerImage, ImageView wrongAnswerImage) {
        super(countOfQuestions, currentQuestionLabel, answers, wordLabel, correctAnswerImage, wrongAnswerImage);
        words = WordService.getInstance().getAllWithTwoOrMoreMissedChars();
    }

    @Override
    protected void prepareData() {
        int rightIndex = characters.get(missingCharacterPosition).getCharacterIndex();
        String rightCharacter = String.valueOf(selectedWord.getValue().charAt(rightIndex));
        String replaceableCharacter = characters.get(missingCharacterPosition).getLetter().getValue();

        List<String> listOfCharacters = new ArrayList<>();
        listOfCharacters.add(rightCharacter);
        listOfCharacters.add(replaceableCharacter);

        distributeLabelsData(listOfCharacters);
    }

    @Override
    public void initLevelView() {
        String currentQuestion = (this.getQuestionPosition() + 1) + "/" + this.getQuestionsCount();
        this.getCurrentQuestionNumber().setText(currentQuestion);
        fillWordLabelText();
        prepareData();
    }

    @Override
    public void restart() {
        new ThirdLevelController().show();
    }

    @Override
    protected void checkAnswer() {
        App.service.submit(() -> {

            if (isRightAnswer()) {

                // если выбраный символ был последним в данном слове
                if (missingCharacterPosition == characters.size() - 1) {
                    int countOfRightAnswers = this.getRightAnswersCount();
                    this.setRightAnswersCount(countOfRightAnswers + 1);
                    checkFullAnswer(true);
                } else {
                    excludedCharsList.add(characters.get(missingCharacterPosition));

                    Platform.runLater(() -> {

                        String text = StringUtils.deleteStringCharacter(selectedWord.getValue(),
                                getIndicesArrayMissingChars(excludedCharsList));

                        wordLabel.setText(text);
                        missingCharacterPosition++;
                        prepareData();
                    });
                }

            } else {
                this.getErrorList().add(selectedWord.getValue());
                checkFullAnswer(false);
            }
        });
    }

    private void fillWordLabelText() {
        Word word = getRandomWord();
        initListOfReplaceableCharacters(word);
        wordLabel.setText(StringUtils.deleteStringCharacter(word.getValue(), getIndicesArrayMissingChars()));
    }

    private void initListOfReplaceableCharacters(Word word) {
        characters.clear();
        excludedCharsList.clear();
        missingCharacterPosition = 0;

        for (ReplaceableCharacter character : word.getReplaceableCharacters()) {
            characters.add(character);
        }

        ReplaceableCharacterService.getInstance().sortByIndexNumber(characters);
    }

    private int[] getIndicesArrayMissingChars() {
        int[] array = new int[characters.size()];
        int index = 0;

        for (ReplaceableCharacter character : characters) {
            array[index] = character.getCharacterIndex();
            index++;
        }
        return array;
    }

    private int[] getIndicesArrayMissingChars(List<ReplaceableCharacter> excludedList) {
        int[] array = new int[characters.size() - excludedList.size()];

        List<ReplaceableCharacter> temp = new ArrayList<>();
        temp.addAll(characters);
        temp.removeAll(excludedList);

        for (int i = 0; i < temp.size(); i++) {
            array[i] = temp.get(i).getCharacterIndex();
        }

        return array;
    }

    private void checkFullAnswer(boolean rightAnswer) {
        Platform.runLater(() -> {
            this.setWaitForAnswer(false);
            wordLabel.setText(selectedWord.getValue());

            if (rightAnswer) wordLabel.setTextFill(Color.GREEN);
            else wordLabel.setTextFill(Color.RED);
        });

        pause();

        Platform.runLater(() -> {
            this.setWaitForAnswer(true);

            if (this.getQuestionPosition() + 1 >= this.getQuestionsCount()) {
                new LevelResultDisplayController(this).show();
            } else {
                this.setQuestionPosition(this.getQuestionPosition() + 1);
                wordLabel.setTextFill(Color.WHITE);
                initLevelView();
            }

        });
    }

    private boolean isRightAnswer() {
        int index = characters.get(missingCharacterPosition).getCharacterIndex();
        String character = String.valueOf(selectedWord.getValue().charAt(index));

        if (this.getSelectedAnswer().getText().equals(character)) return true;
        return false;
    }
}
