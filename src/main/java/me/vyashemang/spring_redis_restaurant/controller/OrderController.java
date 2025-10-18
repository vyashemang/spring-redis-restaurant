package me.vyashemang.spring_redis_restaurant.controller;

import me.vyashemang.spring_redis_restaurant.dto.OrderDTO;
import me.vyashemang.spring_redis_restaurant.dto.OrderStatusDTO;
import me.vyashemang.spring_redis_restaurant.model.OrderStatus;
import me.vyashemang.spring_redis_restaurant.response.BaseResponse;
import me.vyashemang.spring_redis_restaurant.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/{id}/status")
    public ResponseEntity<BaseResponse<String>> updateOrderStatus(@PathVariable("id") Long orderId, @RequestBody OrderStatusDTO orderStatusDTO) {
        try {
            orderService.updateOrderStatus(orderId, OrderStatus.valueOf(orderStatusDTO.getStatus()));
            BaseResponse<String> response = BaseResponse.success("Order status updated successfully");
            return ResponseEntity.status(200).body(response);
        } catch (RuntimeException e) {
            BaseResponse<String> response = BaseResponse.notFound(e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            BaseResponse<String> response = BaseResponse.error(500,
                    "Failed to update status for order: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}
