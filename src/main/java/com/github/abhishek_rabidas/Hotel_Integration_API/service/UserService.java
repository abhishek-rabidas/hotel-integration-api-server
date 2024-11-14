package com.github.abhishek_rabidas.Hotel_Integration_API.service;

import com.github.abhishek_rabidas.Hotel_Integration_API.dao.UserRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.AuthenticationResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.UserSignupRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.NotFoundException;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.HrmsUser;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.CurrentUser;
import com.github.abhishek_rabidas.Hotel_Integration_API.utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

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
        user.setPasswordHash(encoder.encode(PasswordUtil.convertBase64ToString(request.getPassword())));
        userRepository.save(user);
    }

    public AuthenticationResponse authenticateUser(String email, String password) {
        AuthenticationResponse response = new AuthenticationResponse();
        Optional<HrmsUser> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            if (user.get().isActive()) {
                if (encoder.matches(PasswordUtil.convertBase64ToString(password), user.get().getPasswordHash())) {
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<HrmsUser> user = userRepository.findByEmail(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(String.format("User was not found with email : %s", username));
        }
        return new CurrentUser(user.get(), new String[]{"ROLE_USER"});
    }
}
