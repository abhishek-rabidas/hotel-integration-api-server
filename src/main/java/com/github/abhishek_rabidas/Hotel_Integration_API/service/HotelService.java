package com.github.abhishek_rabidas.Hotel_Integration_API.service;

import com.github.abhishek_rabidas.Hotel_Integration_API.dao.HotelRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dao.HotelRoomRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.CreateHotelRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.HotelResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.HotelRoomResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.PageResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.NotFoundException;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.ValidationException;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.Hotel;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.HotelRoom;
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
    private final HotelRoomRepository hotelRoomRepository;

    public HotelService(HotelRepository hotelRepository, HotelRoomRepository hotelRoomRepository) {
        this.hotelRepository = hotelRepository;
        this.hotelRoomRepository = hotelRoomRepository;
    }

    public HotelResponse createHotel(CreateHotelRequest request) {
        Hotel hotel = request.toEntity();
        return new HotelResponse(hotelRepository.save(hotel));
    }

    public PageResponse<HotelResponse> listHotels(Pageable pageable) {
        Page<Hotel> hotels = hotelRepository.findAllByActive(true, pageable);
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

        if (!hotel.isActive()) {
            throw new ValidationException("Hotel is not active");
        }

        HotelResponse response = new HotelResponse(hotel);

        List<HotelRoom> rooms = hotelRoomRepository.findAllByHotel(hotel);
        response.setRooms(rooms.stream().map(HotelRoomResponse::new).collect(Collectors.toList()));
        return response;
    }

    @Transactional
    public void changeHotelStatus(String id, boolean status) {
        Hotel hotel = hotelRepository.findByUuid(id);
        if (hotel == null) {
            throw new NotFoundException("Hotel not found");
        }

        if (hotel.isActive() == status) {
            throw new ValidationException("Hotel is already " + (status ? "activated" : "de-activated"));
        }

        hotel.setActive(status);
        hotelRepository.save(hotel);
    }
}