package com.example.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("gas_alarm_history")
public class GasAlarmHistory{
    @TableId(value= "id",type = IdType.AUTO)
    private long id;
    private String deviceId;
    private String roomId;//分厂
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date alarmTime;
    private String alarmType;
    private int isAcked;
    private String position;//位置
    private int firstAlarm;//首报 1:是  0:否
    private String workShop;//车间
    private String host;//主机
    private String channel;//通道号
    private String gValue;//气体值
    private String number;//厂家设备编号

}
