package com.nexmind3.octoaqua.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ESP32Response {
    @JsonProperty("UpdateTime")
    private String updateTime;

    @JsonProperty("Relay")
    private String relay;

    @JsonProperty("Voltage")
    private String voltage;








}
