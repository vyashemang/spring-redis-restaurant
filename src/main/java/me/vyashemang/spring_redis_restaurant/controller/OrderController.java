package me.vyashemang.spring_redis_restaurant.controller;

import me.vyashemang.spring_redis_restaurant.dto.OrderDTO;
import me.vyashemang.spring_redis_restaurant.response.BaseResponse;
import me.vyashemang.spring_redis_restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping()
    public ResponseEntity<BaseResponse<OrderDTO>> createOrder(@RequestBody OrderDTO orderDTO) {
        try {
            OrderDTO respOrderDTO = orderService.createOrder(orderDTO);
            BaseResponse<OrderDTO> response = BaseResponse.created("Order placed successfully", respOrderDTO);
            return ResponseEntity.status(200).body(response);
        } catch (RuntimeException e) {
            BaseResponse<OrderDTO> response = BaseResponse.notFound(e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            BaseResponse<OrderDTO> response = BaseResponse.error(500,
                    "Failed to create order: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}
