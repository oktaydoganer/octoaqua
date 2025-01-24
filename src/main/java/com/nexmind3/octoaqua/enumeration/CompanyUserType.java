package com.nexmind3.octoaqua.enumeration;

import lombok.Getter;

@Getter
public enum CompanyUserType {
    ADMIN(0),
    OWNER(1),
    MECHANIC(2),
    CUSTOMER(3),
    TEMP_CUSTOMER(4);

    private int companyUserType;

    CompanyUserType(int companyUserType){
        this.companyUserType = companyUserType;
    }
}
