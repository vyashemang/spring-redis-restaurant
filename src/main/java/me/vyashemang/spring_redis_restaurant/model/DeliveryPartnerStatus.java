package me.vyashemang.spring_redis_restaurant.model;

public enum DeliveryPartnerStatus {
    AVAILABLE(0), BUSY(1), OFFLINE(2);

    int value;
    DeliveryPartnerStatus(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
