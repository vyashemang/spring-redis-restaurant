package me.vyashemang.spring_redis_restaurant.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestaurantDTO implements Serializable {
    private String name;
    private String address;
    private String phoneNumber;
    private List<MenuItemDTO> menuItems;
}
