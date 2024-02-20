package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceStatus {
    private String id;//设备id
    private String label;//设备标签
    private String status;//设备状态
    private String roomId;//设备状态
    private String deviceType;//设备类型

}
