package com.github.abhishek_rabidas.Hotel_Integration_API.controller;

import com.github.abhishek_rabidas.Hotel_Integration_API.dto.CreateHotelRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.HotelResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    private final HotelService hotelService;

    public HotelController (HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(@RequestBody CreateHotelRequest createHotelRequest) {
        return ResponseEntity.ok(hotelService.createHotel(createHotelRequest));
    }

    @GetMapping
    public ResponseEntity<?> getAllHotels() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHotel(@PathVariable String id) {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> archiveHotel(@PathVariable String id) {
        return null;
    }
}
