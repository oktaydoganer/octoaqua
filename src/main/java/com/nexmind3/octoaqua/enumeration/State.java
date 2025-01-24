package com.nexmind3.octoaqua.enumeration;

import lombok.Getter;

@Getter
public enum State {
    PASSIVE(0),
    ACTIVE(1);

    private Integer state;

    State(Integer state){
        this.state = state;
    }
}
