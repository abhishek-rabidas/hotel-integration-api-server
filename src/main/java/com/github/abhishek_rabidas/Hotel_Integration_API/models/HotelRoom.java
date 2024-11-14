package com.github.abhishek_rabidas.Hotel_Integration_API.models;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class HotelRoom extends BaseEntity {
    private String roomName;
    private String roomIdentifier;
    private String roomDescription;
    private Double roomPrice;
    private boolean isCurrentlyBooked;
    @ManyToOne
    private Hotel hotel;
    private Date bookedTill;
}
