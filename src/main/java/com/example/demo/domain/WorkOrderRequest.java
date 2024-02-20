package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkOrderRequest {//工单信息
    private String  id;//工单编码
    private String  workOrderCode;//工单编码(不用保存)
    private String  roomId;// 车间名称
    private String  deviceId;//告警设备ID
    private String  position;//告警设备位置
    private String  alarmType;//告警类型(high:高报  low:低报)
//    private String  account;//用户
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;//处理开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//处理完成时间
    private String  pictures;//工作照片
    private String  orderProcessRemark;//工单处理备注
    private String  process_device;//处理设备名称
    private int  processStatus;//处理状态 0:未处理， 1：已处理
    private String number;//厂家设备编号

}
