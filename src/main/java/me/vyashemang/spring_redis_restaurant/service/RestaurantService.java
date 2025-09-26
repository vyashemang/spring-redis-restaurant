package me.vyashemang.spring_redis_restaurant.service;

import me.vyashemang.spring_redis_restaurant.dto.MenuItemDTO;
import me.vyashemang.spring_redis_restaurant.dto.RestaurantDTO;
import me.vyashemang.spring_redis_restaurant.dto.RestaurantRequestBody;
import me.vyashemang.spring_redis_restaurant.model.MenuItem;
import me.vyashemang.spring_redis_restaurant.model.Restaurant;
import me.vyashemang.spring_redis_restaurant.repository.MenuItemRepository;
import me.vyashemang.spring_redis_restaurant.repository.RestaurantRepository;
import me.vyashemang.spring_redis_restaurant.utils.RestaurantUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Cacheable(value = "restaurants", key = "'all-restaurants'")
    @Transactional(readOnly = true)
    public List<RestaurantDTO> getRestaurants() {
        List<Restaurant> restaurantList = restaurantRepository.findAllWithMenuItems();
        return RestaurantUtils.mapRestaurantListToDTO(restaurantList);
    }

    @CacheEvict(value = "restaurants", key = "'all-restaurants'", allEntries = true)
    @Transactional
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
        return RestaurantUtils.mapRestaurantToDTO(restaurant);
    }

    /* ===================== Menu Item Service ===================== */

    @Cacheable(value = "restaurants", key = "'menu' + #id")
    @Transactional(readOnly = true)
    public List<MenuItemDTO> getRestaurantMenu(Long id) {
        Restaurant restaurant = restaurantRepository.findRestaurantById(id);
        List<MenuItem> menuItemList = restaurant.getMenuItems();
        if (menuItemList.isEmpty()) {
            throw new RuntimeException("Restaurant menu not found with id: " + id);
        }
        return RestaurantUtils.mapMenuItemsToDTO(menuItemList);
    }

    @CacheEvict(value = "restaurants", key = "'menu' + #id", allEntries = true)
    public MenuItemDTO createRestaurantMenu(Long id, MenuItemDTO menuItemDTO) {

        Restaurant restaurant = restaurantRepository.findRestaurantById(id);

        if (restaurant == null) {
            throw new RuntimeException("Restaurant not found: " + id);
        }

        MenuItem menuItem = new MenuItem()
                .setName(menuItemDTO.getName())
                .setDescription(menuItemDTO.getDescription())
                .setPrice(menuItemDTO.getPrice())
                .setRestaurant(restaurant)
                .setIsAvailable(menuItemDTO.getIsAvailable() != null ? menuItemDTO.getIsAvailable() : true);

        restaurant.addMenuItem(menuItem);
        restaurantRepository.save(restaurant);

        return menuItemDTO;
    }

}
