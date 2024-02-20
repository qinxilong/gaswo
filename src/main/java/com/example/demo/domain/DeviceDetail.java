package com.example.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("gas_device_detail")
public class DeviceDetail {
    @TableId(value = "id", type = IdType.AUTO)
    private String id;//
    private String deviceCode;//设备自编码
    private String deviceName;//设备名称
    private String deviceType;//规格型号
    private String num;//数量
    private String roomId;//分厂
    private String workShop;//车间
    private String position;//位置
    private String host;//主机
    private String channel;//通道号
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date appraisalTime;//鉴定日期

}
