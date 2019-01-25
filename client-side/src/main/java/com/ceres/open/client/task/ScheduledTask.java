package com.ceres.open.client.task;

import com.ceres.open.client.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTask {

    @Autowired
    private ClientService clientService;

    @Scheduled(fixedRate = 1000 * 5)
    public void ping() {
        try {
            clientService.ping();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
