package com.home.book.controller;

import com.home.book.model.AttributeFilial;
import com.home.book.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Контроллер пользователей
 */
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Получить всех пользователей из AD
     *
     * @return Список всех пользователей из AD
     */
    @GetMapping("/api/get-all-users")
    public Set<AttributeFilial> getAllUsers() {
        return userService.getAllUsers();
    }


}
