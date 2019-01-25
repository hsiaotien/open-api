package dto;

import lombok.Data;

/**
 * 订阅trade
 */
@Data
public class TradeSub {
    private String type;
    private String symbol;
}
