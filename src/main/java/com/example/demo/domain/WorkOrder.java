package com.example.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("gas_work_order")//燃气工单
public class WorkOrder{ //工单信息
    @TableId(value= "id",type = IdType.AUTO)
    private String  id;
    private String  workOrderCode;//工单编码
    private String  roomId;// 车间名称
    private String  alarmType;//告警类型(high:高报  low:低报)
    private String  alarmLevel;//告警级别
    private String  workOrderType;//工单类型
    private String  taskDescription;//任务描述
    private String  orderIssuanceRemark;//工单下发备注
    private String  processPerson;//处理人员
    private int  processStatus;//处理状态 0:已派发， 1：已完成
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  endTime;//处理完成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  alarmTime;//报警时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date  startTime;//处理开始时间
    private String  deviceId;//告警设备ID
    private String  position;//告警设备位置

    private String  process_device;//处理设备名称

    private String  pictures;//工作照片
    private String  orderProcessRemark;//工单处理备注

    private String  dispatcher;//工单下发用户

    private String workShop;//车间

    private String host;//主机

    private String channel;//通道号
    private String number;//厂家设备编号


    public WorkOrder(WorkOrderRequest workOrderRequest) {
       this.id = workOrderRequest.getId();
       this.workOrderCode = workOrderRequest.getWorkOrderCode();
       this.alarmType = workOrderRequest.getAlarmType();
       this.roomId =  workOrderRequest.getRoomId();
       this.deviceId = workOrderRequest.getDeviceId();
       this.position = workOrderRequest.getPosition();
       this.startTime = workOrderRequest.getStartTime();
       this.endTime = workOrderRequest.getEndTime();
       this.orderProcessRemark = workOrderRequest.getOrderProcessRemark();
       this.number = workOrderRequest.getNumber();
    }
}
