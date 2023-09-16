package com.unipi.msc.carapi.Model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Car {
    private int id;
    private String model;
    private int price;
}
