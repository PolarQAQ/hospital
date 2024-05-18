package com.example.common.enums;

public enum StatusEnum {
    YES("是"),
    NO("否"),
    ;
    public final String status;

    StatusEnum(String status) {
        this.status = status;
    }
}