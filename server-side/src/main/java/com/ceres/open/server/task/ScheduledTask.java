package com.ceres.open.server.task;

import com.ceres.open.server.service.DeliverMessageService;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static constants.Const.*;

@Component
@Slf4j
public class ScheduledTask {

    @Autowired
    private DeliverMessageService deliverService;

    /**
     * 重置REST接口限流阀
     */
    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void countToZero() {
        COUNT_WEIGHT.keySet().forEach(key->COUNT_WEIGHT.put(key,0));
        log.info("重置rest接口限流阀，count clear");
    }

    /**
     * 重置ping计数器
     */
    @Scheduled(fixedRate = 1000 * 10)
    public void checkPing() {
        List<String> rmList = new ArrayList<>();
        Set<Map.Entry<String, Integer>> entrySet = COUNT_PING.entrySet();
        for (Map.Entry<String, Integer> entry : entrySet) {
            if (entry.getValue() < PING_MIN_WEIGHT || entry.getValue() > PING_MAX_WEIGHT) {
                rmList.add(entry.getKey());
            }
        }
        // 移除,10秒低于2次, 或者10秒内ping超过5次，过于频繁
        for (String key : rmList) {
            log.info("hostName:{},即将断开",key);
            COUNT_PING.remove(key);
        }
        // reset
        Set<String> keySet = COUNT_PING.keySet();
        for (String key : keySet) {
            COUNT_PING.put(key,0);
        }
        log.info("目前长连接有{}个，重置ping计数器，count clear",COUNT_PING.size());
    }

    /**
     * 重置当前ip的重试次数。 单位时间，不能超过CONN_WEIGHT
     */
    @Scheduled(fixedRate = 1000 * 60)
    public void resetCountConnect() {
        COUNT_CONN.entrySet().forEach(entry->COUNT_CONN.put(entry.getKey(),0));
        log.info("重置连接限制阀，count clear");
    }

    //===============================模拟部分(开发使用)=============================//
    /**
     *  模拟订阅信息的分发
     */
    @Scheduled(initialDelay = 1000 * 5,fixedRate = 1000 * 20)
    public void deliverTest() {
        String topic = "HUOBI/BYC_USDT";
        String msg = "{\"topic\":\"HUOBI/BYC_USDT\",\"value\":\"hello world\"}";
        deliverService.deliver(topic,msg);
    }

    /**
     * 模拟随机断开一个连接
     */
    @Scheduled(initialDelay = 1000 *30, fixedRate = 1000 * 50)
    public void lostConnectTest() {
        Set<String> set = COUNT_PING.keySet();
        List<String> list = set.stream().collect(Collectors.toList());
        Random random = new Random();
        if (list.size() == 0) {
            return;
        }
        int i = random.nextInt(list.size());
        String connFlag = list.get(i);
        COUNT_PING.remove(connFlag);
        log.info("已经移除{}",connFlag);
    }
}
