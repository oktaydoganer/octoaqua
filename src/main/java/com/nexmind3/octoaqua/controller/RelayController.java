package com.nexmind3.octoaqua.controller;

import com.nexmind3.octoaqua.entity.Relay;
import com.nexmind3.octoaqua.entity.RelayLog;
import com.nexmind3.octoaqua.repository.RelayLogRepository;
import com.nexmind3.octoaqua.service.RelayService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
public class RelayController {
    private final RelayService relayService;
    private final RelayLogRepository relayLogRepository;

    public RelayController(RelayService relayService, RelayLogRepository relayLogRepository) {
        this.relayService = relayService;
        this.relayLogRepository = relayLogRepository;
    }

    @PostMapping("/update-all-voltages")
    public String updateAllRelayVoltages() {
        relayService.updateAllRelayVoltages();
        return "Tüm rölelerin voltaj bilgisi güncellendi.";
    }

    @GetMapping("/relays")
    public ModelAndView getAllRelays(ModelAndView mav) {
        mav.setViewName("index");
        mav.addObject("relays", relayService.getAllRelays());
        return mav;
    }

    @GetMapping("/relayList")
    public List<Relay> getRelayList() {
        return relayService.getAllRelays();
    }

    @PostMapping("/toggle/{xName}/{status}")
    public Relay toggleRelay(@PathVariable String xName, @PathVariable boolean status,@RequestParam(defaultValue = "true") boolean isManual) {
        System.out.println("Röle Güncelleme: " + xName + ", Durum: " + status);
        System.out.println("Relay Log Kaydı: " + xName + " | Status: " + (status ? "ON" : "OFF") + " | Timestamp: " + LocalDateTime.now() + " | Is Manual?:" + isManual);
        return relayService.updateRelayStatus(xName, status,isManual);
    }
    @PostMapping("/all/off")
    public String turnOffAllRelaysInBatches() {
        relayService.turnOffAllRelaysInBatches();
        return "Tüm röleler kapatıldı.";
    }
    @PutMapping("/{id}/rename")
    public Relay renameRelay(@PathVariable Long id, @RequestParam String newName) {
        return relayService.renameRelay(id, newName);
    }
    @GetMapping("/relay-logs")
    public List<RelayLog> getAllRelayLogs() {
        return relayLogRepository.findAll();
    }
}
