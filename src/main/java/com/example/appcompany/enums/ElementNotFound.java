package com.example.appcompany.enums;

public enum ElementNotFound {

    ADDRESS("Address topilmadi"),
    WORKER("Ishchi topilmadi"),
    COMPANY("Companiya topilmadi"),
    DEPARTMENT("Bo'lim topilmadi");

    private final String entityType;

    ElementNotFound(String entityType) {
        this.entityType = entityType;
    }

    public String getMessage() {
        return entityType;
    }
}
