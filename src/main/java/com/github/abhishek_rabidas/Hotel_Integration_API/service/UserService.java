package com.github.abhishek_rabidas.Hotel_Integration_API.service;

import com.github.abhishek_rabidas.Hotel_Integration_API.dao.UserRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.UserSignupRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.NotFoundException;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.HrmsUser;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
        user.setPasswordHash(request.getPassword()); // TODO: hash before saving
        userRepository.save(user);
    }
}
