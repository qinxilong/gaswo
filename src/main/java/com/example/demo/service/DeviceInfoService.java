package com.example.demo.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.utils.StringUtils;
import com.example.demo.domain.*;
import com.example.demo.domain.constant.GlobalConstants;
import com.example.demo.mapper.DeviceInfoMapper;
import com.example.demo.response.RequestBaseParams;
import com.example.demo.response.ResponseBase;
import com.example.demo.response.ResponseData;
import com.example.demo.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceInfoService {

    @Autowired
    private DeviceInfoMapper deviceInfoMapper;

    @Autowired
    private WorkOrderService workOrderService;

    @Resource
    private RedisUtil redisUtil;


    public ResponseData selectList(RequestBaseParams params,SysUser sysUser){
        Page<DeviceInfo> page = new Page<>(params.getPage(), params.getSize());  // 创建 Page 对象
        LambdaQueryWrapper<DeviceInfo> wrapper = new LambdaQueryWrapper<>();

//        if(params.getDeviceId()!=null && !params.getDeviceId().equals("")){
//            wrapper.like(DeviceInfo::getDeviceId, params.getDeviceId());
//        }
//        if(params.getPosition()!=null && !params.getPosition().equals("")){
//            wrapper.or().like(DeviceInfo::getPosition, params.getPosition());
//        }
//        if(!sysUser.getRoleName().equals("admin")){//普通用户
//            wrapper.eq(DeviceInfo::getRoomId,sysUser.getDeptName());
//        }

        //新增
        if(!sysUser.getRoleName().equals("admin")){//普通用户只能查询自己的车间
            wrapper.eq(DeviceInfo::getRoomId,sysUser.getDeptName());
        }else{//超级用户可以查询所有的车间
            if(!StringUtils.isEmpty(params.getRoomId())){
                wrapper.like(DeviceInfo::getRoomId,params.getRoomId());
            }
        }

        if(!StringUtils.isEmpty(params.getWorkShop())){
            wrapper.and(w->w.like(DeviceInfo::getWorkShop, params.getWorkShop()));
        }

        if(!StringUtils.isEmpty(params.getPosition())){
            wrapper.and(w->w.like(DeviceInfo::getPosition, params.getPosition()));
        }

        if(!StringUtils.isEmpty(params.getNumber())){
            wrapper.and(w->w.like(DeviceInfo::getNumber, params.getNumber()));
        }

        wrapper.orderByAsc(DeviceInfo::getId);
        try{
            IPage<DeviceInfo> pageResult = deviceInfoMapper.selectPage(page, wrapper);
            return (ResponseData) ResponseData.ok(pageResult);
        }catch (Exception e){
            return (ResponseData) ResponseBase.fail(e.getMessage());
        }
    }


    /**
     * 查询故障设备（故障、高报、低报）
     * @param sysUser
     * @return
     */
    public List<DeviceInfo> selectAbnormalList(SysUser sysUser){
        LambdaQueryWrapper<DeviceInfo> wrapper = new LambdaQueryWrapper<>();
//        wrapper.ne(DeviceInfo::getAlarmType,"normal");//异常的设备
        wrapper.orderByDesc(DeviceInfo::getAlarmTime);
        List<DeviceInfo> deviceInfoListFilter = new ArrayList<>();
        try{
            List<DeviceInfo> deviceInfoList = deviceInfoMapper.selectList(wrapper);
            for(DeviceInfo deviceInfo : deviceInfoList){
//                String deviceStatus = "normal";
//                System.out.println(GlobalConstants.DEVICE_STATUS_KEY + deviceInfo.getDeviceId());
//                if(redisUtil.hasKey(GlobalConstants.DEVICE_STATUS_KEY + deviceInfo.getDeviceId())){
//                    deviceStatus = (String) redisUtil.get(GlobalConstants.DEVICE_STATUS_KEY + deviceInfo.getDeviceId());
//                }else{
////                    System.out.println("不存在");
//                }
                String status = (String) redisUtil.get(GlobalConstants.DEVICE_STATUS_KEY + deviceInfo.getDeviceId());
                if(!StringUtils.isEmpty(status)&&!status.equals("normal")){
//                    deviceStatus = status;
                    deviceInfo.setAlarmType(status);
                    WorkOrder workOrder = workOrderService.selectWorkOrderLast(deviceInfo);
                    if(workOrder!=null){
                        deviceInfo.setWorkOrderStatus("已派发");
                    }else{
                        deviceInfo.setWorkOrderStatus("未派发");
                    }
                    deviceInfoListFilter.add(deviceInfo);
                }

//                if(deviceInfo.getAlarmType()==null||deviceInfo.getAlarmType().trim().equals("")){
//                    deviceInfo.setAlarmType("normal");
//                }
//                if(!deviceStatus.equals("normal")){//异常设备
//                    deviceInfo.setAlarmType(deviceStatus);
//                    WorkOrder workOrder = workOrderService.selectWorkOrderLast(deviceInfo);
//                    if(workOrder!=null&&workOrder.getProcessStatus()==0){
//                        deviceInfo.setWorkOrderStatus("已派发");
//                    }else{
//                        deviceInfo.setWorkOrderStatus("未派发");
//                    }
//                    deviceInfoListFilter.add(deviceInfo);
//
//
////                    if(workOrder==null){
////                        deviceInfo.setWorkOrderStatus("未派发");
////                    }else{
////                        if(workOrder.getProcessStatus()==0){
////                            deviceInfo.setWorkOrderStatus("已派发");
////                        }
////                    }
//                }
            }
            return deviceInfoListFilter;
        }catch (Exception e){
            return null;
        }
    }


    public void insert(DeviceInfo deviceInfo) {
        deviceInfoMapper.insert(deviceInfo);
    }

    /**
     * 查询设备是否存在
     * @param deviceInfo
     */
    public boolean selectExist(DeviceInfo deviceInfo) {
        LambdaQueryWrapper<DeviceInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DeviceInfo::getRoomId,deviceInfo.getRoomId())
                .eq(DeviceInfo::getWorkShop,deviceInfo.getWorkShop())
                .eq(DeviceInfo::getPosition,deviceInfo.getPosition());
                //.eq(DeviceInfo::getNumber,deviceInfo.getNumber());
        try{
            DeviceInfo device = deviceInfoMapper.selectOne(wrapper);
            if(device!=null&&device.getId()!=null){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
//            System.out.println(e);
            return true;
        }
    }

    public ResponseData selectListAll(SysUser sysUser) {
        LambdaQueryWrapper<DeviceInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(DeviceInfo::getId);
        if(!sysUser.getRoleName().equals("admin")){//普通用户
            wrapper.eq(DeviceInfo::getRoomId,sysUser.getDeptName());
        }
        try{
            List<DeviceInfo> deviceInfoList = deviceInfoMapper.selectList(wrapper);
            List<DeviceInfo> list = deviceInfoList.stream().map(deviceInfo -> {
                deviceInfo.setAlarmTime(deviceInfo.getReportTime());
                return deviceInfo;
            }).collect(Collectors.toList());
            return (ResponseData) ResponseData.ok(list);
        }catch (Exception e){
//            System.out.println(e);
            return (ResponseData) ResponseBase.fail(e.getMessage());
        }
    }

    public  List<DeviceInfo>  selectList(SysUser sysUser) {
        LambdaQueryWrapper<DeviceInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(DeviceInfo::getId);
        if(!sysUser.getRoleName().equals("admin")){//普通用户
            wrapper.eq(DeviceInfo::getRoomId,sysUser.getDeptName());
        }
        try{
            List<DeviceInfo> deviceInfoList = deviceInfoMapper.selectList(wrapper);
            return deviceInfoList;
        }catch (Exception e){
//            System.out.println(e);
            return null;
        }
    }

    public ResponseBase update(DeviceInfo deviceInfo) {
        LambdaUpdateWrapper<DeviceInfo> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(DeviceInfo::getAlarmTime, deviceInfo.getAlarmTime())
                .set(DeviceInfo::getAlarmType, deviceInfo.getAlarmType())
                .set(DeviceInfo::getReportTime, deviceInfo.getReportTime())
                .eq(DeviceInfo::getDeviceId,deviceInfo.getDeviceId())
                .eq(DeviceInfo::getPosition,deviceInfo.getPosition())
                .eq(DeviceInfo::getRoomId,deviceInfo.getRoomId());
        try{
            deviceInfoMapper.update(null,wrapper);
            return  ResponseBase.ok();
        }catch (Exception e){
//            System.out.println(e);
            return  ResponseBase.fail(e.getMessage());
        }
    }

    /**
     * 查询是某个用户所在的车间否存在故障设备(高报、低报)
     */
    public boolean abnormalDeviceInfoExist(GasAlarmHistory gasAlarmHistory) {
        List<String> alarmTypeList = Arrays.asList("high","low"); //告警类型
        LambdaQueryWrapper<DeviceInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DeviceInfo::getRoomId,gasAlarmHistory.getRoomId())
                .ne(DeviceInfo::getDeviceId,gasAlarmHistory.getDeviceId());
//                .lt(DeviceInfo::getAlarmTime,gasAlarmHistory.getAlarmTime())//如果当前有告警设备的告警时间小于当前告警设备的告警时间，说明已经有告警
//                .in(DeviceInfo::getAlarmType,alarmTypeList);

        try{
            List<DeviceInfo> deviceList = deviceInfoMapper.selectList(wrapper);//查询统一个车间下设备状态信息
            boolean firstAlarm = true;
            for(DeviceInfo deviceInfo : deviceList){
                if(deviceInfo.getDeviceId().equals(gasAlarmHistory.getDeviceId())){
                    continue;
                }
                if(redisUtil.hasKey(GlobalConstants.DEVICE_INFO_KEY + deviceInfo.getDeviceId())){
//                    System.out.println(redisUtil.get(GlobalConstants.DEVICE_INFO_KEY + deviceInfo.getDeviceId()));//实时设备信息);
                    String device = (String) redisUtil.get(GlobalConstants.DEVICE_INFO_KEY + deviceInfo.getDeviceId());//实时设备信息
                    DeviceInfo deviceInfoReal = JSONObject.parseObject(device,DeviceInfo.class);
//                    System.out.println(deviceInfoReal);

                    if(alarmTypeList.contains(deviceInfoReal.getAlarmType())){//high和low
                        if(gasAlarmHistory.getRoomId()==deviceInfoReal.getRoomId()){//同一个车间设备
                            Date realTime = deviceInfoReal.getAlarmTime();
                            Date alarmTime = gasAlarmHistory.getAlarmTime();
                            if(realTime!=null&&alarmTime!=null){
                                if(realTime.before(alarmTime)){//当前有设备告警时间比此告警设备告警时间早
                                    firstAlarm = false;
                                }
                            }
                        }else{
                            System.out.println("非同一个车间设备");
                        }
                    }
                }
            }
            return firstAlarm;

//            if(deviceList!=null&&deviceList.size()>0){
//                return true;
//            }else{
//                return false;
//            }
        }catch (Exception e){
//            System.out.println(e);
            return false;
        }
    }

    public List<Map> distribution() {
        List<Map>  distributionList = deviceInfoMapper.distribution();
        return distributionList;
    }
}
