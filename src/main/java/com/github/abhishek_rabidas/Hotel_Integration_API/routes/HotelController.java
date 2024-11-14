package com.github.abhishek_rabidas.Hotel_Integration_API.routes;

import com.github.abhishek_rabidas.Hotel_Integration_API.dto.CreateHotelRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.HotelResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.PageResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.service.HotelService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import static com.github.abhishek_rabidas.Hotel_Integration_API.auth.HRMSAuthorization.Privileges.HOTEL_READ;
import static com.github.abhishek_rabidas.Hotel_Integration_API.auth.HRMSAuthorization.Privileges.HOTEL_WRITE;

@RestController
@RequestMapping("/api/hotel")
public class HotelController {

    private final HotelService hotelService;

    public HotelController (HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @PostMapping
    @Secured(HOTEL_WRITE)
    public ResponseEntity<HotelResponse> createHotel(@RequestBody CreateHotelRequest createHotelRequest) {
        return ResponseEntity.ok(hotelService.createHotel(createHotelRequest));
    }

    @GetMapping
    @Secured(HOTEL_READ)
    public ResponseEntity<PageResponse<HotelResponse>> getAllHotels(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable paging = PageRequest.of(page, size);
        return ResponseEntity.ok(hotelService.listHotels(paging));
    }

    @GetMapping("/{id}")
    @Secured(HOTEL_READ)
    public ResponseEntity<HotelResponse> getHotel(@PathVariable String id) {
        return ResponseEntity.ok(hotelService.fetchHotelDetails(id));
    }

    @PutMapping("/deactivate/{id}")
    @Secured(HOTEL_WRITE)
    public void archiveHotel(@PathVariable String id) {
        hotelService.changeHotelStatus(id, false);
    }

    @PutMapping("/activate/{id}")
    @Secured(HOTEL_WRITE)
    public void makeHotelActive(@PathVariable String id) {
        hotelService.changeHotelStatus(id, true);
    }
}
