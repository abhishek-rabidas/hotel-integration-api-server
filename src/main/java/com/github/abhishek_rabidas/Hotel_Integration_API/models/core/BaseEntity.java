package com.github.abhishek_rabidas.Hotel_Integration_API.models.core;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.HrmsUser;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity extends LazyAuditable<HrmsUser, Long> {
    private String uuid;

    @PrePersist
    private void preCreate() {
        this.setUuid(UUID.randomUUID().toString());
        this.setCreatedDate(LocalDateTime.now());
    }

    @PreUpdate
    private void preUpdate() {
        this.setLastModifiedDate(LocalDateTime.now());

    }
}
