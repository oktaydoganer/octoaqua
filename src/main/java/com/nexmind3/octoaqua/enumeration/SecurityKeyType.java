package com.nexmind3.octoaqua.enumeration;

import lombok.Getter;

@Getter
public enum SecurityKeyType {
    CHANGE_PASSWORD(0),
    APPROVE_ACCOUNT(1);

    private int securityKeyType;

    SecurityKeyType(int securityKeyType){
        this.securityKeyType = securityKeyType;
    }
}