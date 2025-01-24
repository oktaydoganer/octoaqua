package com.nexmind3.octoaqua.dto;

import com.fasterxml.jackson.annotation.JsonProperty;


public class ESP32Response {
    @JsonProperty("UpdateTime")
    private String updateTime;

    @JsonProperty("Relay")
    private String relay;

    @JsonProperty("Voltage")
    private String voltage;

    // Getter ve Setter
    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getRelay() {
        return relay;
    }

    public void setRelay(String relay) {
        this.relay = relay;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }







}
