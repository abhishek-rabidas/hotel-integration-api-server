package com.github.abhishek_rabidas.Hotel_Integration_API.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDetails {
    private String roomName;
    private String roomIdentifier;
    private String roomDescription;
    private Double roomPrice;
}
