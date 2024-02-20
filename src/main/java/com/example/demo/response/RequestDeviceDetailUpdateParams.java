package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDeviceDetailUpdateParams extends RequestBaseParams {
    private String roomId;//分厂
    private String workShop;//车间
    private String position;//位置
    private String deviceName;//设备名称
    private String deviceType;//规格型号
    private String deviceCode;//设备自编码
}
