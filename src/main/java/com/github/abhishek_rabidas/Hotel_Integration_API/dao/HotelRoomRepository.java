package com.github.abhishek_rabidas.Hotel_Integration_API.dao;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.Hotel;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.HotelRoom;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRoomRepository extends BaseRepository<HotelRoom, Long> {
    List<HotelRoom> findAllByHotel(Hotel hotel);
    List<HotelRoom> findAllByUuidIn(List<String> list);
}
