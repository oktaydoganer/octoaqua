package com.nexmind3.octoaqua.service;

import com.nexmind3.octoaqua.entity.Relay;
import com.nexmind3.octoaqua.entity.RelayLog;
import com.nexmind3.octoaqua.repository.RelayLogRepository;
import org.springframework.stereotype.Service;

@Service
public class RelayLogService {
    private final RelayLogRepository relayLogRepository;

    public RelayLogService(RelayLogRepository relayLogRepository) {
        this.relayLogRepository = relayLogRepository;
    }

    public void addRelayLog(Relay relay, Boolean status, Boolean isManual) {
        RelayLog log = new RelayLog();
        log.setRelay(relay);
        log.setStatus(status);
        log.setIsManual(isManual);
        relayLogRepository.save(log);
    }
}
