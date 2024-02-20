package com.example.demo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestBaseParams  extends RequestBasePageParams {

    private String deviceId;//设备id

    private String roomId;//分厂

    private String workShop;//车间

    private String number;//厂家设备编号

    private String position;//位置

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private String alarmType;

    private int isAcked =-1;//默认取-1 全部


    public RequestBaseParams(Integer page, Integer size, String sortBy, String deviceId, String position) {
        super(page, size);
        this.deviceId = deviceId;
        this.position = position;
    }

    public RequestBaseParams(Integer page, Integer size, String deviceId, String position) {
        super(page, size);
        this.deviceId = deviceId;
        this.position = position;
    }

    public RequestBaseParams(Integer page, Integer size, String deviceId, String position, Date startTime, Date endTime) {
        super(page, size);
        this.deviceId = deviceId;
        this.position = position;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public RequestBaseParams(Integer page, Integer size, String sortBy, String userAccount, Long sceneId, String deviceId, String position, Date startTime, Date endTime) {
        super(page, size);
        this.deviceId = deviceId;
        this.position = position;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
