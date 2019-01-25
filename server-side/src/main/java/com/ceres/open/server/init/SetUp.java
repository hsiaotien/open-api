package com.ceres.open.server.init;

import com.ceres.open.server.service.SimpleServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order
@Slf4j
public class SetUp implements ApplicationRunner {

    @Autowired
    private SimpleServer simpleServer;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                simpleServer.run();
            }
        });
        thread.start();
        log.info("setup已经执行");
    }
}
