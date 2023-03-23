package com.home.book.service;

import java.util.List;
import java.util.Map;

public interface NumberPoolService {
    Map<String, List<String>> getNumberPool();

    void fillNumberPool();

}
