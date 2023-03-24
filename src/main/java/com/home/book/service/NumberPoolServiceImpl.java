package com.home.book.service;

import org.springframework.stereotype.Service;
import com.home.book.model.AttributeFilial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Service
public class NumberPoolServiceImpl implements NumberPoolService {
    private final UserService userService;
    private static final Map<String, List<String>> numberPool = new ConcurrentHashMap<>(1500);


    public NumberPoolServiceImpl(UserService userService) {
        this.userService = userService;
        generateNumberPool();
    }

    /**
     * Генерация пула номеров
     */
    private void generateNumberPool() {
        IntStream.rangeClosed(6101, 6951)
                .boxed().map(String::valueOf)
                .forEach(number -> numberPool.put(number, new ArrayList<>()));
    }

    /**
     * Получить пул номеров с их владельцами
     *
     * @return {номер:владелец}
     */
    @Override
    public Map<String, List<String>> getNumberPool() {
        return numberPool;
    }

    /**
     * Заполнить пул номеров с владельцами
     * Выполняется раз в час после синхронизации с AD
     */
    public void fillNumberPool() {
        numberPool.clear();
        generateNumberPool();
        Set<AttributeFilial> allFilial = userService.getAllUsers();
        allFilial
                .stream()
                .flatMap(filial -> filial.getAttributeUsers().stream())
                .forEach(user -> {
                    String telephoneNumber = user.getTelephoneNumber();
                    if (telephoneNumber != null && numberPool.containsKey(telephoneNumber)) {
                        numberPool.get(telephoneNumber).add(user.getName());
                    }
                });
    }
}
