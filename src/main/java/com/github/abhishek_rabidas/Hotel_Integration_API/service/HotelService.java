package com.github.abhishek_rabidas.Hotel_Integration_API.service;

import com.github.abhishek_rabidas.Hotel_Integration_API.dao.HotelRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dao.HotelRoomRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.*;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.NotFoundException;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.ValidationException;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.Hotel;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.HotelRoom;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {
    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);

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

    public HotelResponse addHotelRooms(HotelRoomCreateRequest request) {
        Hotel hotel = hotelRepository.findByUuid(request.getHotelId());

        if (hotel == null) {
            throw new NotFoundException("Hotel not found");
        }

        if (!hotel.isActive()) {
            throw new ValidationException("Hotel is not active");
        }

        List<HotelRoom> rooms = new ArrayList<>();

        request.getRoomDetails().forEach(details -> {
            HotelRoom hotelRoom = new HotelRoom();
            hotelRoom.setHotel(hotel);
            hotelRoom.setRoomName(details.getRoomName());
            hotelRoom.setRoomIdentifier(details.getRoomIdentifier());
            hotelRoom.setRoomDescription(details.getRoomDescription());
            hotelRoom.setRoomPrice(details.getRoomPrice());
            rooms.add(hotelRoom);
        });


        hotelRoomRepository.saveAll(rooms);
        HotelResponse response = new HotelResponse(hotel);
        response.setRooms(rooms.stream().map(HotelRoomResponse::new).collect(Collectors.toList()));
        return response;
    }
}
