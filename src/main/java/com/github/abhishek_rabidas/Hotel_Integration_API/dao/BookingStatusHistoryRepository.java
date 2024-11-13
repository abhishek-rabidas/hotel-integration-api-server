package com.github.abhishek_rabidas.Hotel_Integration_API.dao;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.BookingStatusHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingStatusHistoryRepository extends BaseRepository<BookingStatusHistory, Long> {
}
