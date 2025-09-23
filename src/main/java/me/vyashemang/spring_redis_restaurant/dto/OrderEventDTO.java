package me.vyashemang.spring_redis_restaurant.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;
import me.vyashemang.spring_redis_restaurant.model.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderEventDTO {
    private Long orderId;
    private Long userId;
    private Long restaurantId;
    private String restaurantName;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private List<OrderItemEventDTO> items;
    private String eventType; // "ORDER_CREATED", "ORDER_UPDATED", "ORDER_CANCELLED", etc.
    private LocalDateTime eventTimestamp;
}