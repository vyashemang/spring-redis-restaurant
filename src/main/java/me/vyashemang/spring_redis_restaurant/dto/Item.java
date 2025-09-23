package me.vyashemang.spring_redis_restaurant.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Item {
    private Long menuItemId;
    private Integer quantity;
}
