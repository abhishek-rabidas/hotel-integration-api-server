package com.github.abhishek_rabidas.Hotel_Integration_API.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HotelRoomCreateRequest {
    private String hotelId;
    private List<RoomDetails> roomDetails;
}

