package com.ceres.open.server.service;

public interface DeliverMessageService {

    void deliver(String topic,Object obj);
}
