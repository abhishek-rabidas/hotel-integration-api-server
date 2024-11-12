package com.github.abhishek_rabidas.Hotel_Integration_API.models;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Hotel extends BaseEntity {
    private String legalName;
    private String hotelCode;
    private String address;
    private Double rating;
    private int taxRate;
    private boolean active = true;
}
