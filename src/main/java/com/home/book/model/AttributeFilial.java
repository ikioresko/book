package com.home.book.model;

import java.util.*;

/**
 * Модель данных пользователя AD
 */
public class AttributeFilial {
    private final String description;
    private final String telephoneNumber;
    private final String businessCategory;
    private final Set<AttributeUser> attributeUsers = new HashSet<>();

    public AttributeFilial(String description, String telephoneNumber, String businessCategory) {
        this.description = description;
        this.telephoneNumber = telephoneNumber;
        this.businessCategory = businessCategory;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public Set<AttributeUser> getAttributeUsers() {
        return attributeUsers;
    }

    public String getDescription() {
        return description;
    }

    public void addUser(AttributeUser user) {
        attributeUsers.add(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttributeFilial that = (AttributeFilial) o;
        return Objects.equals(description, that.description)
                && Objects.equals(telephoneNumber, that.telephoneNumber)
                && Objects.equals(businessCategory, that.businessCategory)
                && Objects.equals(attributeUsers, that.attributeUsers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, telephoneNumber, businessCategory, attributeUsers);
    }
}
