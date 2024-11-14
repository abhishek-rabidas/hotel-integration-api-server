package com.github.abhishek_rabidas.Hotel_Integration_API.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class AuthenticationResponse {
    private boolean validated;
    private String message;
    private String token;
    private Date expiresAt;
}
