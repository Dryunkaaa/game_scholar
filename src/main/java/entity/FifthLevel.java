package entity;

import controller.level_controller.FifthLevelController;
import domain.Author;
import domain.Book;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import service.jpa.AuthorService;
import service.jpa.BookService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FifthLevel extends Level {

    private List<Book> listOfBooks;
    private List<Author> listOfAuthors;
    private List<Book> validBooks;
    private Label titleLabel;

    public FifthLevel(int countOfQuestions, Label currentQuestionNumber,
                      Label[] answers, Label titleLabel, ImageView correctAnswerImage, ImageView wrongAnswerImage) {
        super(countOfQuestions, currentQuestionNumber, answers,correctAnswerImage, wrongAnswerImage);
        this.titleLabel = titleLabel;
    }

    @Override
    public void start() {
        initLists();
        prepareDataToShow();
    }

    @Override
    public void restart() {
        new FifthLevelController().show();
    }

    private void initLists() {
        BookService bookService = BookService.getInstance();
        AuthorService authorService = AuthorService.getInstance();
        listOfBooks = bookService.getAll();
        // список авторов у которых 3 и более книг так как полей для ответа 4 и 1 из них - не книга автора
        listOfAuthors = authorService.getAllWithThreeOrMoreBooks();
        validBooks = new ArrayList<>();
    }

    @Override
    protected void prepareData() {
        Author author = getRandomAuthor();
        titleLabel.setText("Автором какой книги не является " + author.getFirstName() + " " + author.getLastName());
        labelAssignment(getDataToFill(author));
    }

    private Author getRandomAuthor() {
        Random random = new Random();
        int index = random.nextInt(listOfAuthors.size());
        Author result = listOfAuthors.get(index);
        listOfAuthors.remove(index);
        return result;
    }

    private List<String> getDataToFill(Author author) {
        List<String> result = new ArrayList<>();
        List<Book> booksList = getRandomAnswers(author);
        for (Book book : booksList) {
            result.add(book.getName());
        }
        return result;
    }

    private List<Book> getRandomAnswers(Author author) {
        Random random = new Random();
        validBooks.clear();
        BookService bookService = BookService.getInstance();
        List<Book> listOfBooks = bookService.getAllByAuthor(author);
        if (listOfBooks.size() > 3) listOfBooks = bookService.getThreeRandomBooksByAuthor(author);
        validBooks.addAll(this.listOfBooks);
        validBooks.removeAll(listOfBooks);
        int index = random.nextInt(validBooks.size());
        Book result = validBooks.get(index);
        this.setResponseText(result.getName());
        listOfBooks.add(result);
        return listOfBooks;
    }
}

