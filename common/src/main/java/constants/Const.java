package constants;

import org.java_websocket.WebSocket;

import java.util.HashMap;
import java.util.Map;

public interface Const {

    /**
     * 总权重
     */
    int REST_MAX_WEIGHT = 500;

    int PING_MAX_WEIGHT = 5;

    int PING_MIN_WEIGHT = 2;

    int CONN_WEIGHT = 20;

    /**
     * 各个接口的权重
     */
    int PING_VALUE = 1;

    int TRADE_RECORD = 1;

    int CANDLE_DATA = 1;


    /**
     * 常量
     */
    String PING = "ping";
    String PONG = "pong";
    String TRADE_TYPE = "subscribed-trade";

    /**
     * 	记录rest访问权重, 进行限流。
     * 	（每个接口根据消耗资源程度，设置权重。 单位时间，单个连接，各个rest接口权重统计值总和不超过REST_MAX_WEIGHT）
     */
    Map<String,Integer> COUNT_WEIGHT = new HashMap<>();

    /**
     * 统计单个连接，单位时间的连接次数
     */
    Map<String,Integer> COUNT_CONN = new HashMap<>();

    /**
     *  ping权重统计。单位时间，单个连接，ping的权重统计和不超过PING_MAX_WEIGHT（防止ping过于频繁）。 并且不低于PING_MIN_WEIGHT (维持心跳）
     */
    Map<String,Integer> COUNT_PING = new HashMap<>();

    /**
     * 记录订阅，长连接
     */
    Map<String,Map<String, WebSocket>> TOPIC_FLAG_CONN =  new HashMap<>();

}
