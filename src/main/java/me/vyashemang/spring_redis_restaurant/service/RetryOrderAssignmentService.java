package me.vyashemang.spring_redis_restaurant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.vyashemang.spring_redis_restaurant.dto.OrderEventDTO;
import me.vyashemang.spring_redis_restaurant.model.Order;
import me.vyashemang.spring_redis_restaurant.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RetryOrderAssignmentService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private DeliveryPartnerService deliveryPartnerService;
    @Autowired
    private OrderEventPublisher orderEventPublisher;
    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 10000)
    public void retryOrderAssignment() {
        try {
            List<Order> pendingOrderList = orderRepository.getPendingOrders();

            pendingOrderList.forEach(order -> {
                OrderEventDTO orderEventDTO = orderEventPublisher.mapOrderToEventDTO(order, "RETRY_ORDER_ASSIGNMENT");
                try {
                    deliveryPartnerService.assignDeliverPartner(objectMapper.writeValueAsString(orderEventDTO));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
