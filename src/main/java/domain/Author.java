package domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "author")
@Getter
@Setter
@EqualsAndHashCode(of = {"id","firstName", "lastName"})
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Quote> quotes = new HashSet<>();

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    private Set<Book> books = new HashSet<>();
}
