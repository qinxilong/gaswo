package com.example.demo.controller;

import com.example.demo.common.utils.http.HttpUtils;
import com.example.demo.domain.*;
import com.example.demo.response.RequestBaseParams;
import com.example.demo.response.RequestDeviceDetailParams;
import com.example.demo.response.ResponseBase;
import com.example.demo.response.ResponseData;
import com.example.demo.service.DeviceDetailService;
import com.example.demo.service.DeviceInfoService;
import com.example.demo.service.EquipmentService;
import com.example.demo.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "DeviceInfoController")
@RequestMapping("/qy/gas")
@RestController
public class DeviceInfoController {
    private static final Logger log = LoggerFactory.getLogger(DeviceInfoController.class);

    @Autowired
    private DeviceInfoService deviceInfoService;

    @Autowired
    private DeviceDetailService deviceDetailService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ISysUserService userService;

    @ApiOperation(value = "获取设备信息分页查询")
    @GetMapping("/deviceInfo/list")
    public ResponseData selectList(@ModelAttribute RequestBaseParams params) {
        SysUser sysUser = userService.getSysUser();
//        System.out.println(sysUser);
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
        ResponseData deviceInfoList = deviceInfoService.selectList(params,sysUser);
        if (deviceInfoList != null) {
            return deviceInfoList;
        } else {
            return (ResponseData) ResponseData.noContent();
        }
    }

    @ApiOperation(value = "新增设备信息")
    @PostMapping("/deviceInfo/add")
    public ResponseBase add(@RequestBody DeviceInfo deviceInfo) {
        System.out.println("收到设备新增请求");
        boolean deviceExist = deviceInfoService.selectExist(deviceInfo);
        if (!deviceExist) {//设备不存在
            try {
                deviceInfoService.insert(deviceInfo);
                return ResponseBase.ok();
            } catch (Exception e) {
                log.error(String.valueOf(e));
                return ResponseBase.fail(e.getMessage());
            }
        } else {
            return ResponseBase.success();
        }
    }

