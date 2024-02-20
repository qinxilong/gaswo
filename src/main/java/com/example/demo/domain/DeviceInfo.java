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
@TableName("gas_device_info")
public class DeviceInfo{
    @TableId(value= "id",type = IdType.AUTO)
    private String id;
    private String deviceId;//设备id
    private String roomId;//分厂
    private String gasType;//气体类型
    private String position;//位置
    private String alarmType;//告警类型
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alarmTime;//首次告警时间
    @TableField(exist = false)
    private String workOrderStatus;//工单处理状态（已派发、 未派发）
    private String workShop;//车间
    private String host;//主机
    private String channel;//通道号
    private String gValue;//气体实时值
    private int  alarmLowThreshold;//低报阈值 24
    private int  alarmHighThreshold;//高报阈值 100    private String host;//主机
    private String number;//厂家设备编号
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reportTime;//数据上报时间


}
