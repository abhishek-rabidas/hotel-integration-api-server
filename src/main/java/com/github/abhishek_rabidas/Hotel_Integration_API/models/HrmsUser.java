package com.github.abhishek_rabidas.Hotel_Integration_API.models;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.BaseEntity;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.Privilege;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.Role;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class HrmsUser extends BaseEntity {
    @Column(name = "email", nullable = false, unique = true, length = 128)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private boolean active;

    @Column(name = "phone", length = 32, nullable = true)
    private String phone;

    @Column(length = 100, nullable = false)
    private String firstName;

    private String lastName;

    @OneToOne(fetch = FetchType.EAGER)
    private Role role;

    public Set<String> getPrivileges()
    {
        Set<String> privileges = new HashSet<>();
        for (Privilege privilege : role.getPrivileges()) {
            privileges.add(privilege.getName());
        }
        return privileges;
    }
}
