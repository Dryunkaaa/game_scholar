package service.jpa;

import domain.Letter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import storage.AbstractDao;

import javax.persistence.NoResultException;

public class LetterService {

    private static final LetterService INSTANCE = new LetterService();

    private LetterService() {
    }

    public static LetterService getInstance() {
        return INSTANCE;
    }

    public void create(String value) {
        AbstractDao<Letter> abstractDao = new AbstractDao<>(Letter.class);
        if (getByValue(value) == null) {
            Letter letter = new Letter();
            letter.setValue(value);
            abstractDao.createDao(letter);
        }
    }

    public Letter getByValue(String value) {
        AbstractDao<Letter> abstractDao = new AbstractDao<>(Letter.class);
        Session session = abstractDao.openSession();
        Query query = session.createQuery("from Letter l where l.value=:value");
        query.setParameter("value", value);
        Letter result = null;
        try {
            result = (Letter) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        session.close();
        return result;
    }
}
