package com.ecommerce.project.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min = 5, message = "Street name must be AtLeast 5 Characters")
    private String street;

    @NotBlank
    @Size(min = 5, message = "Building Name must be AtLeast 5 Characters")
    private String buildingName;

    @NotBlank
    @Size(min = 3, message = "City name must be AtLeast 3 Characters")
    private String city;

    @NotBlank
    @Size(min = 4, message = "State name must be AtLeast 5 Characters")
    private String state;

    @NotBlank
    @Size(min = 2, message = "Country name must be AtLeast 5 Characters")
    private String country;

    @NotBlank
    @Size(min = 6, message = "PinCode must be AtLeast 6 Characters")
    private String pinCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String buildingName, String city, String country, String pinCode, String state, String street) {
        this.buildingName = buildingName;
        this.city = city;
        this.country = country;
        this.pinCode = pinCode;
        this.state = state;
        this.street = street;
    }
}
