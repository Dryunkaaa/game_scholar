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

public class FourthLevel extends Level {

    private List<Quote> quotes;
    private List<Author> authors;
    private Quote selectedQuote;
    private Label quoteLabel;

    public FourthLevel(int countOfQuestions, Label numberOfTheCurrentQuestion,
                       Label[] answers, Label quoteLabel, ImageView correctAnswerImage, ImageView wrongAnswerImage) {
        super(countOfQuestions, numberOfTheCurrentQuestion, answers, correctAnswerImage, wrongAnswerImage);
        this.quoteLabel = quoteLabel;
        quotes = QuoteService.getInstance().getAll();
    }

    @Override
    public void restart() {
        new FourthLevelController().show();
    }

    @Override
    protected void prepareData() {
        authors = AuthorService.getInstance().getAll();

        Quote quote = getRandomQuote();
        String answerText = quote.getAuthor().getFirstName() + " " + quote.getAuthor().getLastName();

        this.setResponseText(answerText);
        quoteLabel.setText(quote.getValue());

        distributeLabelsData(getAuthorNameList());
    }

    private List<String> getAuthorNameList() {
        List<String> authorNameList = new ArrayList<>();
        List<Author> authorList = getRandomAnswersList(selectedQuote.getAuthor());

        for (Author author : authorList) {
            String authorFullName = author.getFirstName() + " " + author.getLastName();
            authorNameList.add(authorFullName);
        }

        return authorNameList;
    }

    private List<Author> getRandomAnswersList(Author rightAuthor) {
        List<Author> authorList = new ArrayList<>();
        authorList.add(rightAuthor);

        authors.remove(rightAuthor);
        // так как всего 4 варианта ответа и правильный уже добавлен
        int numberOfAnswersToAdd = this.getAnswers().length - 1;

        for (int i = 0; i < numberOfAnswersToAdd; i++) {
            int index = random.nextInt(authors.size());
            authorList.add(authors.get(index));
            authors.remove(index);
        }

        return authorList;
    }

    private Quote getRandomQuote() {
        int index = random.nextInt(quotes.size());

        selectedQuote = quotes.get(index);
        quotes.remove(index);
        return selectedQuote;
    }

}
