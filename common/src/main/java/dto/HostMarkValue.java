package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HostMarkValue {

    /**
     * 主机标记
     */
    private String mark;
    /**
     * 剩余数量
     */
    private Integer value;
}
