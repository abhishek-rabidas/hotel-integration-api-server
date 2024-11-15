package com.github.abhishek_rabidas.Hotel_Integration_API.routes;

import com.github.abhishek_rabidas.Hotel_Integration_API.dto.BookingResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.CreateBookingRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.github.abhishek_rabidas.Hotel_Integration_API.auth.HRMSAuthorization.Privileges.BOOKING_READ;
import static com.github.abhishek_rabidas.Hotel_Integration_API.auth.HRMSAuthorization.Privileges.BOOKING_WRITE;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @Secured(BOOKING_WRITE)
    public ResponseEntity<BookingResponse> createBooking(@RequestBody CreateBookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/list/user")
    @Secured(BOOKING_READ)
    public ResponseEntity<List<BookingResponse>> getAllBookingsByUser(@RequestParam(required = false) String userId) {
        return ResponseEntity.ok(bookingService.getBookingsForUser(userId));
    }

    @GetMapping("/view/{id}")
    @Secured(BOOKING_READ)
    public ResponseEntity<BookingResponse> viewBookingDetails(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.viewBookingDetails(id));
    }

    @PutMapping("/cancel/{id}")
    @Secured(BOOKING_WRITE)
    public void cancelBooking(@PathVariable String id) {
        bookingService.cancelBooking(id);
    }

    @PostMapping("/checkIn/{id}")
    @Secured(BOOKING_WRITE)
    public void checkIn(@PathVariable String id) {
        bookingService.checkInBooking(id);
    }

    @PostMapping("/checkout/{id}")
    @Secured(BOOKING_WRITE)
    public void checkout(@PathVariable String id) {
        bookingService.checkoutBooking(id);
    }
}
