package com.home.book.controller;

import com.home.book.service.ReloadService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер ручной синхронизации с AD
 */
@RestController
public class ReloadController {
    private final ReloadService reloadService;

    public ReloadController(ReloadService reloadService) {
        this.reloadService = reloadService;
    }

    /**
     * Ручная принудительная синхронизация с AD
     */
    @PostMapping("api/reload")
    public void reload() {
        reloadService.reload();
    }
}
