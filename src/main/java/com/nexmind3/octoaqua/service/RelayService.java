package com.nexmind3.octoaqua.service;

import com.nexmind3.octoaqua.entity.Relay;
import com.nexmind3.octoaqua.entity.RelayLog;
import com.nexmind3.octoaqua.repository.RelayLogRepository;
import com.nexmind3.octoaqua.repository.RelayRepository;
import com.nexmind3.octoaqua.dto.ESP32Response;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RelayService {
    private final RelayRepository relayRepository;
    private final ESP32Service esp32Service;
    private final RelayLogService relayLogService;
    private final RelayLogRepository relayLogRepository;

    public void turnOffAllRelaysInBatches() {
        // Tüm röleleri al
        List<Relay> allRelays = relayRepository.findAll();

        // 10'lu gruplar halinde işleyin
        int batchSize = 10;
        for (int i = 0; i < allRelays.size(); i += batchSize) {
            // Röle listesinden 10'luk bir grup oluştur
            List<Relay> batch = allRelays.subList(i, Math.min(i + batchSize, allRelays.size()));

            for (Relay relay : batch) {
                relay.setStatus(false);
                esp32Service.toggleRelay(relay.getName(), false);
            }

            relayRepository.saveAll(batch);
        }
    }
    public RelayService(RelayRepository relayRepository, ESP32Service esp32Service, RelayLogService relayLogService, RelayLogRepository relayLogRepository) {
        this.relayRepository = relayRepository;
        this.esp32Service = esp32Service;
        this.relayLogService = relayLogService;
        this.relayLogRepository = relayLogRepository;
    }

    public List<Relay> getAllRelays() {
        return relayRepository.getAllSorted();
    }

    public Relay updateRelayStatus(String xName, boolean status, boolean isManual) {

        Relay relay = relayRepository.findByName(xName);

        if (relay == null) {
            throw new RuntimeException("Relay not found: " + xName);
        }

        List<ESP32Response> esp32Responses = esp32Service.toggleRelay(xName, status);

        if (esp32Responses == null || esp32Responses.isEmpty()) {
            return null;
        }

        ESP32Response latestResponse = esp32Responses.get(esp32Responses.size() - 1);

        System.out.println("Gateway Yanıtı:");
        System.out.println(latestResponse.toString());

        relay.setStatus(status);
        try {
            // Regular expression to find a floating-point number
            String regex = "\\d+\\.\\d+";

            // Use Pattern and Matcher to extract the float value
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(latestResponse.getVoltage());

            if (matcher.find()) {
                // Parse the matched value as a float
                float voltage = Float.parseFloat(matcher.group());
                relay.setVoltage(voltage);
                System.out.println("Extracted voltage: " + voltage);
            } else {
                System.out.println("No float value found in the string.");
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        relay = relayRepository.save(relay);
        relayLogService.addRelayLog(relay, status, isManual);
        System.out.println("Relay: " + relay.getName() + " | Status: " + (status ? "ON" : "OFF") + " | Timestamp: " + LocalDateTime.now());

        return relay;
    }

    public Relay renameRelay(Long id, String newName) {
        Relay relay = relayRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Relay not found with ID: " + id));
        relay.setName(newName);

        // Güncellenmiş röleyi kaydet
        return relayRepository.save(relay);

    }

    public void updateAllRelayVoltages() {
        // Veritabanındaki tüm röleleri alın
        List<Relay> allRelays = relayRepository.findAll();

        for (Relay relay : allRelays) {
            try {
                // ESP32'ye istek gönder ve yanıtı al
                List<ESP32Response> responses = esp32Service.toggleRelay(relay.getName(), relay.getStatus());

                // Yanıtı işleyin ve son elemandaki voltaj bilgisini alın
                if (responses != null && !responses.isEmpty()) {
                    ESP32Response lastResponse = responses.get(responses.size() - 1);
                    try {
                        relay.setVoltage(Float.parseFloat(lastResponse.getVoltage()));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }

                // Güncellenmiş röleyi kaydedin
                relayRepository.save(relay);

            } catch (Exception e) {
                System.err.println("Röle güncellenirken hata oluştu: " + relay.getName());
                e.printStackTrace();
            }
        }
    }

    @Scheduled(fixedRate=300000)
    public void autoTurnOffRelays(){
        try {
            List<RelayLog> activeRelayLogs = getRelaysOpenLongerThanThirtyMinutes();
            System.out.println(LocalDateTime.now() + " Kontrol yapıldı");
            activeRelayLogs.forEach(relayLog -> {
                System.out.println(relayLog.getRelay().getXName() + " bulundu.");
                esp32Service.toggleRelay(relayLog.getRelay().getXName(), false);
                System.out.println(relayLog.getRelay().getXName() + " kapatıldı.");

                RelayLog newLog = new RelayLog();
                newLog.setRelay(relayLog.getRelay());
                newLog.setStatus(false); // Röle kapalı
                newLog.setIsManual(false); // Otomatik kapatıldı
                newLog.setActionTimestamp(LocalDateTime.now()); // Şu anki zaman
                relayLogRepository.save(newLog); // Log kaydını kaydet
            });
        } catch (Exception e) {
            System.err.println("Hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<RelayLog> getRelaysOpenLongerThanThirtyMinutes() {
        return relayRepository.findIdleRelays(30);
    }
}
