package com.ceres.open.server.controller;

import dto.HostMarkValue;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static constants.Const.COUNT_CONN;
import static constants.Const.COUNT_WEIGHT;

@RestController
@RequestMapping("/monitor")
@SuppressWarnings("all")
public class MonitorController {

    /**
     * rest接口剩余访问次数
     * @param request
     * @return
     */
    @GetMapping("rest/self")
    public ResponseEntity viewRestSelf(HttpServletRequest request) {
        return ResponseEntity.ok(new HostMarkValue(request.getRemoteHost(),COUNT_WEIGHT.get(request.getRemoteHost())==null ? 500 :
                500-COUNT_WEIGHT.get(request.getRemoteHost())>0 ?
                        500-COUNT_WEIGHT.get(request.getRemoteHost()) : 0));
    }

    /**
     * 所有ip(被统计）rest接口剩余访问次数
     * @return
     */
    @GetMapping("rest/admin")
    public ResponseEntity viewRestAll() {
        Set<Map.Entry<String, Integer>> entrySet = COUNT_WEIGHT.entrySet();
        List<HostMarkValue> all = entrySet.stream().map(entry ->
                new HostMarkValue(entry.getKey(), entry.getValue() == null ?
                        500 : (500 - entry.getValue() > 0 ?
                        500 - entry.getValue() : 0)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(all);
    }

    /**
     * 单个ip，单位时间剩余连接次数
     * @param request
     * @return
     */
    @GetMapping("conn/self")
    public ResponseEntity viewConnSelf(HttpServletRequest request) {
        return ResponseEntity.ok(new HostMarkValue(request.getRemoteHost(),COUNT_CONN.get(request.getRemoteHost())==null?
                20 : 20-COUNT_CONN.get(request.getRemoteHost()) > 0 ?
                20-COUNT_CONN.get(request.getRemoteHost()) : 0 ));
    }

    /**
     * 所有ip（被统计）单位时间剩余连接次数
     * @return
     */
    @GetMapping("conn/admin")
    public ResponseEntity viewConnAdmin() {
        Set<Map.Entry<String, Integer>> entrySet = COUNT_CONN.entrySet();
        List<HostMarkValue> all = entrySet.stream().map(entry ->
                new HostMarkValue(entry.getKey(), entry.getValue() == null ?
                        20 : (20 - entry.getValue() > 0 ?
                        20 - entry.getValue() : 0)))
                .collect(Collectors.toList());
        return ResponseEntity.ok(all);
    }




}
