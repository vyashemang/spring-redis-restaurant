package me.vyashemang.spring_redis_restaurant.controller;

import me.vyashemang.spring_redis_restaurant.dto.RestaurantDTO;
import me.vyashemang.spring_redis_restaurant.dto.RestaurantRequestBody;
import me.vyashemang.spring_redis_restaurant.response.BaseResponse;
import me.vyashemang.spring_redis_restaurant.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping()
    public ResponseEntity<BaseResponse<List<RestaurantDTO>>> getRestaurants() {
        try {
            List<RestaurantDTO> restaurants = restaurantService.getRestaurants();
            BaseResponse<List<RestaurantDTO>> response = BaseResponse.success("Restaurants fetched successfully", restaurants);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<List<RestaurantDTO>> response = BaseResponse.error(500, "Failed to fetch restaurants: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<RestaurantDTO>> getRestaurantById(@PathVariable("id") Long id) {
        try {
            RestaurantDTO restaurant = restaurantService.getRestaurantById(id);
            BaseResponse<RestaurantDTO> response = BaseResponse.success("Restaurant fetched successfully", restaurant);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            BaseResponse<RestaurantDTO> response = BaseResponse.notFound(e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            BaseResponse<RestaurantDTO> response = BaseResponse.error(500, "Failed to fetch restaurant: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping()
    public ResponseEntity<BaseResponse<String>> createRestaurant(@RequestBody RestaurantRequestBody requestBody) {
        try {
            restaurantService.createRestaurant(requestBody);
            BaseResponse<String> response = BaseResponse.created("Restaurant created successfully", null);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            BaseResponse<String> response = BaseResponse.badRequest("Failed to create restaurant: " + e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }
}
