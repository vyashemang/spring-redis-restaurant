package me.vyashemang.spring_redis_restaurant.utils;

import me.vyashemang.spring_redis_restaurant.dto.MenuItemDTO;
import me.vyashemang.spring_redis_restaurant.dto.RestaurantDTO;
import me.vyashemang.spring_redis_restaurant.model.MenuItem;
import me.vyashemang.spring_redis_restaurant.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class RestaurantUtils {
    public static List<MenuItemDTO> mapMenuItemsToDTO(List<MenuItem> menuItems) {
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

    public static List<RestaurantDTO> mapRestaurantListToDTO(List<Restaurant> restaurantList) {
        List<RestaurantDTO> restaurantDTOList = new ArrayList<>();
        for (Restaurant restaurant : restaurantList) {
            restaurantDTOList.add(mapRestaurantToDTO(restaurant));
        }
        return restaurantDTOList;
    }

    public static RestaurantDTO mapRestaurantToDTO(Restaurant restaurant) {
        return new RestaurantDTO()
                .setId(restaurant.getId())
                .setName(restaurant.getName())
                .setAddress(restaurant.getAddress())
                .setPhoneNumber(restaurant.getPhoneNumber())
                .setMenuItems(mapMenuItemsToDTO(restaurant.getMenuItems()));
    }
}
