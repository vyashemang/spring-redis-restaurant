package me.vyashemang.spring_redis_restaurant.repository;

import me.vyashemang.spring_redis_restaurant.model.DeliveryPartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryPartnerRepository extends JpaRepository<DeliveryPartner, Long> {

    @Query("select dp from DeliveryPartner dp where dp.status = 'AVAILABLE'")
    List<DeliveryPartner> getAvailableDeliverPartners();
}
