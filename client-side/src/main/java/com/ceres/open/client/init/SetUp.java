package com.ceres.open.client.init;

import com.ceres.open.client.service.ClientService;
import com.ceres.open.client.service.EmptyClient;
import dto.TradeSub;
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
    private EmptyClient emptyClient;
    @Autowired
    private ClientService clientService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        emptyClient.connect();
        log.info("连接");
        // 订阅trade
        TradeSub tradeSub = new TradeSub();
        tradeSub.setType("subscribed-trade");
        tradeSub.setSymbol("HUOBI/BYC_USDT");
        clientService.subTrade(tradeSub);
        log.info("订阅subTrade");
    }
}
