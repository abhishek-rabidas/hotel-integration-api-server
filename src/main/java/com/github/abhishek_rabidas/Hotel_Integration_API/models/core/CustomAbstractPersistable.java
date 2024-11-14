package com.github.abhishek_rabidas.Hotel_Integration_API.models.core;


import org.springframework.data.domain.Persistable;
import org.springframework.data.util.ProxyUtils;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Keeping everything same as org.springframework.data.jpa.domain.AbstractPersistable and changed the primary key generation type strategy
 * to not use sequence table for mysql database.
 * @author Abhishek Kumar Rabidas
 *
 * @param <PK>
 */

@MappedSuperclass
public abstract class CustomAbstractPersistable<PK extends Serializable> implements Persistable<PK> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Nullable
    private PK id;

    public CustomAbstractPersistable() {}

    @Nullable
    public PK getId() {
        return this.id;
    }

    protected void setId(@Nullable PK id) {
        this.id = id;
    }

    @Transient
    public boolean isNew() {
        return this.getId() == null;
    }

    public String toString() {
        return String.format("Entity of type %s with id: %s", this.getClass().getName(), this.getId());
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!this.getClass().equals(ProxyUtils.getUserClass(obj))) {
            return false;
        } else {
            org.springframework.data.jpa.domain.AbstractPersistable<?> that = (org.springframework.data.jpa.domain.AbstractPersistable) obj;
            return this.getId() == null ? false : this.getId().equals(that.getId());
        }
    }

    public int hashCode() {
        int hashCode = 17;
        hashCode += this.getId() == null ? 0 : this.getId().hashCode() * 31;
        return hashCode;
    }
}
