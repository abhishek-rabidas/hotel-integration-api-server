package com.github.abhishek_rabidas.Hotel_Integration_API.models;

import com.github.abhishek_rabidas.Hotel_Integration_API.enums.BookingStatus;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.BaseEntity;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Booking extends BaseEntity {

    @Column(unique = true, nullable = false, length = 24)
    private String bookingId;

    @ManyToOne
    private Hotel hotel;

    @ManyToMany
    private List<HotelRoom> rooms;

    @ManyToOne
    private HrmsUser user;

    private Double amountPaid;

    private Date bookingFrom;

    private Date bookingTo;

    private int pax;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
