package com.example.weatherapi.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Pincode {
    @Id
    private String code;
    private double latitude;
    private double longitude;
}
