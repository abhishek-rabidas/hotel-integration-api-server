package com.github.abhishek_rabidas.Hotel_Integration_API.service;

import com.github.abhishek_rabidas.Hotel_Integration_API.dao.HotelRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.CreateHotelRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.HotelResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelService {

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public HotelResponse createHotel(CreateHotelRequest request) {
        Hotel hotel = request.toEntity();
        return new HotelResponse(hotelRepository.save(hotel));
    }
}
