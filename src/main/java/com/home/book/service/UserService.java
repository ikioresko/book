package com.home.book.service;

import com.home.book.model.AttributeFilial;

import java.util.Set;

public interface UserService {
    Set<AttributeFilial> getAllUsers();
    void initUsersList();
}
