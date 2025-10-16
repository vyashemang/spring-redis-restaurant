package me.vyashemang.spring_redis_restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisSubscriber {

    @Autowired
    private DeliveryPartnerService deliveryPartnerService;

    public void onMessage(String message, String channel) {
        System.out.println("Received on channel: " + channel + " : " + message);
        deliveryPartnerService.assignDeliverPartner(message);
    }
}
