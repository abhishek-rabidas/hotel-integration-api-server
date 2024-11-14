package com.github.abhishek_rabidas.Hotel_Integration_API.dto;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.BookingStatusHistory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BookingHistory {
    private String status;
    private LocalDateTime timestamp;

    public BookingHistory(BookingStatusHistory statusHistory) {
        this.status = statusHistory.getStatus().name();
        this.timestamp = statusHistory.getCreatedDate().get();
    }
}
