package com.github.abhishek_rabidas.Hotel_Integration_API.dto;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.Hotel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelResponse extends Response {
    private String legalName;
    private String hotelCode;
    private String address;
    private Double rating;
    private int taxRate;
    private List<HotelRoomResponse> rooms;

    public HotelResponse(Hotel hotel) {
        this.setId(hotel.getUuid());
        this.legalName = hotel.getLegalName();
        this.hotelCode = hotel.getHotelCode();
        this.address = hotel.getAddress();
        this.rating = hotel.getRating();
        this.taxRate = hotel.getTaxRate();
    }
}
