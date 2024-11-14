package com.github.abhishek_rabidas.Hotel_Integration_API.models.core;


import lombok.Getter;

import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Privilege extends BaseEntity {
    private String name;
}
