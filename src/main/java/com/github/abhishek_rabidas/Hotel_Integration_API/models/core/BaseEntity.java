package com.github.abhishek_rabidas.Hotel_Integration_API.models.core;

import com.github.abhishek_rabidas.Hotel_Integration_API.models.HrmsUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.github.abhishek_rabidas.Hotel_Integration_API.service.UserService.getCurrentUser;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity extends LazyAuditable<HrmsUser, Long> {
    @Column(nullable = false, length = 64)
    private String uuid;

    @PrePersist
    private void preCreate() {
        this.setUuid(UUID.randomUUID().toString());
        this.setCreatedDate(LocalDateTime.now());
        HrmsUser currentUser = getCurrentUser();
        if (currentUser != null) {
            this.setCreatedBy(currentUser);
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.setLastModifiedDate(LocalDateTime.now());
        HrmsUser currentUser = getCurrentUser();
        if (currentUser != null) {
            this.setLastModifiedBy(currentUser);
        }
    }
}
