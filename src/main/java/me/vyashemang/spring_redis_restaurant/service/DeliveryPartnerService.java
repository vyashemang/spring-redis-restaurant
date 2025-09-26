package me.vyashemang.spring_redis_restaurant.service;

import me.vyashemang.spring_redis_restaurant.dto.DeliveryPartnerDTO;
import me.vyashemang.spring_redis_restaurant.model.DeliveryPartner;
import me.vyashemang.spring_redis_restaurant.model.DeliveryPartnerStatus;
import me.vyashemang.spring_redis_restaurant.repository.DeliveryPartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryPartnerService {

    @Autowired
    private DeliveryPartnerRepository deliveryPartnerRepository;

    @Transactional
    public DeliveryPartnerDTO createDeliveryPartner(DeliveryPartnerDTO deliveryPartnerDTO) {
        DeliveryPartner deliveryPartner = new DeliveryPartner()
                .setName(deliveryPartnerDTO.getName())
                .setPhoneNumber(deliveryPartnerDTO.getPhoneNumber())
                .setStatus(DeliveryPartnerStatus.AVAILABLE);

        DeliveryPartner savedDeliveryPartner = deliveryPartnerRepository.save(deliveryPartner);
        return mapToDeliveryPartnerDTO(savedDeliveryPartner);
    }

    private DeliveryPartnerDTO mapToDeliveryPartnerDTO(DeliveryPartner deliveryPartner) {
        return new DeliveryPartnerDTO()
                .setId(deliveryPartner.getId())
                .setName(deliveryPartner.getName())
                .setPhoneNumber(deliveryPartner.getPhoneNumber());
    }

}
