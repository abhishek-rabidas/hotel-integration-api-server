package com.github.abhishek_rabidas.Hotel_Integration_API.routes;

import com.github.abhishek_rabidas.Hotel_Integration_API.dto.BookingResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.CreateBookingRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.service.BookingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public void createBooking(@RequestBody CreateBookingRequest request) {
        bookingService.createBooking(request);
    }

    @GetMapping("/list/user")
    public List<BookingResponse> getAllBookingsByUser(@RequestParam String userId) {
        return bookingService.getBookingsForUser(userId);
    }

    @PutMapping("/cancel/{id}")
    public void cancelBooking(@PathVariable String id) {
        bookingService.cancelBooking(id);
    }

    @PostMapping("/checkIn/{id}")
    public void checkIn(@PathVariable String id) {
        bookingService.checkInBooking(id);
    }

    @PostMapping("/checkout/{id}")
    public void checkout(@PathVariable String id) {
        bookingService.cancelBooking(id);
    }
}
