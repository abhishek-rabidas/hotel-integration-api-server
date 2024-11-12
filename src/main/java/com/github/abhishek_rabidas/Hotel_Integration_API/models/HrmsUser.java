package com.github.abhishek_rabidas.Hotel_Integration_API.models;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.BaseEntity;
import com.github.abhishek_rabidas.Hotel_Integration_API.models.core.LazyAuditable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class HrmsUser extends LazyAuditable<HrmsUser, Long> {

    @Column(nullable = false, length = 64)
    private String uuid;

    @Column(name = "email", nullable = false, unique = true, length = 128)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(name = "active", nullable = false, columnDefinition = "boolean default true")
    private boolean active;

    @Column(name = "phone", length = 32)
    private String phone;

}
