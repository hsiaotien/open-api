package com.ceres.open.client.service.impl;

import com.ceres.open.client.service.ClientService;
import com.ceres.open.client.service.EmptyClient;
import dto.Ping;
import dto.TradeSub;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import util.JsonUtils;

@Service
@Slf4j
public class ClientServiceImpl implements ClientService {

    @Autowired
    private EmptyClient emptyClient;

    @Override
    public void ping() {
        Ping ping = new Ping();
        ping.setType("ping");
        ping.setValue(System.currentTimeMillis());
        try {
            emptyClient.send(JsonUtils.serialize(ping));
        } catch (Exception e) {
            // net error 或者 服务端断开
            emptyClient.reconnect();
            log.info("重连");
            // 再次订阅trade
            try {
                // 等待连接成功
                Thread.sleep(1000*2);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            TradeSub tradeSub = new TradeSub();
            tradeSub.setType("subscribed-trade");
            tradeSub.setSymbol("HUOBI/BYC_USDT");
            emptyClient.send(JsonUtils.serialize(tradeSub));
            log.info("再次订阅");
        }

    }

    @Override
    public void subTrade(TradeSub tradeSub) {
        emptyClient.send(JsonUtils.serialize(tradeSub));
    }
}
