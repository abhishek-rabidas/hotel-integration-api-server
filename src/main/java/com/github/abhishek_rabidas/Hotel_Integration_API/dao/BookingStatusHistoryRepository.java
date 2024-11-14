package com.github.abhishek_rabidas.Hotel_Integration_API.dao;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.Booking;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.BookingStatusHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingStatusHistoryRepository extends BaseRepository<BookingStatusHistory, Long> {
    List<BookingStatusHistory> findAllByBooking(Booking booking);
}
