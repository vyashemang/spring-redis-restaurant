package me.vyashemang.spring_redis_restaurant.service;

import me.vyashemang.spring_redis_restaurant.dto.UserDTO;
import me.vyashemang.spring_redis_restaurant.model.User;
import me.vyashemang.spring_redis_restaurant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @CacheEvict(value = "users", key = "'all-users'", allEntries = true)
    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User()
                .setName(userDTO.getName())
                .setEmail(userDTO.getEmail())
                .setPasswordHash(userDTO.getPassword())
                .setPhoneNumber(userDTO.getPhoneNumber());

        userRepository.save(user);
        return userDTO;
    }



}
