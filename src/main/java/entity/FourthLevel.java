package entity;

import controller.level_controller.FourthLevelController;
import domain.Author;
import domain.Quote;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import service.jpa.AuthorService;
import service.jpa.QuoteService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FourthLevel extends Level {

    private List<Quote> quotes;
    private List<Author> authors;
    private Quote selectedQuote;
    private Label quoteLabel;

    public FourthLevel(int countOfQuestions, Label numberOfTheCurrentQuestion,
                       Label[] answers, Label quoteLabel, ImageView correctAnswerImage, ImageView wrongAnswerImage) {
        super(countOfQuestions, numberOfTheCurrentQuestion, answers, correctAnswerImage, wrongAnswerImage);
        this.quoteLabel = quoteLabel;
    }

    @Override
    public void start() {
        initQuotes();
        prepareDataToShow();
    }

    @Override
    public void restart() {
        new FourthLevelController().show();
    }

    @Override
    protected void prepareData() {
        initAuthors();
        Quote quote = getRandomQuote();
        String answerText = quote.getAuthor().getFirstName() + " " + quote.getAuthor().getLastName();
        this.setResponseText(answerText);
        quoteLabel.setText(quote.getValue());
        labelAssignment(getDataToFill());
    }

    private List<String> getDataToFill() {
        List<String> result = new ArrayList<>();
        List<Author> listOfAuthors = getRandomAnswers(selectedQuote.getAuthor());
        for (Author author : listOfAuthors) {
            String authorFullName = author.getFirstName() + " " + author.getLastName();
            result.add(authorFullName);
        }
        return result;
    }

    private List<Author> getRandomAnswers(Author rightAuthor) {
        Random random = new Random();
        List<Author> result = new ArrayList<>();
        result.add(rightAuthor);
        removeRightAnswerFromList(rightAuthor);
        // так как всего 4 варианта ответа и правильный уже добавлен
        int numberOfAnswersToAdd = this.getAnswers().length - 1;
        for (int i = 0; i < numberOfAnswersToAdd; i++) {
            int index = random.nextInt(authors.size());
            result.add(authors.get(index));
            authors.remove(index);
        }
        return result;
    }

    private void removeRightAnswerFromList(Author author) {
        for (int i = 0; i < authors.size(); i++) {
            if (authors.get(i).getLastName().equals(author.getLastName())) {
                authors.remove(i);
                break;
            }
        }
    }

    private void initQuotes() {
        QuoteService quoteService = QuoteService.getInstance();
        quotes = quoteService.getAll();
    }

    private void initAuthors() {
        AuthorService authorService = AuthorService.getInstance();
        authors = authorService.getAll();
    }

    private Quote getRandomQuote() {
        Random random = new Random();
        int index = random.nextInt(quotes.size());
        Quote result = quotes.get(index);
        quotes.remove(index);
        selectedQuote = result;
        return result;
    }

}
