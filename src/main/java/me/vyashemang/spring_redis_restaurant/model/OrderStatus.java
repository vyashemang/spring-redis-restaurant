package me.vyashemang.spring_redis_restaurant.model;

public enum OrderStatus {
    PLACED (0), COOKING(1), DISPATCHED(2), DELIVERED(3), CANCELLED(4);

    int value;
    OrderStatus(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
