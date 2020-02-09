package entity;

import controller.level_controller.FirstLevelController;
import domain.CorrectlySpelledWord;
import domain.MisspelledWord;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import service.jpa.CorrectlySpelledWordService;
import service.jpa.MisspelledWordService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FirstLevel extends Level {

    private CorrectlySpelledWord selectedWord;

    private List<CorrectlySpelledWord> correctlySpelledWords;

    public FirstLevel(int countOfQuestions, Label currentQuestionLabel, Label[] answers,
                      ImageView correctAnswerImage, ImageView wrongAnswerImage) {
        super(countOfQuestions, currentQuestionLabel, answers, correctAnswerImage, wrongAnswerImage);
    }

    @Override
    public void start() {
        initResponseWords();
        prepareDataToShow();
    }

    @Override
    public void restart() {
        new FirstLevelController().show();
    }

    private void initResponseWords() {
        CorrectlySpelledWordService correctlySpelledWordService = CorrectlySpelledWordService.getInstance();
        correctlySpelledWords = correctlySpelledWordService.getAllWords();
    }

    private CorrectlySpelledWord getRandomWord() {
        Random random = new Random();
        int index = random.nextInt(correctlySpelledWords.size());
        CorrectlySpelledWord result = correctlySpelledWords.get(index);
        correctlySpelledWords.remove(index);
        selectedWord = result;
        this.setResponseText(selectedWord.getValue());
        return result;
    }

    @Override
    protected void prepareData() {
        MisspelledWordService misspelledWordService = MisspelledWordService.getInstance();
        CorrectlySpelledWord correctlySpelledWord = getRandomWord();
        List<MisspelledWord> dependentWords = misspelledWordService.getAllWordsByOwner(correctlySpelledWord);
        List<String> answersToDisplay = new ArrayList<>();
        for (MisspelledWord misspelledWord : dependentWords) {
            answersToDisplay.add(misspelledWord.getValue());
        }
        answersToDisplay.add(correctlySpelledWord.getValue());
        labelAssignment(answersToDisplay);
    }
}
