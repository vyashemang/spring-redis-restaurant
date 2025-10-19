package me.vyashemang.spring_redis_restaurant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.vyashemang.spring_redis_restaurant.dto.OrderEventDTO;
import me.vyashemang.spring_redis_restaurant.dto.OrderItemEventDTO;
import me.vyashemang.spring_redis_restaurant.model.Order;
import me.vyashemang.spring_redis_restaurant.model.OrderItem;
import me.vyashemang.spring_redis_restaurant.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static me.vyashemang.spring_redis_restaurant.constant.RedisPubSubConstants.*;

@Service
public class OrderEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void publishOrderCreatedEvent(Order order) {
        publishOrderEvent(order, "ORDER_CREATED");
    }

    public void publishOrderUpdatedEvent(Order order) {
        publishOrderEvent(order, "ORDER_UPDATED");
    }

    public void publishOrderAssignmentRetryEvent(Order order) {
        publishOrderAssignmentRetryEvent(order, "RETRY_ORDER_ASSIGNMENT");
    }

    public void publishOrderCancelledEvent(Order order) {
        publishOrderEvent(order, "ORDER_CANCELLED");
    }

    private void publishOrderEvent(Order order, String eventType) {
        try {
            OrderEventDTO orderEvent = mapOrderToEventDTO(order, eventType);
            String eventJson = objectMapper.writeValueAsString(orderEvent);

            if (eventType.equals("ORDER_CREATED")) {
                stringRedisTemplate.convertAndSend(ORDER_EVENTS_CHANNEL, eventJson);
            }
            stringRedisTemplate.convertAndSend(NOTIFICATION_CHANNEL, eventJson);

            logger.info("Published {} event for order ID: {}", eventType, order.getId());

        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize order event for order ID: {}", order.getId(), e);
        } catch (Exception e) {
            logger.error("Failed to publish order event for order ID: {}", order.getId(), e);
        }
    }

    private void publishOrderAssignmentRetryEvent(Order order, String eventType) {
        try {
            OrderEventDTO orderEvent = mapOrderToEventDTO(order, eventType);
            String eventJson = objectMapper.writeValueAsString(orderEvent);

            stringRedisTemplate.convertAndSend(RETRY_ORDER_ASSIGNMENT_CHANNEL, eventJson);

            logger.info("Published {} event for order ID: {}", eventType, order.getId());
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize order event for order ID: {}", order.getId(), e);
        } catch (Exception e) {
            logger.error("Failed to publish order event for order ID: {}", order.getId(), e);
        }
    }

    public OrderEventDTO mapOrderToEventDTO(Order order, String eventType) {
        // Get restaurant name
        String restaurantName = order.getRestaurant() != null ?
                order.getRestaurant().getName() : "Unknown Restaurant";

        // Map order items
        List<OrderItemEventDTO> itemEvents = order.getOrderItems().stream()
                .map(this::mapOrderItemToEventDTO)
                .collect(Collectors.toList());

        return new OrderEventDTO()
                .setOrderId(order.getId())
                .setUserId(order.getUserId())
                .setRestaurantId(order.getRestaurantId())
                .setRestaurantName(restaurantName)
                .setStatus(order.getStatus())
                .setTotalPrice(order.getTotalPrice())
                .setCreatedAt(order.getCreatedAt())
                .setItems(itemEvents)
                .setEventType(eventType)
                .setEventTimestamp(LocalDateTime.now());
    }

    private OrderItemEventDTO mapOrderItemToEventDTO(OrderItem orderItem) {
        // You might want to fetch menu item details here for the name
        String menuItemName = "Menu Item " + orderItem.getMenuItemId(); // Placeholder

        return new OrderItemEventDTO()
                .setMenuItemId(orderItem.getMenuItemId())
                .setMenuItemName(menuItemName)
                .setQuantity(orderItem.getQuantity())
                .setPrice(orderItem.getPrice())
                .setTotalPrice(orderItem.getPrice().multiply(java.math.BigDecimal.valueOf(orderItem.getQuantity())));
    }
}
