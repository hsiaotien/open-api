package com.ceres.open.client.service;

import dto.TradeSub;

public interface ClientService {

    void ping();

    void subTrade(TradeSub tradeSub);
}