    @ApiOperation(value = "获取设备信息不分页")
    @GetMapping("/deviceInfo/listAll")
    public ResponseData selectListAll() {
        SysUser sysUserDetail = userService.getSysUser();
//        System.out.println(sysUserDetail);
        if(sysUserDetail==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
        ResponseData deviceInfoList = deviceInfoService.selectListAll(sysUserDetail);
        if (deviceInfoList != null) {
            return deviceInfoList;
        } else {
            return (ResponseData) ResponseData.noContent();
        }
    }

    @ApiOperation(value = "更新设备信息")
    @PutMapping("/deviceInfo/update")
    public ResponseBase update(@RequestBody DeviceInfo deviceInfo) {
        boolean deviceExist = deviceInfoService.selectExist(deviceInfo);
        if (!deviceExist) {//设备不存在
            try {
                deviceInfoService.insert(deviceInfo);
                return  ResponseData.ok();
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
                return  ResponseData.fail("");
            }
        } else {
             return  deviceInfoService.update(deviceInfo);
        }
    }


    @ApiOperation(value = "获取故障设备信息不分页")
    @GetMapping("/deviceInfo/abnormal/list")
    public ResponseData selectAbnormalList() {
        SysUser sysUser = userService.getSysUser();
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
        if(!sysUser.getRoleName().equals("admin")){
            return (ResponseData) ResponseData.fail("用户权限不够");
        }
        List<DeviceInfo> deviceInfoList = deviceInfoService.selectAbnormalList(sysUser);
        if (deviceInfoList != null&& deviceInfoList.size()>0) {
            return (ResponseData) ResponseData.ok(deviceInfoList);
        } else {
            return (ResponseData) ResponseData.ok(deviceInfoList);
        }
    }

    @ApiOperation(value = "获取台账资源分页查询")
    @GetMapping("/deviceDetail/list")
    public ResponseData selectList(@ModelAttribute RequestDeviceDetailParams params) {
        SysUser sysUser = userService.getSysUser();
//        System.out.println(sysUser);
        if(sysUser==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
        ResponseData deviceDetailList = deviceDetailService.selectList(params,sysUser);
        if (deviceDetailList != null) {
            return deviceDetailList;
        } else {
            return (ResponseData) ResponseData.noContent();
        }
    }

    @ApiOperation(value = "更新台账资源信息")
    @PutMapping("/deviceDetail/update")
    public ResponseBase deviceDetailUpdate(@RequestBody DeviceDetail deviceDetail) {
        SysUser sysUserDetail = userService.getSysUser();
//        System.out.println(sysUserDetail);
        if(sysUserDetail==null){
            return  ResponseBase.fail("用户不存在");
        }
        boolean exist = deviceDetailService.selectExist(deviceDetail);
        if (exist) {//设备不存在
            return  deviceDetailService.update(deviceDetail);
        }else{
            return  ResponseBase.fail("台账资源不存在");
        }
    }

    @ApiOperation(value = "新增或者更新网关、主机设备信息")
    @PutMapping("/equipment/update")
    public ResponseBase equipmentUpdate(@RequestBody Equipment equipment) {
//        System.out.println("新增或者更新网关、主机设备信息");
//        System.out.println(equipment.toString());
        return  equipmentService.update(equipment);
    }

    @ApiOperation(value = "批量新增或者更新网关、主机设备信息")
    @PutMapping("/equipment/update/list")
    public ResponseBase equipmentListUpdate(@RequestBody List<Equipment> equipmentList) {
//        System.out.println(equipmentList);
        try{
            if(equipmentList!=null){
                equipmentList.stream().forEach(equipment -> {equipmentService.update(equipment);});
                return  ResponseBase.ok();
            }else{
                return  ResponseBase.fail("接口调用参数为空");
            }

        }catch (Exception e){
            log.error(e.getLocalizedMessage());
            return  ResponseBase.fail("批量新增或更新设备信息失败");
        }
    }

    @ApiOperation(value = "获取设备通信状态列表")
    @GetMapping("/device/status/list")
    public ResponseData deviceStatus() {
//        System.out.println("获取设备通信状态");
        List<DeviceStatus>  deviceStatusList =  new ArrayList<>();
        //或者网关、主机状态列表
        SysUser sysUserDetail = userService.getSysUser();
//        System.out.println(sysUserDetail);
        if(sysUserDetail==null){
            return (ResponseData) ResponseBase.fail("用户不存在");
        }
        List<Equipment> equipmentList = equipmentService.selectList(sysUserDetail);
        if(equipmentList!=null){
            List<DeviceStatus> list1 = equipmentList.stream().map(equipment ->{
                DeviceStatus deviceStatus  = new DeviceStatus();
                deviceStatus.setId(equipment.getDeviceId());
                deviceStatus.setLabel(equipment.getDeviceName());
                deviceStatus.setStatus(equipment.getStatus());
                deviceStatus.setRoomId(equipment.getRoomId());
                deviceStatus.setDeviceType(equipment.getDeviceType());
                return deviceStatus;
            }).collect(Collectors.toList());
            deviceStatusList.addAll(list1);
        }

        List<DeviceInfo> deviceInfoList = deviceInfoService.selectList(sysUserDetail);
        if(deviceInfoList!=null){
            List<DeviceStatus> list1 = deviceInfoList.stream().map(deviceInfo -> {
                DeviceStatus deviceStatus  = new DeviceStatus();
                deviceStatus.setId(deviceInfo.getDeviceId());
                deviceStatus.setLabel(deviceInfo.getNumber());
                deviceStatus.setStatus(deviceInfo.getAlarmType());
                deviceStatus.setRoomId(deviceInfo.getRoomId());
                deviceStatus.setDeviceType("sensor");
                return deviceStatus;
            }).collect(Collectors.toList());
            deviceStatusList.addAll(list1);
        }
        return (ResponseData) ResponseData.ok(deviceStatusList);
    }

}
