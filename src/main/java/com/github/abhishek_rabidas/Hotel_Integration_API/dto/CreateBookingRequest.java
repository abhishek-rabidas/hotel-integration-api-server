package com.github.abhishek_rabidas.Hotel_Integration_API.dto;

import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.NotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
@Setter
public class CreateBookingRequest {
    private String hotelId;
    private List<String> roomIds;
    private String bookingFrom; //yyyy-MM-dd hh:mm:ss
    private String bookingTo; //yyyy-MM-dd hh:mm:ss
    private int pax;
    private Double amountPaid;
    private String userId;

    public void validate() {
        if (!StringUtils.hasLength(this.hotelId)) {
            throw new NotFoundException("Hotel details not provided");
        }

        if (!StringUtils.hasLength(this.bookingFrom)) {
            throw new NotFoundException("Booking start date and time not provided");
        }

        if (!StringUtils.hasLength(this.bookingTo)) {
            throw new NotFoundException("Booking end date and time not provided");
        }

        if (this.roomIds == null || this.roomIds.isEmpty()) {
            throw new NotFoundException("Room details not provided");
        }

        if (this.pax <= 0) {
            throw new NotFoundException("Number of occupants not provided");
        }

        if (this.amountPaid <= 0) {
            throw new NotFoundException("Total amount paid not provided");
        }
    }
}
