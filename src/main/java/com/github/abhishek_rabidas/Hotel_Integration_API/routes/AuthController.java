package com.github.abhishek_rabidas.Hotel_Integration_API.routes;

import com.github.abhishek_rabidas.Hotel_Integration_API.dto.AuthenticationResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.LoginRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.UserSignupRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@RestController
@CrossOrigin(originPatterns = "*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Value("${jwt.secret.key}")
    private String secretKey;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    @PostMapping("/signup")
    public void signup(@RequestBody UserSignupRequest request) {
        userService.signUp(request);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest loginRequest) {
        AuthenticationResponse response = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (response.isValidated()) {
            String authToken = Jwts.builder()
                    .setSubject(loginRequest.getEmail())
                    .setIssuedAt(new Date())
                    .setExpiration(response.getExpiresAt())
                    .signWith(key).compact();
            response.setToken(authToken);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
