package me.vyashemang.spring_redis_restaurant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.vyashemang.spring_redis_restaurant.dto.DeliveryPartnerDTO;
import me.vyashemang.spring_redis_restaurant.dto.OrderEventDTO;
import me.vyashemang.spring_redis_restaurant.model.*;
import me.vyashemang.spring_redis_restaurant.repository.DeliveryPartnerRepository;
import me.vyashemang.spring_redis_restaurant.repository.OrderAssignmentRepository;
import me.vyashemang.spring_redis_restaurant.repository.OrderRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class DeliveryPartnerService {

    @Autowired
    private DeliveryPartnerRepository deliveryPartnerRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private OrderEventPublisher orderEventPublisher;
    @Autowired
    private OrderAssignmentRepository orderAssignmentRepository;

    @Transactional
    public DeliveryPartnerDTO createDeliveryPartner(DeliveryPartnerDTO deliveryPartnerDTO) {
        DeliveryPartner deliveryPartner = new DeliveryPartner()
                .setName(deliveryPartnerDTO.getName())
                .setPhoneNumber(deliveryPartnerDTO.getPhoneNumber())
                .setStatus(DeliveryPartnerStatus.AVAILABLE);

        DeliveryPartner savedDeliveryPartner = deliveryPartnerRepository.save(deliveryPartner);
        return mapToDeliveryPartnerDTO(savedDeliveryPartner);
    }

    @Transactional(readOnly = true)
    public List<DeliveryPartnerDTO> getAvailableDeliveryPartners() {
        List<DeliveryPartner> availableDeliveryPartners = deliveryPartnerRepository.getAvailableDeliverPartners();

        if (availableDeliveryPartners.isEmpty()) {
            throw new RuntimeException("No available delivery partners found");
        }

        return availableDeliveryPartners.stream().map(this::mapToDeliveryPartnerDTO).toList();
    }

    @Transactional
    public void assignDeliverPartner(String orderEvent) {
        String lockKey = "assign_driver_lock";
        RLock lock = redissonClient.getLock(lockKey);

        try {
            OrderEventDTO orderEventDTO = objectMapper.readValue(orderEvent, OrderEventDTO.class);

            // acquire the lock
            if (lock.tryLock(5, 10, TimeUnit.SECONDS)) {
                log.info("Lock acquired for order id: {}", orderEventDTO.getOrderId());
                List<DeliveryPartner> availableDeliverPartners = deliveryPartnerRepository.getAvailableDeliverPartners();

                Order order = orderRepository.getReferenceById(orderEventDTO.getOrderId());

                if (availableDeliverPartners.isEmpty()) {
                    // todo: change the topic for retry and add the scheduler
                    order.setStatus(OrderStatus.PENDING);
                    orderRepository.save(order);
                    orderEventPublisher.publishOrderAssignmentRetryEvent(order);
                    throw new RuntimeException("No delivery partners are available");
                }

                DeliveryPartner chosenDeliveryPartner = availableDeliverPartners.get(0);

                // Mark driver as busy
                chosenDeliveryPartner.setStatus(DeliveryPartnerStatus.BUSY);
                deliveryPartnerRepository.save(chosenDeliveryPartner);

                // Mark order as dispatched
                order.setStatus(OrderStatus.DISPATCHED);
                orderRepository.save(order);
                orderEventPublisher.publishOrderUpdatedEvent(order);

                // Add entry in order assignment
                OrderAssignment orderAssignment = new OrderAssignment()
                        .setOrderId(order.getId())
                        .setDeliveryId(chosenDeliveryPartner.getId())
                        .setOrder(order)
                        .setDeliveryPartner(chosenDeliveryPartner);

                orderAssignmentRepository.save(orderAssignment);
            }
        } catch (JsonProcessingException | InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    private DeliveryPartnerDTO mapToDeliveryPartnerDTO(DeliveryPartner deliveryPartner) {
        return new DeliveryPartnerDTO()
                .setId(deliveryPartner.getId())
                .setName(deliveryPartner.getName())
                .setPhoneNumber(deliveryPartner.getPhoneNumber());
    }

}
