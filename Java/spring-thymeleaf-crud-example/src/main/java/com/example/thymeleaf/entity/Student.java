package com.example.thymeleaf.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.owasp.encoder.Encode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.SPACE;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Table(name = "student")
public class Student {

    @Id
    private String id;

    private String name;
    private String email;
    private LocalDate birthday;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Address address;

    private static final List<String> ALLOWED_NAME_DELIMITERS = List.of(SPACE, "-");
    private static final int MAX_NAME_LENGTH = 250;
    private static final String NAME_FORMAT = "(\\p{Alpha}|-|\\s)+";
    private static final int MAX_EMAIL_LENGTH = 250;
    private static final String OWASP_EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

    public Student(String name, String email, LocalDate birthday, Address address) {
        validateStudentData(name, email, birthday, address);
        this.name = transformName(Encode.forHtml(name));
        this.email = Encode.forHtml(email);
        this.birthday = birthday;
        this.address = address;
    }

    private void validateStudentData(String name, String email, LocalDate birthday, Address address) {
        validateName(name);
        validateEmail(email);
        validateBirthday(birthday);
        validateAddress(address);
    }

    private void validateName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new InvalidStudentDataException("Name cannot be blank.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidStudentDataException("Name is too long.");
        }
        if (!name.matches(NAME_FORMAT)) {
            throw new InvalidStudentDataException("Name has incorrect format.");
        }
    }

    private void validateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            throw new InvalidStudentDataException("Email cannot be blank.");
        }
        if (email.length() > MAX_EMAIL_LENGTH) {
            throw new InvalidStudentDataException("Email is too long.");
        }
        if (!email.matches(OWASP_EMAIL_REGEX)) {
            throw new InvalidStudentDataException("Email is invalid.");
        }
    }

    private void validateBirthday(LocalDate birthday) {
        if (Objects.isNull(birthday)) {
            throw new InvalidStudentDataException("Birthday cannot be null.");
        }
        if (birthday.isAfter(LocalDate.now())) {
            throw new InvalidStudentDataException("Birthday cannot be a future date.");
        }
    }

    private void validateAddress(Address address) {
        if (Objects.isNull(address)) {
            throw new InvalidStudentDataException("Address cannot be null");
        }
    }

    private String transformName(final String name) {
        String result = name;
        result = result.toLowerCase();
        for (var delimiter : ALLOWED_NAME_DELIMITERS) {
            if (result.contains(delimiter)) {
                result = Arrays.stream(result.split(delimiter)).map(StringUtils::capitalize).collect(Collectors.joining(delimiter));
            }
        }
        return result;
    }

    @PrePersist
    private void prePersist() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public static final class InvalidStudentDataException extends RuntimeException {
        public InvalidStudentDataException(String message) {
            super(message);
        }
    }
}
