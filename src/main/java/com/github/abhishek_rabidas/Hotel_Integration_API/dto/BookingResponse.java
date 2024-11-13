package com.github.abhishek_rabidas.Hotel_Integration_API.dto;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.Booking;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class BookingResponse extends Response {
    private String bookingId;
    private Double amountPaid;
    private Date bookingFrom;
    private Date bookingTo;
    private int pax;
    private HotelResponse hotelResponse;
    private List<HotelRoomResponse> hotelRoomResponses;

    public BookingResponse(Booking booking) {
        this.setId(booking.getUuid());
        this.bookingId = booking.getBookingId();
        this.amountPaid = booking.getAmountPaid();
        this.bookingFrom = booking.getBookingFrom();
        this.bookingTo = booking.getBookingTo();
        this.pax = booking.getPax();
        this.hotelResponse = new HotelResponse(booking.getHotel());
        this.hotelRoomResponses =  booking.getRooms().stream().map(HotelRoomResponse::new).collect(Collectors.toList());
    }
}
