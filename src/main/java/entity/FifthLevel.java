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

public class FifthLevel extends Level {

    private List<Book> bookList;
    private List<Author> authorList;
    private List<Book> validBookList = new ArrayList<>();
    private Label titleLabel;

    public FifthLevel(int countOfQuestions, Label currentQuestionNumber,
                      Label[] answers, Label titleLabel, ImageView correctAnswerImage, ImageView wrongAnswerImage) {
        super(countOfQuestions, currentQuestionNumber, answers,correctAnswerImage, wrongAnswerImage);
        this.titleLabel = titleLabel;

        // список авторов у которых 3 и более книг так как полей для ответа 4 и 1 из них - не книга автора
        authorList = AuthorService.getInstance().getAllWithThreeOrMoreBooks();
    }

    @Override
    public void restart() {
        new FifthLevelController().show();
    }

    @Override
    protected void prepareData() {
        bookList = BookService.getInstance().getAll();
        Author author = getRandomAuthor();

        titleLabel.setText("Автором какой книги не является " + author.getFirstName() + " " + author.getLastName());
        distributeLabelsData(getBookNameList(author));
    }

    private Author getRandomAuthor() {
        int index = random.nextInt(authorList.size());

        Author result = authorList.get(index);
        authorList.remove(index);
        return result;
    }

    private List<String> getBookNameList(Author author) {
        List<String> bookNameList = new ArrayList<>();
        List<Book> booksList = getRandomBooks(author);

        for (Book book : booksList) {
            bookNameList.add(book.getName());
        }
        return bookNameList;
    }

    private List<Book> getRandomBooks(Author author) {
        validBookList.clear();

        BookService bookService = BookService.getInstance();
        List<Book> books = bookService.getAllByAuthor(author);

        if (books.size() > 3){
            books = bookService.getThreeRandomBooksByAuthor(author);
        }

        validBookList.addAll(this.bookList);
        validBookList.removeAll(books);

        int index = random.nextInt(validBookList.size());
        Book result = validBookList.get(index);
        this.setResponseText(result.getName());
        books.add(result);

        return books;
    }
}

