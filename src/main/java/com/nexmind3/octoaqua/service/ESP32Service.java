package com.nexmind3.octoaqua.service;

import com.nexmind3.octoaqua.dto.ESP32Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ESP32Service {
    @Value("${esp32.url}")
    private String esp32Url;

    public List<ESP32Response> toggleRelay(String relayXName, boolean status) {
        String url = esp32Url + "/?" + relayXName + "=" + (status ? "on" : "off");
        RestTemplate restTemplate = new RestTemplate();

        try {
            // ESP32'den yanıt alın
            String jsonResponse = restTemplate.getForObject(url, String.class);

            // JSON parse et
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(jsonResponse, new TypeReference<List<ESP32Response>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
