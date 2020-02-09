package service.jpa;

import domain.CorrectlySpelledWord;
import domain.MisspelledWord;
import org.hibernate.Session;
import storage.AbstractDao;

import java.util.List;

public class MisspelledWordService {

    private final static MisspelledWordService INSTANCE = new MisspelledWordService();

    private MisspelledWordService() {}

    public void create(String value, CorrectlySpelledWord correctlySpelledWord) {
        AbstractDao<MisspelledWord> abstractDao = new AbstractDao<>(MisspelledWord.class);
        MisspelledWord dependentWord = new MisspelledWord();
        dependentWord.setValue(value);
        dependentWord.setCorrectlySpelledWord(correctlySpelledWord);
        abstractDao.createDao(dependentWord);
    }

    public List<MisspelledWord> getAllWordsByOwner(CorrectlySpelledWord correctlySpelledWord) {
        AbstractDao<MisspelledWord> abstractDao = new AbstractDao<>(MisspelledWord.class);
        Session session = abstractDao.openSession();

        String query = "from MisspelledWord w where w.correctlySpelledWord.id = " + correctlySpelledWord.getId();
        List<MisspelledWord> result = session.createQuery(query, MisspelledWord.class).list();
        session.close();
        return result;
    }

    public static MisspelledWordService getInstance(){
        return INSTANCE;
    }
}
