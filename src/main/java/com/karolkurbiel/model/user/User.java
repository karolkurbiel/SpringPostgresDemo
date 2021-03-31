package com.karolkurbiel.model.user;

import com.karolkurbiel.exceptions.FieldIsBlankException;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "UsersDB")
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private final UUID id;
    @Column(nullable = false, length = 64)
    private final String password;
    @Column(nullable = false, length = 50)
    private final String name;
    private final LocalDate dateOfBirth;
    @Column(nullable = false, unique = true, length = 45)
    private final String email;

    public User(String password, String name, String email, LocalDate dateOfBirth) {
        this(UUID.randomUUID(), password, name, email, dateOfBirth);
    }

    public User(UUID id, String password, String name, String email, LocalDate dateOfBirth) {
        if(name.isBlank() || email.isBlank() || password.isEmpty()) {
            throw new FieldIsBlankException();
        }
        this.id = id;
        this.password = password;
        this.name = name;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
    }

    public UUID getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }


}
