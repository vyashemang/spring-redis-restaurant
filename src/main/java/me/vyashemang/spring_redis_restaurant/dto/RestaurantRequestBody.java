package me.vyashemang.spring_redis_restaurant.dto;

import lombok.Data;

@Data
public class RestaurantRequestBody {
    private String name;
    private String address;
    private String phoneNumber;
}
