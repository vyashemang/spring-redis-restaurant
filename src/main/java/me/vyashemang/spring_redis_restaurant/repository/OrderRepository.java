package me.vyashemang.spring_redis_restaurant.repository;

import me.vyashemang.spring_redis_restaurant.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
