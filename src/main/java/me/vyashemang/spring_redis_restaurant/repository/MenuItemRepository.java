package me.vyashemang.spring_redis_restaurant.repository;

import me.vyashemang.spring_redis_restaurant.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
