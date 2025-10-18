package me.vyashemang.spring_redis_restaurant.repository;

import me.vyashemang.spring_redis_restaurant.model.OrderAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderAssignmentRepository extends JpaRepository<OrderAssignment, Long> {
}
