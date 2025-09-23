package me.vyashemang.spring_redis_restaurant.repository;

import me.vyashemang.spring_redis_restaurant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u from User u WHERE u.id = :id")
    User getUserById(@Param("id") Long id);

}
