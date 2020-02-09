package service.jpa;

import domain.Author;
import domain.Book;
import org.hibernate.Session;
import org.hibernate.query.Query;
import storage.AbstractDao;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookService {

    private static final BookService INSTANCE = new BookService();

    private BookService(){}

    public void create(String name, Author author){
        if (getBookByName(name) == null){
            AbstractDao<Book> abstractDao = new AbstractDao<>(Book.class);
            Book book = new Book();
            book.setName(name);
            book.setAuthor(author);
            abstractDao.createDao(book);
        }
    }

    public List<Book> getAll(){
        AbstractDao<Book> abstractDao = new AbstractDao<>(Book.class);
        return abstractDao.getAllDao();
    }

    public Book getBookByName(String name){
        AbstractDao<Book> abstractDao = new AbstractDao<>(Book.class);
        Session session = abstractDao.openSession();

        Query query = session.createQuery("from Book b where b.name = :name");
        query.setParameter("name", name);

        try {
            return  (Book) query.getSingleResult();
        }catch (NoResultException e){
            return null;
        }finally {
            session.close();
        }

    }

    public List<Book> getAllByAuthor(Author author){
        AbstractDao<Book> abstractDao = new AbstractDao<>(Book.class);
        Session session = abstractDao.openSession();

        Query query = session.createQuery("from Book b where b.author.id = :authorId");
        query.setParameter("authorId", author.getId());

        List<Book> result = query.list();
        session.close();
        return result;
    }

    public List<Book> getThreeRandomBooksByAuthor(Author author){
        Random random = new Random();
        List<Book> booksByAuthor = getAllByAuthor(author);
        List<Book> result = new ArrayList<>();

        for (int i = 0; i<3;i++){
            int index = random.nextInt(booksByAuthor.size());
            result.add(booksByAuthor.get(index));
            booksByAuthor.remove(index);
        }

        return result;
    }

    public static BookService getInstance(){
        return INSTANCE;
    }
}
