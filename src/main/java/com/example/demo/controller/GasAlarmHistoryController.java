package com.example.demo.controller;

import com.example.demo.common.annotation.Log;
import com.example.demo.common.enums.BusinessType;
import com.example.demo.common.utils.StringUtils;
import com.example.demo.domain.GasAlarmHistory;
import com.example.demo.domain.SysUser;
import com.example.demo.response.RequestBaseParams;
import com.example.demo.response.ResponseBase;
import com.example.demo.response.ResponseData;
import com.example.demo.service.DeviceInfoService;
import com.example.demo.service.GasAlarmHistoryService;
import com.example.demo.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "GasAlarmHistoryController")
@RequestMapping("/qy/gas")
@RestController
public class GasAlarmHistoryController {

    @Autowired
    private GasAlarmHistoryService gasAlarmHistoryService;

    @Autowired
    private ISysUserService userService;

    @Value("${alarm-info.report-interval}")
    private int reportInterval;

    @Autowired
    private DeviceInfoService deviceInfoService;

    @ApiOperation(value = "获取告警信息分页查询")
    @GetMapping("/gasAlarmHistory/list")
    public ResponseData selectList(RequestBaseParams params) {
        SysUser sysUserDetail = userService.getSysUser();
//        System.out.println(sysUserDetail);
        if(sysUserDetail==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
        ResponseData responseData = gasAlarmHistoryService.GetGasAlarmHistoryList(params,sysUserDetail);
        return responseData;
    }

    @ApiOperation(value = "告警确认")
    @Log(title = "告警确认", businessType = BusinessType.UPDATE)
    @PostMapping("/gasAlarmHistory/update")
    public ResponseBase update(@RequestBody GasAlarmHistory gasAlarmHistory) {
        return gasAlarmHistoryService.update(gasAlarmHistory);
    }

    @ApiOperation(value = "新增告警信息")
    @PostMapping("/gasAlarmHistory/add")
    public ResponseBase add(@RequestBody GasAlarmHistory gasAlarmHistory) {
        String roomId = gasAlarmHistory.getRoomId();
//        String deviceId = gasAlarmHistory.getDeviceId();
        if(StringUtils.isEmpty(roomId)){
           return  ResponseBase.fail("告警信无车间信息roomId");
        }
        try{
//            GasAlarmHistory history = gasAlarmHistoryService.GetGasAlarmHistoryLast(gasAlarmHistory);
//            if(history==null||(history.getAlarmTime().getTime()-gasAlarmHistory.getAlarmTime().getTime())/1000>reportInterval){//时间大于一个小时
                //判断是否为首报，如果当前的车间没有告警 todo
                boolean abnormalExist = deviceInfoService.abnormalDeviceInfoExist(gasAlarmHistory);
                if(abnormalExist){//已经存在告警设备
                    gasAlarmHistory.setFirstAlarm(0);
                }else{
                    gasAlarmHistory.setFirstAlarm(1);
                }
                gasAlarmHistoryService.insert(gasAlarmHistory);
                //触发当前车间下的所有用户，进行告警
                if(userService.startAlarmVoiceByRoomId(roomId)){
//                    System.out.println("警告触发成功");
                }else{
//                    System.out.println("警告触发失败");
                }
//            }
            return  ResponseBase.ok();
        }catch (Exception e){
//            System.out.println(e);
            return  ResponseBase.fail(e.getMessage());
        }


//        System.out.println(reportInterval);
//        try{
//            GasAlarmHistory history = gasAlarmHistoryService.GetGasAlarmHistoryLast(gasAlarmHistory);
//            if(history==null||(history.getAlarmTime().getTime()-gasAlarmHistory.getAlarmTime().getTime())/1000>reportInterval){//时间大于一个小时
//                gasAlarmHistoryService.insert(gasAlarmHistory);
//            }
//            return  ResponseBase.ok();
//        }catch (Exception e){
//            System.out.println(e);
//            return  ResponseBase.fail(e.getMessage());
//        }
    }

    @ApiOperation(value = "获取某个设备同一个告警最新的时间")
    @GetMapping("/gasAlarmHistory/latest")
    public ResponseBase getLatest(GasAlarmHistory gasAlarmHistory) {
        try{
            gasAlarmHistoryService.GetGasAlarmHistoryLast(gasAlarmHistory);
            return  ResponseBase.ok();
        }catch (Exception e){
//            System.out.println(e);
            return  ResponseBase.fail(e.getMessage());
        }
    }
}
