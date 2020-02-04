package domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "correctly_spelled_word")
@Getter
@Setter
@EqualsAndHashCode
public class CorrectlySpelledWord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "value")
    private String value;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "correctlySpelledWord")
    private Set<MisspelledWord> misspelledWords = new HashSet<>();
}
