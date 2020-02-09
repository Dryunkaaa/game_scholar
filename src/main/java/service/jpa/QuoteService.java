package service.jpa;

import domain.Author;
import domain.Quote;
import org.hibernate.Session;
import org.hibernate.query.Query;
import storage.AbstractDao;

import javax.persistence.NoResultException;
import java.util.List;

public class QuoteService {

    private static final QuoteService INSTANCE = new QuoteService();

    private QuoteService() {
    }

    public static QuoteService getInstance() {
        return INSTANCE;
    }

    public void create(String quoteValue, Author author) {
        if (getQuoteByValue(quoteValue) == null) {
            AbstractDao<Quote> abstractDao = new AbstractDao<>(Quote.class);
            Quote quote = new Quote();
            quote.setValue(quoteValue);
            quote.setAuthor(author);
            abstractDao.createDao(quote);
        }
    }

    public List<Quote> getAll() {
        AbstractDao<Quote> abstractDao = new AbstractDao<>(Quote.class);
        return abstractDao.getAllDao();
    }

    public Quote getQuoteByValue(String value) {
        AbstractDao<Quote> abstractDao = new AbstractDao<>(Quote.class);
        Session session = abstractDao.openSession();

        Query query = session.createQuery("from Quote q where q.value = :value");
        query.setParameter("value", value);

        try {
            return  (Quote) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }finally {
            session.close();
        }

    }
}
