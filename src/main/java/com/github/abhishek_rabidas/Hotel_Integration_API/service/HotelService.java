package com.github.abhishek_rabidas.Hotel_Integration_API.service;

import com.github.abhishek_rabidas.Hotel_Integration_API.dao.HotelRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.CreateHotelRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.HotelResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.PageResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.NotFoundException;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.ValidationException;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.Hotel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public PageResponse<HotelResponse> listHotels(Pageable pageable) {
        Page<Hotel> hotels = hotelRepository.findAll(pageable);
        PageResponse<HotelResponse> response = new PageResponse<>();
        response.setTotalCount(hotels.getTotalElements());
        response.setList(hotels.stream().map(HotelResponse::new).collect(Collectors.toList()));
        response.setPageNumber(hotels.getNumber());
        response.setPageSize(hotels.getSize());
        return response;
    }

    public HotelResponse fetchHotelDetails(String id) {
        Hotel hotel = hotelRepository.findByUuid(id);
        if (hotel == null) {
            throw new NotFoundException("Hotel not found");
        }
        return new HotelResponse(hotel);
    }

    @Transactional
    public void archiveHotel(String id) {
        Hotel hotel = hotelRepository.findByUuid(id);
        if (hotel == null) {
            throw new NotFoundException("Hotel not found");
        }

        if (!hotel.isActive()) {
            throw new ValidationException("Hotel is already de-activated");
        }

        hotel.setActive(false);
        hotelRepository.save(hotel);
    }
}
