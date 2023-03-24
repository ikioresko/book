package com.home.book.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class ReloadServiceImpl implements ReloadService {
    private final UserService userService;
    private final NumberPoolService numberPoolService;

    public ReloadServiceImpl(UserService userService, NumberPoolService numberPoolService) {
        this.userService = userService;
        this.numberPoolService = numberPoolService;
    }

    /**
     * Синхронизация с AD для актуализации данных
     */
    @Scheduled(fixedDelay = (1000 * 60 * 60))
    @Override
    public void reload() {
        userService.initUsersList();
        numberPoolService.fillNumberPool();
    }
}
