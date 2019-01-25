package com.ceres.open.server.service.impl;

import com.ceres.open.server.service.DeliverMessageService;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.springframework.stereotype.Service;
import util.JsonUtils;

import java.util.Collection;
import java.util.Map;

import static constants.Const.TOPIC_FLAG_CONN;

@Service
@Slf4j
public class DeliverMessageServiceImpl implements DeliverMessageService {

    @Override
    public void deliver(String topic, Object obj) {
        Map<String, WebSocket> connMap = TOPIC_FLAG_CONN.get(topic);
        if (connMap == null || connMap.size() == 0) {
            return;
        }
        Collection<WebSocket> webSockets = connMap.values();
        webSockets.stream().forEach(conn->{
            try {
                conn.send(JsonUtils.serialize(obj));
            } catch (Exception e) {
                log.info("连接已经关闭，errorMessage:{}",e.getMessage());
            }
        });
        log.info("消息已经分发.topic:{}，msg:{}",topic,JsonUtils.serialize(obj));
    }
}
