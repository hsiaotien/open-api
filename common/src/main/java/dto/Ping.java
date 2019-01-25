package dto;

import lombok.Data;

@Data
public class Ping {
    private String type;
    private Long value;
}
