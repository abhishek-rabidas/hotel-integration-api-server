package com.github.abhishek_rabidas.Hotel_Integration_API.models.core;

import jakarta.persistence.*;
import org.springframework.data.domain.Auditable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;


@MappedSuperclass
public abstract class LazyAuditable<U, PK extends Serializable> extends CustomAbstractPersistable<PK> implements
        Auditable<U, PK, LocalDateTime> {

    @ManyToOne(fetch = FetchType.LAZY)
    private @Nullable
    U createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    private @Nullable
    Date createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private @Nullable
    U lastModifiedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private @Nullable
    Date lastModifiedDate;

    @Override
    public Optional<U> getCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    @Override
    public void setCreatedBy(U createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public Optional<LocalDateTime> getCreatedDate() {
        return null == createdDate ? Optional.empty()
                : Optional.of(LocalDateTime.ofInstant(createdDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = Date.from(createdDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public Optional<U> getLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    @Override
    public void setLastModifiedBy(U lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public Optional<LocalDateTime> getLastModifiedDate() {
        return null == lastModifiedDate ? Optional.empty()
                : Optional.of(LocalDateTime.ofInstant(lastModifiedDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = Date.from(lastModifiedDate.atZone(ZoneId.systemDefault()).toInstant());
    }
}
