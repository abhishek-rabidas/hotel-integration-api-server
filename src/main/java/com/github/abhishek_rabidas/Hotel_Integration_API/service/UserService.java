package com.github.abhishek_rabidas.Hotel_Integration_API.service;

import com.github.abhishek_rabidas.Hotel_Integration_API.auth.HRMSAuthorization;
import com.github.abhishek_rabidas.Hotel_Integration_API.dao.PrivilegeRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dao.RoleRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dao.UserRepository;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.AuthenticationResponse;
import com.github.abhishek_rabidas.Hotel_Integration_API.dto.UserSignupRequest;
import com.github.abhishek_rabidas.Hotel_Integration_API.exceptions.NotFoundException;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.HrmsUser;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.CurrentUser;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.Privilege;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.Role;
import com.github.abhishek_rabidas.Hotel_Integration_API.utils.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private Role userLevelRole = null;

    private Role adminLevelRole = null;

    @PostConstruct
    public void init() {

        List<String> privileges = HRMSAuthorization.privileges;
        Set<Privilege> privilegeSet = new HashSet<>();
        Set<Privilege> userPrivilegeSet = new HashSet<>();

        for (String privilege : privileges) {
            if (privilegeRepository.countByName(privilege) == 0) {
                Privilege p = new Privilege();
                p.setName(privilege);
                privilegeRepository.saveAndFlush(p);
                privilegeSet.add(p);
                if (!privilege.contains("HOTEL_WRITE")) {
                    userPrivilegeSet.add(p);
                }
                logger.info("Added {} to database", privilege);
            }
        }

        Role userRole = roleRepository.findAllByName("USER_ROLE");
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("USER_ROLE");
            userRole.setExpireAfterHour(730); // 1month
            userRole.setPrivileges(userPrivilegeSet);
            userRole = roleRepository.save(userRole);
            userLevelRole = userRole;
            logger.info("Created USER_ROLE");
        }
        userLevelRole = userRole;

        Role adminRole = roleRepository.findAllByName("ADMIN_ROLE");
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName("ADMIN_ROLE");
            adminRole.setExpireAfterHour(24); // 1 day
            adminRole.setPrivileges(privilegeSet); // giving all the privileges
            adminRole = roleRepository.save(adminRole);
            adminLevelRole = adminRole;
            logger.info("Created ADMIN_ROLE");
        }
        adminLevelRole = adminRole;

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
        user.setRole(userLevelRole);
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
