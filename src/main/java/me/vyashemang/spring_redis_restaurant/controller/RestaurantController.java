package me.vyashemang.spring_redis_restaurant.controller;

import me.vyashemang.spring_redis_restaurant.dto.MenuItemDTO;
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
            BaseResponse<List<RestaurantDTO>> response = BaseResponse.success("Restaurants fetched successfully",
                    restaurants);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            BaseResponse<List<RestaurantDTO>> response = BaseResponse.error(500,
                    "Failed to fetch restaurants: " + e.getMessage());
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
            BaseResponse<RestaurantDTO> response = BaseResponse.error(500,
                    "Failed to fetch restaurant: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }


    /*===================== Menu Item Endpoints =====================*/
    @GetMapping("/{id}/menu")
    public ResponseEntity<BaseResponse<List<MenuItemDTO>>> getRestaurantMenuItem(@PathVariable("id") Long id) {
        try {
            List<MenuItemDTO> menuItemDTOList = restaurantService.getRestaurantMenu(id);
            BaseResponse<List<MenuItemDTO>> response = BaseResponse.success("Restaurant menu fetched successfully",
                    menuItemDTOList);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            BaseResponse<List<MenuItemDTO>> response = BaseResponse.notFound(e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            BaseResponse<List<MenuItemDTO>> response = BaseResponse.error(500,
                    "Failed to fetch restaurant menu: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/{id}/menu")
    public ResponseEntity<BaseResponse<MenuItemDTO>> createRestaurantMenuItem(@PathVariable("id") Long id, @RequestBody MenuItemDTO menuItemDTO) {
        try {
            MenuItemDTO menuItemDTOResp = restaurantService.createRestaurantMenu(id, menuItemDTO);
            BaseResponse<MenuItemDTO> response = BaseResponse.success("Restaurant menu created successfully",
                    menuItemDTOResp);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            BaseResponse<MenuItemDTO> response = BaseResponse.notFound(e.getMessage());
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            BaseResponse<MenuItemDTO> response = BaseResponse.error(500,
                    "Failed to create restaurant menu: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }}
