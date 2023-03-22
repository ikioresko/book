package com.home.book.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Objects;

/**
 * Модель данных пользователя AD
 */
public class AttributeUser {
    private final String name;
    private final String description;
    private final String department;
    private final String telephoneNumber;
    private final String ipPhone;
    private final String businessCategory;

    public AttributeUser(String name, String description, String department,
                         String telephoneNumber, String ipPhone, String businessCategory) {
        this.name = name;
        this.description = description;
        this.department = department;
        this.telephoneNumber = telephoneNumber;
        this.ipPhone = ipPhone;
        this.businessCategory = businessCategory;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getIpPhone() {
        return ipPhone;
    }

    @JsonIgnore
    public String getDepartment() {
        return department;
    }


    public String getBusinessCategory() {
        return businessCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AttributeUser that = (AttributeUser) o;
        return Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(department, that.department)
                && Objects.equals(telephoneNumber, that.telephoneNumber)
                && Objects.equals(ipPhone, that.ipPhone)
                && Objects.equals(businessCategory, that.businessCategory);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, department, telephoneNumber, ipPhone, businessCategory);
    }
}
