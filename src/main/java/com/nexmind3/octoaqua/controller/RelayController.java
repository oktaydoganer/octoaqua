package com.nexmind3.octoaqua.controller;

import com.nexmind3.octoaqua.entity.Relay;
import com.nexmind3.octoaqua.service.RelayService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
public class RelayController {
    private final RelayService relayService;

    public RelayController(RelayService relayService) {
        this.relayService = relayService;
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
    public Relay toggleRelay(@PathVariable String xName, @PathVariable boolean status) {
        System.out.println("Röle Güncelleme: " + xName + ", Durum: " + status);
        return relayService.updateRelayStatus(xName, status);
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



}
