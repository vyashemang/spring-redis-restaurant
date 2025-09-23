package me.vyashemang.spring_redis_restaurant.controller;

import me.vyashemang.spring_redis_restaurant.dto.UserDTO;
import me.vyashemang.spring_redis_restaurant.response.BaseResponse;
import me.vyashemang.spring_redis_restaurant.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<BaseResponse<String>> createUser(@RequestBody UserDTO userDTO) {
        try {
            userService.createUser(userDTO);
            BaseResponse<String> response = BaseResponse.created("User created successfully", null);
            return ResponseEntity.status(201).body(response);
        } catch (Exception e) {
            BaseResponse<String> response = BaseResponse.badRequest("Failed to create user: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}
