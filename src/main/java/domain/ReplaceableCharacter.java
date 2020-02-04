package domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "replaceable_character")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"letter", "word"})
public class ReplaceableCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "word_id")
    private Word word;

    @ManyToOne
    @JoinColumn(name = "letter_id")
    private Letter letter;

    @Column(name = "characterIndex")
    private int characterIndex;
}
