package service.jpa;

import domain.Word;
import org.hibernate.Session;
import org.hibernate.query.Query;
import storage.AbstractDao;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

public class WordService {

    private static final WordService INSTANCE = new WordService();

    private WordService() {
    }

    public static WordService getInstance() {
        return INSTANCE;
    }

    public void create(String value) {
        if (getByValue(value) == null) {
            AbstractDao<Word> abstractDao = new AbstractDao<>(Word.class);
            Word word = new Word();
            word.setValue(value);
            abstractDao.createDao(word);
        }
    }

    public List<Word> getAll() {
        AbstractDao<Word> abstractDao = new AbstractDao<>(Word.class);
        return abstractDao.getAllDao();
    }

    public Word getByValue(String value) {
        AbstractDao<Word> abstractDao = new AbstractDao<>(Word.class);
        Session session = abstractDao.openSession();

        Query query = session.createQuery("from Word w where w.value = :value");
        query.setParameter("value", value);

        try {
            return (Word) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            session.close();
        }

    }

    public List<Word> getAllWithOneMissedChar() {
        List<Word> result = new ArrayList<>();
        for (Word word : getAll()) {
            if (word.getReplaceableCharacters().size() == 1) result.add(word);
        }
        return result;
    }

    public List<Word> getAllWithTwoOrMoreMissedChars() {
        List<Word> result = new ArrayList<>();
        for (Word word : getAll()) {
            if (word.getReplaceableCharacters().size() > 1) result.add(word);
        }
        return result;
    }
}
