package com.github.abhishek_rabidas.Hotel_Integration_API.models.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Privilege> privileges;
    private int expireAfterHour;
}
