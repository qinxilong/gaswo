package com.example.demo.controller;

import com.example.demo.domain.DeviceInfo;
import com.example.demo.domain.SysUser;
import com.example.demo.response.RequestBaseParams;
import com.example.demo.response.ResponseBase;
import com.example.demo.response.ResponseData;
import com.example.demo.service.DeviceInfoService;
import com.example.demo.service.GasAlarmHistoryService;
import com.example.demo.service.ISysUserService;
import com.example.demo.service.WorkOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "GasStatisticController")
@RequestMapping("/qy/gas/statistic")
@RestController
public class GasStatisticController {
    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private GasAlarmHistoryService gasAlarmHistoryService;
    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private ISysUserService userService;

    @ApiOperation(value = "获取车间设备分布")
    @GetMapping("/deviceInfo/distribution")//超级用户可以看
    public ResponseData deviceInfoDistribution() {
        SysUser sysUser = userService.getSysUser();
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
        if(!sysUser.getRoleName().equals("admin")){
            return (ResponseData) ResponseData.fail("用户权限不够");
        }
        List<Map> distributionList = deviceInfoService.distribution();
        if (distributionList != null) {
            return (ResponseData) ResponseData.ok(distributionList);
        } else {
            return (ResponseData) ResponseData.noContent();
        }
    }

    @ApiOperation(value = "获取告警类型的告警统计数量")
    @GetMapping("/alarm/alarmType/number")
    public ResponseData alarmTypeAlarmNumber() {
        SysUser sysUser = userService.getSysUser();
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
        List<Map> distributionList = gasAlarmHistoryService.alarmTypeNumber(sysUser);
        if (distributionList != null) {
            return (ResponseData) ResponseData.ok(distributionList);
        } else {
            return (ResponseData) ResponseData.noContent();
        }
    }

    @ApiOperation(value = "获取厂区告警的统计数量")
    @GetMapping("/alarm/roomId/number")
    public ResponseData roomIdAlarmNumber() {//超级用户
        SysUser sysUser = userService.getSysUser();
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
        if(!sysUser.getRoleName().equals("admin")){
            return (ResponseData) ResponseData.fail("用户权限不够");
        }
        List<Map> distributionList = gasAlarmHistoryService.roomIdNumber();
        if (distributionList != null) {
            return (ResponseData) ResponseData.ok(distributionList);
        } else {
            return (ResponseData) ResponseData.noContent();
        }
    }


    @ApiOperation(value = "获取位置告警的统计数量")
    @GetMapping("/alarm/position/number")
    public ResponseData positionAlarmNumber() {
        SysUser sysUser = userService.getSysUser();
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
//        if(!sysUser.getRoleName().equals("admin")){
//            return (ResponseData) ResponseData.fail("用户权限不够");
//        }
        List<Map> distributionList = gasAlarmHistoryService.positionNumber(sysUser);
        if (distributionList != null) {
            return (ResponseData) ResponseData.ok(distributionList);
        } else {
            return (ResponseData) ResponseData.noContent();
        }
    }

    @ApiOperation(value = "获取设备的的工单数量")
    @GetMapping("/workorder/position/number")
    public ResponseData positionWorkOrderNumber() {
        SysUser sysUser = userService.getSysUser();
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
//        if(!sysUser.getRoleName().equals("admin")){
//            return (ResponseData) ResponseData.fail("用户权限不够");
//        }
        List<Map> distributionList = workOrderService.positionWorkOrderNumber(sysUser);
        if (distributionList != null) {
            return (ResponseData) ResponseData.ok(distributionList);
        } else {
            return (ResponseData) ResponseData.noContent();
        }
    }

    @ApiOperation(value = "获取厂区的工单数量")
    @GetMapping("/workorder/roomId/number")
    public ResponseData roomIdWorkOrderNumber() {//超级用户
        SysUser sysUser = userService.getSysUser();
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
        if(!sysUser.getRoleName().equals("admin")){
            return (ResponseData) ResponseData.fail("用户权限不够");
        }
        List<Map> distributionList = workOrderService.roomIdWorkOrderNumber();
        if (distributionList != null) {
            return (ResponseData) ResponseData.ok(distributionList);
        } else {
            return (ResponseData) ResponseData.noContent();
        }
    }

    @ApiOperation(value = "获取告警类型工单统计数量")
    @GetMapping("/workorder/alarmType/number")
    public ResponseData alarmTypeWorkOrderNumber() {
        SysUser sysUser = userService.getSysUser();
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
//        if(!sysUser.getRoleName().equals("admin")){
//            return (ResponseData) ResponseData.fail("用户权限不够");
//        }
        List<Map> alarmTypeWorkOrderNumber = workOrderService.alarmTypeWorkOrderNumber(sysUser);
        if (alarmTypeWorkOrderNumber != null) {
            return (ResponseData) ResponseData.ok(alarmTypeWorkOrderNumber);
        } else {
            return (ResponseData) ResponseData.noContent();
        }
    }



    @ApiOperation(value = "按月对告警进行统计")
    @GetMapping("/alarm/month/number")
    public ResponseData AlarmMonthNumber() {
        SysUser sysUser = userService.getSysUser();
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
//        if(!sysUser.getRoleName().equals("admin")){
//            return (ResponseData) ResponseData.fail("用户权限不够");
//        }
        List<Map> alarmMonthNumberList = gasAlarmHistoryService.alarmMonthNumber(sysUser);
        if (alarmMonthNumberList != null) {
            return (ResponseData) ResponseData.ok(alarmMonthNumberList);
        } else {
            return (ResponseData) ResponseData.noContent();
        }
    }
    @ApiOperation(value = "按天对告警进行统计")
    @GetMapping("/alarm/day/number")
    public ResponseData AlarmDayNumber() {
        SysUser sysUser = userService.getSysUser();
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
//        if(!sysUser.getRoleName().equals("admin")){
//            return (ResponseData) ResponseData.fail("用户权限不够");
//        }

        List<Map> alarmDayNumberList = gasAlarmHistoryService.alarmDayNumber(sysUser);
        if (alarmDayNumberList != null) {
            return (ResponseData) ResponseData.ok(alarmDayNumberList);
        } else {
            return (ResponseData) ResponseData.noContent();
        }
    }

}
