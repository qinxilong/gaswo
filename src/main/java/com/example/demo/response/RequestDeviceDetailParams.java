package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDeviceDetailParams extends RequestBasePageParams {
    private String id;
    private String roomId;//分厂
    private String workShop;//车间
    private String position;//位置
    private String deviceName;//设备名称
    private String deviceType;//规格型号
    private String deviceCode;//设备自编码
}
