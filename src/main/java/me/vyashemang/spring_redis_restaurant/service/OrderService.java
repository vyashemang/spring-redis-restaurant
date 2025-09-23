package me.vyashemang.spring_redis_restaurant.service;

import me.vyashemang.spring_redis_restaurant.dto.Item;
import me.vyashemang.spring_redis_restaurant.dto.OrderDTO;
import me.vyashemang.spring_redis_restaurant.model.*;
import me.vyashemang.spring_redis_restaurant.repository.OrderRepository;
import me.vyashemang.spring_redis_restaurant.repository.RestaurantRepository;
import me.vyashemang.spring_redis_restaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private OrderEventPublisher orderEventPublisher;

    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        User user = userRepository.findById(orderDTO.getUserId()).orElseThrow(
                () -> new RuntimeException("User not found with id: " + orderDTO.getUserId()));

        Restaurant restaurant = restaurantRepository.findById(orderDTO.getRestaurantId()).orElseThrow(
                () -> new RuntimeException("Restaurant not found with id: " + orderDTO.getRestaurantId()));

        Order order = new Order()
                .setUser(user)
                .setUserId(user.getId())
                .setRestaurantId(restaurant.getId())
                .setRestaurant(restaurant)
                .setStatus(OrderStatus.PLACED)
                .setTotalPrice(BigDecimal.ZERO);

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderedItemList = new ArrayList<>();

        for (Item item : orderDTO.getItems()) {
            // Get the menu item information
            MenuItem menuItem = restaurant.getMenuItems().stream()
                    .filter(mi -> mi.getId().equals(item.getMenuItemId()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + item.getMenuItemId()));

            // If the item is not available then throw exception
            if (!menuItem.getIsAvailable()) {
                throw new RuntimeException("Menu item '" + menuItem.getName() + "' is not available");
            }

            // Calculate the price and add it to total price
            BigDecimal itemTotalPrice = menuItem.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
            totalPrice = totalPrice.add(itemTotalPrice);

            // Create order item
            OrderItem orderItem = new OrderItem()
                    .setMenuItemId(menuItem.getId())
                    .setQuantity(item.getQuantity())
                    .setPrice(menuItem.getPrice());

            orderedItemList.add(orderItem);
            order.addOrderItem(orderItem);
        }

        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);

        // Send message to queue with order-events
        orderEventPublisher.publishOrderCreatedEvent(savedOrder);

        // Return the saved order as DTO
        return mapOrderToDTO(savedOrder);
    }

    // Helper method to map Order entity to DTO
    private OrderDTO mapOrderToDTO(Order order) {
        List<Item> items = order.getOrderItems().stream()
                .map(orderItem -> new Item()
                        .setMenuItemId(orderItem.getMenuItemId())
                        .setQuantity(orderItem.getQuantity()))
                .toList();

        return new OrderDTO()
                .setId(order.getId())
                .setUserId(order.getUserId())
                .setRestaurantId(order.getRestaurantId())
                .setItems(items);
    }

}
