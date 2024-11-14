package com.github.abhishek_rabidas.Hotel_Integration_API.models.core;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.HrmsUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
public class CurrentUser extends User {

    private HrmsUser user;

    public CurrentUser(HrmsUser user, String[] roles) {
        super(user.getEmail(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(roles));
        this.user = user;
    }
}