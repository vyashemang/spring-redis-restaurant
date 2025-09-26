package me.vyashemang.spring_redis_restaurant.controller;

import me.vyashemang.spring_redis_restaurant.dto.DeliveryPartnerDTO;
import me.vyashemang.spring_redis_restaurant.response.BaseResponse;
import me.vyashemang.spring_redis_restaurant.service.DeliveryPartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery-partners")
public class DeliveryPartnerController {

    @Autowired
    private DeliveryPartnerService deliveryPartnerService;

    @PostMapping()
    public ResponseEntity createDeliveryPartner(@RequestBody DeliveryPartnerDTO deliveryPartnerDTO) {
        try {
            DeliveryPartnerDTO deliveryPartnerDTOResp = deliveryPartnerService.createDeliveryPartner(deliveryPartnerDTO);
            BaseResponse response = BaseResponse.created("Delivery Partner Created successfully", deliveryPartnerDTOResp);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            BaseResponse<DeliveryPartnerDTO> response = BaseResponse.error(500,
                    "Failed to create delivery partner: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/available")
    public ResponseEntity getAvailableDeliveryPartners() {
        try {
            List<DeliveryPartnerDTO> availableDeliveryPartnerDTOList = deliveryPartnerService.getAvailableDeliveryPartners();
            BaseResponse response = BaseResponse.success(availableDeliveryPartnerDTOList);
            return ResponseEntity.status(200).body(response);
        } catch (RuntimeException e) {
            BaseResponse<List<DeliveryPartnerDTO>> response = BaseResponse.error(500,
                    "Not available delivery partner: " + e.getMessage());
            return ResponseEntity.status(403).body(response);
        } catch (Exception e) {
            BaseResponse<List<DeliveryPartnerDTO>> response = BaseResponse.error(500,
                    "Failed to fetch delivery partner: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
