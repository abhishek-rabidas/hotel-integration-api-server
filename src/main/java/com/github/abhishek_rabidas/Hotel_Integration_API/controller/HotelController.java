package com.github.abhishek_rabidas.Hotel_Integration_API.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    @PostMapping
    public ResponseEntity<?> createHotel() {
        return null;
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
