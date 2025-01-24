package com.nexmind3.octoaqua.service;

import com.nexmind3.octoaqua.dto.ESP32Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ESP32Service {

    public List<ESP32Response> toggleRelay(String relayName, boolean status) {
        String esp32Url = "http://192.168.1.228/?" + relayName + "=" + (status ? "on" : "off");
        RestTemplate restTemplate = new RestTemplate();

        try {
            // ESP32'den yanıt alın
            String jsonResponse = restTemplate.getForObject(esp32Url, String.class);

            // JSONdüzelt
            String fixedJson = fixJson(jsonResponse);

            // JSON parse et
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(fixedJson, new TypeReference<List<ESP32Response>>() {});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JSON'u düzeltmek için kullanılan metot
    private String fixJson(String jsonResponse) {
        return jsonResponse.replaceAll("\"UpdateTime\":(\\d{2}:\\d{2}:\\d{2})", "\"UpdateTime\":\"$1\"");
    }
}
