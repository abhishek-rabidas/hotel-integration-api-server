package com.github.abhishek_rabidas.Hotel_Integration_API.auth;

import java.util.ArrayList;
import java.util.List;

public final class HRMSAuthorization {

    public static final List<String> privileges = List.of("HOTEL_READ", "HOTEL_WRITE", "BOOKING_WRITE", "BOOKING_READ");

    public static final class Privileges {
        public static final String HOTEL_READ = "ROLE_HOTEL_READ";
        public static final String HOTEL_WRITE = "ROLE_HOTEL_WRITE";
        public static final String BOOKING_WRITE = "ROLE_BOOKING_WRITE";
        public static final String BOOKING_READ = "ROLE_BOOKING_READ";
    }
}
