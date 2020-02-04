package domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "letter")
@Getter
@Setter
@EqualsAndHashCode(exclude = "replaceableCharacters")
public class Letter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "value")
    private String value;

    @OneToMany(mappedBy = "letter", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<ReplaceableCharacter> replaceableCharacters = new HashSet<>();
}
