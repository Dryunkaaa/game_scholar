package service.jpa;

import domain.Author;
import org.hibernate.Session;
import org.hibernate.query.Query;
import storage.AbstractDao;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class AuthorService {

    private static final AuthorService INSTANCE = new AuthorService();

    private AuthorService(){}

    public void create(String name, String last_name) {
        if (getByLastName(last_name) == null) {
            AbstractDao<Author> authorAbstractDao = new AbstractDao<>(Author.class);
            Author author = new Author();
            author.setFirstName(name);
            author.setLastName(last_name);
            authorAbstractDao.createDao(author);
        }
    }

    public Author getByLastName(String name) {
        AbstractDao<Author> authorAbstractDao = new AbstractDao<>(Author.class);
        Session session = authorAbstractDao.openSession();

        Query query = session.createQuery("from Author a where a.lastName = :name");
        query.setParameter("name", name);

        try {
            return (Author) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }finally {
            session.close();
        }

    }

    public List<Author> getAll() {
        AbstractDao<Author> authorAbstractDao = new AbstractDao<>(Author.class);
        return authorAbstractDao.getAllDao();
    }

    public List<Author> getAllWithThreeOrMoreBooks() {
        List<Author> authors = getAll();
        List<Author> result = new ArrayList<>();

        for (Author author : authors) {
            if (author.getBooks().size() > 2) result.add(author);
        }

        return result;
    }

    public static AuthorService getInstance(){
        return INSTANCE;
    }
}
