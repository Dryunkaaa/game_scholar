package entity;

import controller.AbstractController;
import controller.level_controller.LevelResultDisplayController;
import domain.ReplaceableCharacter;
import domain.Word;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import service.jpa.ReplaceableCharacterService;
import service.jpa.WordService;
import start.Main;
import utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ThirdLevel extends SecondLevel {

    private List<ReplaceableCharacter> characters;

    private List<ReplaceableCharacter> excludedCharsList;

    private int missingCharacterPosition = 0;

    public ThirdLevel(int countOfQuestions, Label currentQuestionLabel, Label[] answers,
                      Label wordLabel, ImageView correctAnswerImage, ImageView wrongAnswerImage) {
        super(countOfQuestions, currentQuestionLabel, answers, wordLabel, correctAnswerImage, wrongAnswerImage);
    }

    @Override
    public void start() {
        characters = new ArrayList<>();
        excludedCharsList = new ArrayList<>();
        initWords();
        prepareDataToShow();
    }

    @Override
    protected void initWords() {
        WordService wordService = WordService.getInstance();
        words = wordService.getAllWithTwoOrMoreMissedChars();
    }

    @Override
    protected void prepareData() {
        int rightIndex = characters.get(missingCharacterPosition).getCharacterIndex();
        String rightCharacter = String.valueOf(selectedWord.getValue().charAt(rightIndex));
        String replaceableCharacter = characters.get(missingCharacterPosition).getLetter().getValue();

        List<String> listOfCharacters = new ArrayList<>();
        listOfCharacters.add(rightCharacter);
        listOfCharacters.add(replaceableCharacter);

        labelAssignment(listOfCharacters);
    }

    @Override
    protected void prepareDataToShow() {
        String currentQuestion = (this.getCurrentQuestionPosition() + 1) + "/" + this.getQuestionsCount();
        this.getCurrentQuestionNumber().setText(currentQuestion);
        fillWordLabelText();
        prepareData();
    }

    private void fillWordLabelText() {
        Word word = getRandomWord();
        initListOfReplaceableCharacters(word);
        wordLabel.setText(StringUtils.deleteStringCharacter(word.getValue(), getArrayOfIndicesOfMissingCharacters()));
    }

    private void initListOfReplaceableCharacters(Word word) {
        returnOfInitialValuesToFields();

        for (ReplaceableCharacter character : word.getReplaceableCharacters()){
            characters.add(character);
        }

        ReplaceableCharacterService characterService = ReplaceableCharacterService.getInstance();
        characterService.sortByIndexNumber(characters);
    }

    private void returnOfInitialValuesToFields() {
        characters.clear();
        excludedCharsList.clear();
        missingCharacterPosition = 0;
    }

    private int[] getArrayOfIndicesOfMissingCharacters() {
        int[] array = new int[characters.size()];
        int index = 0;

        for (ReplaceableCharacter character : characters) {
            array[index] = character.getCharacterIndex();
            index++;
        }
        return array;
    }

    private int[] getArrayOfIndicesOfMissingCharacters(List<ReplaceableCharacter> excludedList) {
        int[] array = new int[characters.size() - excludedList.size()];

        List<ReplaceableCharacter> temp = new ArrayList<>();
        temp.addAll(characters);
        temp.removeAll(excludedList);

        for (int i = 0; i < temp.size(); i++) {
            array[i] = temp.get(i).getCharacterIndex();
        }
        return array;
    }

    @Override
    protected void checkAnswer() {
        Main.service.submit(() -> {

            if (isRightAnswer()) {

                // если выбраный символ был последним в данном слове
                if (missingCharacterPosition == characters.size() - 1) {
                    int countOfRightAnswers = this.getRightAnswersCount();
                    this.setRightAnswersCount(countOfRightAnswers + 1);
                    eventOnFullAnswer(true);
                } else {
                    excludedCharsList.add(characters.get(missingCharacterPosition));

                    Platform.runLater(() -> {

                        String text = StringUtils.deleteStringCharacter(selectedWord.getValue(),
                                getArrayOfIndicesOfMissingCharacters(excludedCharsList));

                        wordLabel.setText(text);
                        missingCharacterPosition++;
                        prepareData();
                    });
                }

            } else {
                this.getErrorList().add(selectedWord.getValue());
                eventOnFullAnswer(false);
            }
        });
    }

    private void eventOnFullAnswer(boolean isRightAnswer) {
        Platform.runLater(() -> {
            AbstractController.resetLabelEvents(this.getAnswers());
            wordLabel.setText(selectedWord.getValue());
            if (isRightAnswer) wordLabel.setTextFill(Color.GREEN);
            else wordLabel.setTextFill(Color.RED);
        });

        pause();

        Platform.runLater(() -> {
            addEventsToLabels(this.getAnswers());

            if (this.getCurrentQuestionPosition() + 1 >= this.getQuestionsCount()) {
                new LevelResultDisplayController(this).show();
            } else {
                this.setCurrentQuestionPosition(this.getCurrentQuestionPosition() + 1);
                wordLabel.setTextFill(Color.WHITE);
                prepareDataToShow();
            }

        });
    }

    private boolean isRightAnswer() {
        String text = this.getSelectedAnswer().getText();
        int index = characters.get(missingCharacterPosition).getCharacterIndex();
        String character = String.valueOf(selectedWord.getValue().charAt(index));
        if (text.equals(character)) return true;
        return false;
    }
}
