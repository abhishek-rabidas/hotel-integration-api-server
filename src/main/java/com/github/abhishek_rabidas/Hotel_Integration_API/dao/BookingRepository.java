package com.github.abhishek_rabidas.Hotel_Integration_API.dao;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.Booking;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.HrmsUser;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends BaseRepository<Booking, Long> {
    List<Booking> findAllByUser(HrmsUser user);
    List<Booking> findAllByBookingToLessThan(Date bookingTo);
}
