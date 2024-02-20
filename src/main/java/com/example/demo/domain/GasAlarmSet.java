package com.example.demo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("gas_alarm_set")
public class GasAlarmSet {
    private int id = 1;
    private float  alarmLowThreshold;//低报阈值
    private float  alarmHighThreshold;//高报阈值
    private String alarmNotifyType;//通知方式
}

