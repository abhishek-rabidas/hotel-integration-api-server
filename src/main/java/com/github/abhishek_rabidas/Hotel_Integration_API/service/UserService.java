package com.github.abhishek_rabidas.Hotel_Integration_API.service;

import com.github.abhishek_rabidas.Hotel_Integration_API.dao.UserRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.AuthenticationResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.UserSignupRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.NotFoundException;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.HrmsUser;
import com.github.abhishek_rabidas.Hotel_Integration_API.utils.PasswordUtil;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {

    }

    public void signUp(UserSignupRequest request) {
        if (!StringUtils.hasLength(request.getFirstName())) {
            throw new NotFoundException("First name not found in request");
        }

        if (!StringUtils.hasLength(request.getEmail())) {
            throw new NotFoundException("Email address not found in request");
        }

        if (!StringUtils.hasLength(request.getPhone())) {
            throw new NotFoundException("Phone number not found in request");
        }

        if (!StringUtils.hasLength(request.getPassword())) {
            throw new NotFoundException("Password not found in request");
        }

        HrmsUser user = new HrmsUser();
        user.setActive(true);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());
        user.setPasswordHash(PasswordUtil.convertBase64ToString(request.getPassword())); // TODO: hash before saving
        userRepository.save(user);
    }

    public AuthenticationResponse authenticateUser(String email, String password) {
        AuthenticationResponse response = new AuthenticationResponse();
        Optional<HrmsUser> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            if (user.get().isActive()) {
                if (PasswordUtil.convertBase64ToString(password).equals(user.get().getPasswordHash())) {
                    response.setValidated(true);
                    response.setMessage("Login successful");
                } else {
                    response.setValidated(false);
                    response.setMessage("Incorrect password");
                }
            } else {
                response.setValidated(false);
                response.setMessage("User account has been locked");
            }
        } else {
            response.setValidated(false);
            response.setMessage("No user found with " + email);
        }
        return response;
    }
}
