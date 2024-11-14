package com.github.abhishek_rabidas.Hotel_Integration_API.models.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {
    private String name;
    @ManyToMany
    private Set<Privilege> privileges;
    private int expireAfterHour;
}
