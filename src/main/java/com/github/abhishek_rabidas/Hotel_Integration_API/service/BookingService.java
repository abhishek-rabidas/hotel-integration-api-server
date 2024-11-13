package com.github.abhishek_rabidas.Hotel_Integration_API.service;

import com.github.abhishek_rabidas.Hotel_Integration_API.dao.BookingRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dao.HotelRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dao.HotelRoomRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dao.UserRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.BookingResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.CreateBookingRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.NotFoundException;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.ValidationException;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.Booking;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.Hotel;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.HotelRoom;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.HrmsUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private HotelRoomRepository hotelRoomRepository;

    @Autowired
    private UserRepository userRepository;

    public void createBooking(CreateBookingRequest createBookingRequest) {
        createBookingRequest.validate();

        HrmsUser user = userRepository.findByUuid(createBookingRequest.getUserId());

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        Hotel hotel = hotelRepository.findByUuid(createBookingRequest.getHotelId());

        if (hotel == null) {
            throw new NotFoundException("Hotel not found");
        }

        if (!hotel.isActive()) {
            throw new ValidationException("Hotel is not active");
        }

        List<HotelRoom> rooms = hotelRoomRepository.findAllByUuidIn(createBookingRequest.getRoomIds());

        if (rooms == null || rooms.isEmpty()) {
            throw new ValidationException("Provided rooms not found");
        }

        rooms.forEach(hotelRoom -> {
            if (!hotelRoom.isCurrentlyBooked()) {
                throw new ValidationException(hotelRoom.getRoomName() + " is already booked");
            }

            if (!hotelRoom.getHotel().getUuid().equals(hotel.getUuid())) {
                throw new ValidationException(hotelRoom.getRoomName() + " is not a room of " + hotel.getLegalName());
            }
        });

        Date bookingStartDate;
        Date bookingEndDate;

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        try {
           bookingStartDate = simpleDateFormat.parse(createBookingRequest.getBookingFrom());
           bookingEndDate = simpleDateFormat.parse(createBookingRequest.getBookingTo());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ValidationException("Failed to parse details of booking start date and end date");
        }

        Booking booking = new Booking();
        booking.setBookingId(RandomStringUtils.randomAlphabetic(12).toUpperCase());
        booking.setHotel(hotel);
        booking.setRooms(rooms);
        booking.setUser(user);
        booking.setBookingFrom(bookingStartDate);
        booking.setBookingTo(bookingEndDate);
        booking.setPax(createBookingRequest.getPax());
        booking.setAmountPaid(createBookingRequest.getAmountPaid());
        bookingRepository.save(booking);

        rooms.forEach(hotelRoom -> {
            hotelRoom.setCurrentlyBooked(true);
            hotelRoom.setBookedTill(bookingEndDate);
        });
        hotelRoomRepository.saveAll(rooms);
    }

    public List<BookingResponse> getBookingsForUser(String userId) {
        HrmsUser user = userRepository.findByUuid(userId);

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        List<Booking> bookings = bookingRepository.findAllByUser(user);

        if (bookings.isEmpty()) {
            throw new NotFoundException("No bookings found");
        }

        return bookings.stream().map(BookingResponse::new).collect(Collectors.toList());
    }
}
