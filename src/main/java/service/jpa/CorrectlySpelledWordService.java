package service.jpa;

import domain.CorrectlySpelledWord;
import org.hibernate.Session;
import org.hibernate.query.Query;
import storage.AbstractDao;

import javax.persistence.NoResultException;
import java.util.List;

public class CorrectlySpelledWordService {

    private final static CorrectlySpelledWordService INSTANCE = new CorrectlySpelledWordService();

    private CorrectlySpelledWordService(){}

    public void create(String value) {
        if (getByValue(value) == null) {
            AbstractDao<CorrectlySpelledWord> abstractDao = new AbstractDao<>(CorrectlySpelledWord.class);
            CorrectlySpelledWord responseWord = new CorrectlySpelledWord();
            responseWord.setValue(value);
            abstractDao.createDao(responseWord);
        }
    }

    public List<CorrectlySpelledWord> getAllWords() {
        AbstractDao<CorrectlySpelledWord> abstractDao = new AbstractDao<>(CorrectlySpelledWord.class);
        return abstractDao.getAllDao();
    }

    public CorrectlySpelledWord getByValue(String value) {
        AbstractDao<CorrectlySpelledWord> abstractDao = new AbstractDao<>(CorrectlySpelledWord.class);
        Session session = abstractDao.openSession();
        Query query = session.createQuery("from CorrectlySpelledWord w where w.value = :value");
        query.setParameter("value", value);
        CorrectlySpelledWord result = null;
        try {
            result = (CorrectlySpelledWord) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        session.close();
        return result;
    }

    public static CorrectlySpelledWordService getInstance(){
        return INSTANCE;
    }
}
