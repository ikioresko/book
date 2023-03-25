package com.home.book.controller;

import com.home.book.service.NumberPoolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Контроллер пула номеров
 */
@RestController
public class NumberPoolController {
    private final NumberPoolService numberPoolService;

    public NumberPoolController(NumberPoolService numberPoolService) {
        this.numberPoolService = numberPoolService;
    }

    /**
     * Получить пул номеров с их владельцами
     *
     * @return Пул номеров
     */
    @GetMapping("api/get-number-pool")
    public Map<String, List<String>> getNumberPool() {
        return numberPoolService.getNumberPool();
    }
}
