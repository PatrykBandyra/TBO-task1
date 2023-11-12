package com.example.thymeleaf.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.owasp.encoder.Encode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
@Table(name = "address")
public class Address {

    @Id
    private String id;

    @Column(name = "zip_code")
    private String zipCode;

    private String street;
    private String number;
    private String complement;
    private String district;
    private String city;
    private String state;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Setter
    @OneToOne
    @JoinColumn(name = "fk_student")
    private Student student;

    private static final String ZIP_CODE_FORMAT = "^\\d{5}-\\d{3}$";
    private static final int MAX_STREET_NAME_LENGTH = 250;
    private static final int STREET_NUMBER_MAX_LENGTH = 10;
    private static final int COMPLEMENT_MAX_LENGTH = 2000;
    private static final int MAX_DISTRICT_NAME_LENGTH = 250;
    private static final int MAX_CITY_NAME_LENGTH = 250;
    private static final int MAX_STATE_NAME_LENGTH = 250;

    public Address(String zipCode, String street, String number, String complement, String district, String city, String state) {
        validateAddressData(zipCode, street, number, complement, district, city, state);
        this.zipCode = Encode.forHtml(zipCode);
        this.street = Encode.forHtml(street);
        this.number = Encode.forHtml(number);
        this.complement = Encode.forHtml(complement);
        this.district = Encode.forHtml(district);
        this.city = Encode.forHtml(city);
        this.state = Encode.forHtml(state);
    }

    public void validateAddressData(String zipCode, String street, String number, String complement, String district, String city, String state) {
        validateZipCode(zipCode);
        validateStreet(street);
        validateStreetNumber(number);
        validateComplement(complement);
        validateDistrict(district);
        validateCity(city);
        validateState(state);
    }

    private void validateZipCode(String zipCode) {
        if (StringUtils.isBlank(zipCode)) {
            throw new InvalidAddressDataException("Zip code cannot be blank.");
        }
        if (!zipCode.matches(ZIP_CODE_FORMAT)) {
            throw new InvalidAddressDataException("Zip code has incorrect format.");
        }
    }

    private void validateStreet(String street) {
        if (StringUtils.isBlank(street)) {
            throw new InvalidAddressDataException("Street cannot be blank.");
        }
        if (street.length() > MAX_STREET_NAME_LENGTH) {
            throw new InvalidAddressDataException("Street is too long.");
        }
    }

    private void validateStreetNumber(String number) {
        if (StringUtils.isBlank(number)) {
            throw new InvalidAddressDataException("Number cannot be blank.");
        }
        if (number.length() > STREET_NUMBER_MAX_LENGTH) {
            throw new InvalidAddressDataException("Number is too long.");
        }
        if (!StringUtils.isAlphanumeric(number)) {
            throw new InvalidAddressDataException("Number has invalid format.");
        }
    }

    private void validateComplement(String complement) {
        if (complement.length() > COMPLEMENT_MAX_LENGTH) {
            throw new InvalidAddressDataException("Complement is too long.");
        }
    }

    private void validateDistrict(String district) {
        if (StringUtils.isBlank(district)) {
            throw new InvalidAddressDataException("District cannot be blank.");
        }
        if (district.length() > MAX_DISTRICT_NAME_LENGTH) {
            throw new InvalidAddressDataException("District is too long.");
        }
    }

    private void validateCity(String city) {
        if (StringUtils.isBlank(city)) {
            throw new InvalidAddressDataException("City cannot be blank.");
        }
        if (city.length() > MAX_CITY_NAME_LENGTH) {
            throw new InvalidAddressDataException("City is too long.");
        }
    }

    private void validateState(String state) {
        if (StringUtils.isBlank(state)) {
            throw new InvalidAddressDataException("State cannot be blank.");
        }
        if (state.length() > MAX_STATE_NAME_LENGTH) {
            throw new InvalidAddressDataException("State is too long.");
        }
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

    public static final class InvalidAddressDataException extends RuntimeException {
        public InvalidAddressDataException(String message) {
            super(message);
        }
    }
}
