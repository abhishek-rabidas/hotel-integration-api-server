package com.github.abhishek_rabidas.Hotel_Integration_API.models;

import com.github.abhishek_rabidas.Hotel_Integration_API.enums.BookingStatus;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class BookingStatusHistory extends BaseEntity {
    @ManyToOne
    private Booking booking;
    private BookingStatus status;
    private String remark;

    public BookingStatusHistory(Booking booking, BookingStatus status) {
        this.booking = booking;
        this.status = status;
    }
}
