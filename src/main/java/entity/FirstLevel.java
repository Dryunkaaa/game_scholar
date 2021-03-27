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

public class FirstLevel extends Level {

    private List<CorrectlySpelledWord> correctlyWords;

    public FirstLevel(int countOfQuestions, Label currentQuestionLabel, Label[] answers,
                      ImageView correctAnswerImage, ImageView wrongAnswerImage) {
        super(countOfQuestions, currentQuestionLabel, answers, correctAnswerImage, wrongAnswerImage);
        correctlyWords = CorrectlySpelledWordService.getInstance().getAllWords();
    }

    @Override
    public void restart() {
        new FirstLevelController().show();
    }

    @Override
    protected void prepareData() {
        MisspelledWordService misspelledWordService = MisspelledWordService.getInstance();
        CorrectlySpelledWord correctWord = getRandomWord();

        List<MisspelledWord> dependentWords = misspelledWordService.getAllWordsByOwner(correctWord);
        List<String> answerList = new ArrayList<>();

        for (MisspelledWord misspelledWord : dependentWords) {
            answerList.add(misspelledWord.getValue());
        }

        answerList.add(correctWord.getValue());
        distributeLabelsData(answerList);
    }

    private CorrectlySpelledWord getRandomWord() {
        int index = random.nextInt(correctlyWords.size());

        CorrectlySpelledWord selectedWord = correctlyWords.get(index);
        correctlyWords.remove(index);
        this.setResponseText(selectedWord.getValue());

        return selectedWord;
    }
}
