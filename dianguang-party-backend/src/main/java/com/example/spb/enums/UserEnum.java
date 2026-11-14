package com.example.spb.enums;

import lombok.Getter;

@Getter
public enum UserEnum {
    user("name"),
    pass("pass");
    private String code;

    UserEnum(String code) {
        this.code = code;
    }
}
