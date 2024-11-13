package com.github.abhishek_rabidas.Hotel_Integration_API.dto;

import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.ValidationException;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.Hotel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Getter
@Setter
public class CreateHotelRequest {
    private String legalName;
    private String hotelCode;
    private String address;
    private Double rating;
    private int taxRate;

    private void validate() {
        if (!StringUtils.hasLength(this.legalName)) {
            throw new ValidationException("Legal name for creating hotel is required");
        }
        if (!StringUtils.hasLength(this.hotelCode)) {
            throw new ValidationException("Hotel code for creating hotel is required");
        }
        if (!StringUtils.hasLength(this.address)) {
            throw new ValidationException("Address for creating hotel is required");
        }
        if (this.taxRate <= 0) {
            throw new ValidationException("Tax rate should be more than 0");
        }
    }

    public Hotel toEntity() {
        validate();
        Hotel hotel = new Hotel();
        hotel.setActive(true);
        hotel.setLegalName(this.legalName);
        hotel.setHotelCode(this.hotelCode);
        hotel.setAddress(this.address);
        hotel.setRating(this.rating);
        hotel.setTaxRate(this.taxRate);
        return hotel;
    }
}
