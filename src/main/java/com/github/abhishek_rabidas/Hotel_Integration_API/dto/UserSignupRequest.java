package com.github.abhishek_rabidas.Hotel_Integration_API.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignupRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
}
