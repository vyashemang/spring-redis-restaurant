package me.vyashemang.spring_redis_restaurant.service;

import me.vyashemang.spring_redis_restaurant.dto.MenuItemDTO;
import me.vyashemang.spring_redis_restaurant.dto.RestaurantDTO;
import me.vyashemang.spring_redis_restaurant.dto.RestaurantRequestBody;
import me.vyashemang.spring_redis_restaurant.model.MenuItem;
import me.vyashemang.spring_redis_restaurant.model.Restaurant;
import me.vyashemang.spring_redis_restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Cacheable(value = "restaurants", key = "'all-restaurants'")
    @Transactional(readOnly = true)
    public List<RestaurantDTO> getRestaurants() {
        List<Restaurant> restaurantList = restaurantRepository.findAllWithMenuItems();
        return mapRestaurantListToDTO(restaurantList);
    }

    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    public void createRestaurant(RestaurantRequestBody requestBody) {
        Restaurant restaurant = new Restaurant()
                .setName(requestBody.getName())
                .setAddress(requestBody.getAddress())
                .setPhoneNumber(requestBody.getPhoneNumber());
        
        restaurantRepository.save(restaurant);
    }

    @Cacheable(value = "restaurants", key = "#id")
    @Transactional(readOnly = true)
    public RestaurantDTO getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(id);
        if (restaurant == null) {
            throw new RuntimeException("Restaurant not found with id: " + id);
        }
        return mapRestaurantToDTO(restaurant);
    }

    private List<MenuItemDTO> mapMenuItemsToDTO(List<MenuItem> menuItems) {
        List<MenuItemDTO> menuItemDTOs = new ArrayList<>();

        for (MenuItem menuItem : menuItems) {
            MenuItemDTO menuItemDTO = new MenuItemDTO()
                    .setId(menuItem.getId())
                    .setName(menuItem.getName())
                    .setDescription(menuItem.getDescription())
                    .setPrice(menuItem.getPrice())
                    .setIsAvailable(menuItem.getIsAvailable())
                    .setCreatedAt(menuItem.getCreatedAt());

            menuItemDTOs.add(menuItemDTO);
        }

        return menuItemDTOs;
    }

    private List<RestaurantDTO> mapRestaurantListToDTO(List<Restaurant> restaurantList) {
        List<RestaurantDTO> restaurantDTOList = new ArrayList<>();
        for (Restaurant restaurant : restaurantList) {
            restaurantDTOList.add(mapRestaurantToDTO(restaurant));
        }
        return restaurantDTOList;
    }

    private RestaurantDTO mapRestaurantToDTO(Restaurant restaurant) {
        return new RestaurantDTO()
                .setName(restaurant.getName())
                .setAddress(restaurant.getAddress())
                .setPhoneNumber(restaurant.getPhoneNumber())
                .setMenuItems(mapMenuItemsToDTO(restaurant.getMenuItems()));
    }
}
