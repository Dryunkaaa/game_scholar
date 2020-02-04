package service.jpa;

import domain.Letter;
import domain.ReplaceableCharacter;
import domain.Word;
import storage.AbstractDao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReplaceableCharacterService {

    private static final ReplaceableCharacterService INSTANCE = new ReplaceableCharacterService();

    private ReplaceableCharacterService() {
    }

    public static ReplaceableCharacterService getInstance() {
        return INSTANCE;
    }

    public void create(Word word, Letter letter, int charIndex) {
        AbstractDao<ReplaceableCharacter> abstractDao = new AbstractDao<>(ReplaceableCharacter.class);
        ReplaceableCharacter character = new ReplaceableCharacter();
        character.setWord(word);
        character.setLetter(letter);
        character.setCharacterIndex(charIndex);
        abstractDao.createDao(character);
    }

    public List<ReplaceableCharacter> sortByIndexNumber(List<ReplaceableCharacter> characters) {
        Collections.sort(characters, characterIndexesComparator);
        return characters;
    }

    private Comparator<ReplaceableCharacter> characterIndexesComparator = (rc1, rc2) -> {
        long firstIndex = rc1.getCharacterIndex();
        long secondIndex = rc2.getCharacterIndex();
        return Long.compare(firstIndex, secondIndex);
    };
}
