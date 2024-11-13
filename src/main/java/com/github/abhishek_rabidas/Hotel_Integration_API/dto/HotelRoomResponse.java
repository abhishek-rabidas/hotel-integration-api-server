package com.github.abhishek_rabidas.Hotel_Integration_API.dto;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.HotelRoom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HotelRoomResponse extends Response {
    private String roomName;
    private String roomIdentifier;
    private String roomDescription;
    private Double roomPrice;
    private boolean isCurrentlyBooked;

    public HotelRoomResponse(HotelRoom hotelRoom) {
        this.setId(hotelRoom.getUuid());
        this.isCurrentlyBooked = hotelRoom.isCurrentlyBooked();
        this.roomIdentifier = hotelRoom.getRoomIdentifier();
        this.roomName = hotelRoom.getRoomName();
        this.roomDescription = hotelRoom.getRoomDescription();
        this.roomPrice = hotelRoom.getRoomPrice();
    }
}
