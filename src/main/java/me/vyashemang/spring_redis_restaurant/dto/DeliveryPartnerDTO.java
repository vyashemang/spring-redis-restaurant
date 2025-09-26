package me.vyashemang.spring_redis_restaurant.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeliveryPartnerDTO {
    private Long id;
    private String name;
    private String phoneNumber;
}
