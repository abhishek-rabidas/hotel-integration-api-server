package com.github.abhishek_rabidas.Hotel_Integration_API.dao;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends BaseRepository<Hotel, Long> {
}
