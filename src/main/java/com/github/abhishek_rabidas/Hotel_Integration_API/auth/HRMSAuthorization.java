package com.github.abhishek_rabidas.Hotel_Integration_API.auth;

public final class HRMSAuthorization {

    public static final class Privileges {
        public static final String SELF_READ = "ROLE_SELF_READ";
        public static final String SELF_WRITE = "ROLE_SELF_WRITE";
        public static final String HOTEL_READ = "ROLE_HOTEL_READ";
        public static final String HOTEL_WRITE = "ROLE_HOTEL_WRITE";
        public static final String BOOKING_WRITE = "ROLE_BOOKING_WRITE";
        public static final String BOOKING_READ = "ROLE_BOOKING_READ";
    }
}
