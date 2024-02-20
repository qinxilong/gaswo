package com.example.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 主机、网管设备信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("gas_equipment")
public class Equipment {
    @TableId(value= "id",type = IdType.AUTO)
    private Long id;//主键
    private String deviceId;//设备id
    private String deviceName;//设备名称
    private String deviceType;//设备类型
    private String roomId;//分厂
    private String status;//设备状态
}
