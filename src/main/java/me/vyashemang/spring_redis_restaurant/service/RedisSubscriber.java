package me.vyashemang.spring_redis_restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static me.vyashemang.spring_redis_restaurant.constant.RedisPubSubConstants.ORDER_EVENTS_CHANNEL;

@Service
public class RedisSubscriber {

    @Autowired
    private DeliveryPartnerService deliveryPartnerService;

    public void onMessage(String message, String channel) {
        System.out.println("Received on channel: " + channel + " : " + message);
        if (channel.equals(ORDER_EVENTS_CHANNEL)) {
            deliveryPartnerService.assignDeliverPartner(message);
        }
        //todo: handle notification service
    }
}
