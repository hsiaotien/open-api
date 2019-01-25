package com.ceres.open.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static constants.Const.*;

@RestController
@RequestMapping("api/v1/market")
public class MarketController {

    @GetMapping("trade")
    public ResponseEntity transRecord(HttpServletRequest request) {
        // 是否超过访问限制
        if ( checkTotalAccess(request.getRemoteHost(), TRADE_RECORD) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("禁止调用，超过访问速率限制");
        }
        // TODO
        return ResponseEntity.ok().build();
    }

    @GetMapping("candle")
    public ResponseEntity candleData(HttpServletRequest request) {
        // 是否超过访问限制
        if ( checkTotalAccess(request.getRemoteHost(), CANDLE_DATA) ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("禁止调用，超过访问速率限制");
        }
        // TODO 监控
        return ResponseEntity.ok().build();
    }

    /**
     * ip速率限制
     * @param flag
     * @return
     */
    private synchronized boolean checkTotalAccess(String flag, Integer value) {
        Integer total = COUNT_WEIGHT.get(flag);
        if (total == null) {
            COUNT_WEIGHT.put(flag,value);
            return false;
        }
        COUNT_WEIGHT.put(flag, total+value);
        if (total >= REST_MAX_WEIGHT) {
            return true;
        }
        return false;
    }
}
