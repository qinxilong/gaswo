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
public class WorkOrderRequestBaseParams extends RequestBasePageParams  {
//    private String  workOrderCode;//工单编码
    private String  alarmType;//告警类型
    private String  processStatus;//处理状态
    private String  workOrderType;//工单类型
    private String  position;//工单编码
}
