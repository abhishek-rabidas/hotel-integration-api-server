package com.github.abhishek_rabidas.Hotel_Integration_API.service;

import com.github.abhishek_rabidas.Hotel_Integration_API.dao.*;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.BookingHistory;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.BookingResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.CreateBookingRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.enums.BookingStatus;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.AccessDeniedException;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.NotFoundException;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.ValidationException;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.*;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.abhishek_rabidas.Hotel_Integration_API.service.UserService.getCurrentUser;

@Service
public class BookingService {
    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);


    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final HotelRoomRepository hotelRoomRepository;
    private final UserRepository userRepository;
    private final BookingStatusHistoryRepository statusHistoryRepository;

    @Autowired
    public BookingService(
            BookingRepository bookingRepository,
            HotelRepository hotelRepository,
            HotelRoomRepository hotelRoomRepository,
            UserRepository userRepository,
            BookingStatusHistoryRepository statusHistoryRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.hotelRepository = hotelRepository;
        this.hotelRoomRepository = hotelRoomRepository;
        this.userRepository = userRepository;
        this.statusHistoryRepository = statusHistoryRepository;
    }

    /*    Schedule to run every day at midnight (12:00 AM) to check for bookings
        which have not been checked out successfully to release the rooms occupied*/
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkBookings() {
        Date currentDate = new Date();
        List<Booking> bookings = bookingRepository.findAllByBookingToLessThan(currentDate);
        List<HotelRoom> rooms = new ArrayList<>();

        bookings.forEach(booking -> {
            booking.getRooms().forEach(hotelRoom -> {
                if (hotelRoom.isCurrentlyBooked()) {
                    hotelRoom.setCurrentlyBooked(false);
                    hotelRoom.setBookedTill(null);
                    rooms.add(hotelRoom);
                }
            });
        });

        hotelRoomRepository.saveAll(rooms);
    }

    public BookingResponse createBooking(CreateBookingRequest createBookingRequest) {
        createBookingRequest.validate();

        HrmsUser user = getCurrentUser();

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
            if (hotelRoom.isCurrentlyBooked()) {
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
        booking.setStatus(BookingStatus.CREATED);
        booking = bookingRepository.save(booking);
        saveBookingStatus(booking, BookingStatus.CREATED);
        return new BookingResponse(booking);
    }

    public List<BookingResponse> getBookingsForUser(String userId) {
        HrmsUser user;
        if (StringUtils.hasLength(userId)) {
            user = userRepository.findByUuid(userId);
        } else {
            user = getCurrentUser();
        }

        if (user == null) {
            throw new NotFoundException("User not found");
        }

        List<Booking> bookings = bookingRepository.findAllByUser(user);

        if (bookings.isEmpty()) {
            throw new NotFoundException("No bookings found");
        }

        return bookings.stream().map(BookingResponse::new).collect(Collectors.toList());
    }

    @Transactional
    public void cancelBooking(String id) {
        Booking booking = bookingRepository.findByUuid(id);

        if (booking == null) {
            throw new NotFoundException("Booking details not found");
        }

        if (!validateBookingView(booking)) {
            throw new AccessDeniedException("You are not allowed to view this booking");
        }

        Date currentDate = new Date();

        if (!booking.getStatus().equals(BookingStatus.CREATED)) {
            throw new ValidationException("You can't cancel the booking");
        }

        if (currentDate.getTime() > booking.getBookingFrom().getTime()) {
            throw new ValidationException("You can't cancel the booking in between the stay tenure");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        saveBookingStatus(booking, BookingStatus.CANCELLED);

        List<HotelRoom> rooms = new ArrayList<>();
        booking.getRooms().forEach(hotelRoom -> {
            hotelRoom.setCurrentlyBooked(false);
            hotelRoom.setBookedTill(null);
            rooms.add(hotelRoom);
        });
        hotelRoomRepository.saveAll(rooms);
    }

    @Transactional
    public void checkInBooking(String id) {
        Booking booking = bookingRepository.findByUuid(id);

        if (booking == null) {
            throw new NotFoundException("Booking details not found");
        }

        if (!validateBookingView(booking)) {
            throw new AccessDeniedException("You are not allowed to view this booking");
        }

        if (!booking.getStatus().equals(BookingStatus.CREATED)) {
            throw new ValidationException("You can't check-in at this moment");
        }

        booking.setStatus(BookingStatus.CHECKED_IN);
        bookingRepository.save(booking);
        saveBookingStatus(booking, BookingStatus.CHECKED_IN);

        List<HotelRoom> rooms = new ArrayList<>();
        booking.getRooms().forEach(hotelRoom -> {
            hotelRoom.setCurrentlyBooked(true);
            hotelRoom.setBookedTill(booking.getBookingTo());
            rooms.add(hotelRoom);
        });
        hotelRoomRepository.saveAll(rooms);
    }

    @Transactional
    public void checkoutBooking(String id) {
        Booking booking = bookingRepository.findByUuid(id);

        if (booking == null) {
            throw new NotFoundException("Booking details not found");
        }

        if (!validateBookingView(booking)) {
            throw new AccessDeniedException("You are not allowed to view this booking");
        }

        if (!booking.getStatus().equals(BookingStatus.CHECKED_IN)) {
            throw new ValidationException("You can't check-out at this moment");
        }

        booking.setStatus(BookingStatus.CHECKED_OUT);
        bookingRepository.save(booking);
        saveBookingStatus(booking, BookingStatus.CHECKED_OUT);

        List<HotelRoom> rooms = new ArrayList<>();
        booking.getRooms().forEach(hotelRoom -> {
            hotelRoom.setCurrentlyBooked(false);
            hotelRoom.setBookedTill(null);
            rooms.add(hotelRoom);
        });
        hotelRoomRepository.saveAll(rooms);
    }

    private void saveBookingStatus(Booking booking, BookingStatus status) {
        BookingStatusHistory statusHistory = new BookingStatusHistory(booking, status);
        statusHistoryRepository.save(statusHistory);
    }

    public BookingResponse viewBookingDetails(String id) {
        Booking booking = bookingRepository.findByUuid(id);

        if (booking == null) {
            throw new NotFoundException("Booking details not found");
        }

        if (!validateBookingView(booking)) {
            throw new AccessDeniedException("You are not allowed to view this booking");
        }

        BookingResponse bookingResponse = new BookingResponse(booking);

        List<BookingStatusHistory> statusHistories = statusHistoryRepository.findAllByBooking(booking);

        List<BookingHistory> history = new ArrayList<>();

        statusHistories.forEach(statusHistory -> {
            history.add(new BookingHistory(statusHistory));
        });

        bookingResponse.setHistory(history);

        return bookingResponse;
    }

    private boolean validateBookingView(Booking booking) {
        HrmsUser user = getCurrentUser();

        if (user == null) {
            return false;
        }

        return booking.getCreatedBy().isPresent() && booking.getCreatedBy().get().getUuid().equals(user.getUuid());
    }
}
