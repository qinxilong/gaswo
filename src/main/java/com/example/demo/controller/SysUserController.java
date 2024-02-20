package com.example.demo.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.annotation.Log;
import com.example.demo.common.core.controller.BaseController;
import com.example.demo.common.core.page.TableDataInfo;
import com.example.demo.common.enums.BusinessType;
import com.example.demo.common.utils.StringUtils;
import com.example.demo.domain.GasMqttTopic;
import com.example.demo.domain.SysDept;
import com.example.demo.domain.SysUser;
import com.example.demo.response.ResponseData;
import com.example.demo.service.GasMqttTopicService;
import com.example.demo.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户信息
 * 
 * @author ruoyi
 */
@Api(tags = "SysUserController")
@RestController
@RequestMapping("/qy/gas")
public class SysUserController extends BaseController {
    @Autowired
    private ISysUserService userService;

    @Autowired
    private GasMqttTopicService gasMqttTopicService;


    @ApiOperation(value = "根据车间名称获取改车间下所有用户列表")
    @GetMapping("/system/user/list")
    public ResponseData listByDept(@RequestParam(required = true,name="roomId") String roomId) {
        if(StringUtils.isEmpty(roomId)){
            return (ResponseData) ResponseData.fail("roomId不能为空");
        }
        List<SysUser> sysUserList = userService.selectUserListByRoomId(roomId);
        if(sysUserList!=null){
            List<String> userNameList =sysUserList.stream().map(sysUser -> sysUser.getUserName()).collect(Collectors.toList());
            return (ResponseData) ResponseData.ok(userNameList);
        }else {
            return (ResponseData) ResponseData.ok(null);
        }
    }

    @ApiOperation(value = "获取用户的消音状态，1：警告 0：消音")
    @GetMapping("/system/user/alarmVoice/get")
    public ResponseData getSysUserAlarmVoice() {
        SysUser sysUserDetail = userService.getSysUser();
//        System.out.println(sysUserDetail);
        if(sysUserDetail==null||StringUtils.isEmpty(sysUserDetail.getUserName())){
            return (ResponseData) ResponseData.fail("用户信息不存在");
        }
        SysUser sysUser = userService.getSysUserByUserName(sysUserDetail.getUserName());
        if(sysUser!=null){
            if(sysUser.getAlarmVoice()==1){//喇叭状态开着，并且未消音，响
                return (ResponseData) ResponseData.ok(1);
            }else{
                return (ResponseData) ResponseData.ok(0);
            }
        }else {
            return (ResponseData) ResponseData.fail("用户信息不存在");
        }
    }

    @ApiOperation(value = "获取用户的消音状态硬件控制，1：告警 0：消音")
    @GetMapping("/system/user/alarmVoice/state")
    public ResponseData getSysUserAlarmVoiceState() {
        SysUser sysUserDetail = userService.getSysUser();
//        System.out.println(sysUserDetail);
        if(sysUserDetail==null||StringUtils.isEmpty(sysUserDetail.getUserName())){
            return (ResponseData) ResponseData.fail("用户信息不存在");
        }
        SysUser sysUser = userService.getSysUserByUserName(sysUserDetail.getUserName());
        if(sysUser!=null){
            if(sysUser.getHornState()==1&&sysUser.getAlarmVoice()==1){//喇叭状态开着，并且未消音，响
                return (ResponseData) ResponseData.ok(1);
            }else{
                return (ResponseData) ResponseData.ok(0);
            }
        }else {
            return (ResponseData) ResponseData.fail("用户信息不存在");
        }
    }


    @ApiOperation(value = "获取用户的喇叭开关状态，1：开 0：关")
    @GetMapping("/system/user/hornState/get")
    public ResponseData getSysUserHornState() {
        SysUser sysUserDetail = userService.getSysUser();
//        System.out.println(sysUserDetail);
        if(sysUserDetail==null||StringUtils.isEmpty(sysUserDetail.getUserName())){
            return (ResponseData) ResponseData.fail("用户信息不存在");
        }
        SysUser sysUser = userService.getSysUserByUserName(sysUserDetail.getUserName());
        if(sysUser!=null){
            if(sysUser.getHornState()==1){//喇叭状态
                return (ResponseData) ResponseData.ok(1);
            }else{
                return (ResponseData) ResponseData.ok(0);
            }
        }else {
            return (ResponseData) ResponseData.fail("用户信息不存在");
        }
    }

    @ApiOperation(value = "对用户进行消音")
    @Log(title = "用户消音", businessType = BusinessType.UPDATE)
    @PutMapping("/system/user/alarmVoice/cancel")
    public ResponseData updateSysUserAlarmVoice() {//@RequestBody SysUser sysUser
        SysUser sysUserDetail = userService.getSysUser();
        if(sysUserDetail==null||StringUtils.isEmpty(sysUserDetail.getUserName())){
            return (ResponseData) ResponseData.fail("用户信息不存在");
        }
        if(userService.cancelAlarmVoiceBySysuser(sysUserDetail)){
            return (ResponseData) ResponseData.ok("消音成功");
        }else{
            return (ResponseData) ResponseData.fail("消音失败");
        }
    }

    @ApiOperation(value = "喇叭开关状态切换")
    @PutMapping("/system/user/hornState/switch")
    public ResponseData updateSysUserHornState(int switchState) {
        SysUser sysUserDetail = userService.getSysUser();
        if(sysUserDetail==null||StringUtils.isEmpty(sysUserDetail.getUserName())){
            return (ResponseData) ResponseData.fail("用户信息不存在");
        }
        if(switchState==0||switchState==1){
            if(userService.updateSysUserHornStateBySysuser(sysUserDetail,switchState)){
                if(switchState==0){
                    return (ResponseData) ResponseData.ok("喇叭关闭");
                }else{
                    return (ResponseData) ResponseData.ok("喇叭打开");
                }
            }else{
                return (ResponseData) ResponseData.fail("喇叭开关失败");
            }
        }else{
            return (ResponseData) ResponseData.fail("喇叭开关失败，参数不对");
        }

    }

//    @ApiOperation(value = "根据车间名称获取改车间下所有用户列表")
//    @GetMapping("/system/user/list")
//    public ResponseData getSysUser() {
//        if(StringUtils.isEmpty(userName)){
//            return (ResponseData) ResponseData.fail("roomId不能为空");
//        }
//        List<SysUser> sysUserList = userService.selectUserListByRoomId(roomId);
//        if(sysUserList!=null){
//            List<String> userNameList =sysUserList.stream().map(sysUser -> sysUser.getUserName()).collect(Collectors.toList());
//            return (ResponseData) ResponseData.ok(userNameList);
//        }else {
//            return (ResponseData) ResponseData.ok(null);
//        }
//
//    }

    @ApiOperation(value ="超级用户")
    @GetMapping("/system/user/admin/list")
    public ResponseData getAdminUserList() {//@RequestBody SysUser sysUser
        userService.startAlarmVoiceByRoomId("LT02");
        List<SysUser> userList= userService.getAdminUserList();
        if(userList==null){
            return (ResponseData) ResponseData.fail("用户信息不存在");
        }else{
            return (ResponseData) ResponseData.ok(userList);
        }
    }


    @ApiOperation(value ="用户的mqtt监听主体")
    @GetMapping("/system/user/mqtt/topic")
    public ResponseData getAdminUserMqttTopic() {
        SysUser sysUserDetail = userService.getSysUser();
        System.out.println(sysUserDetail);
        if(sysUserDetail==null||StringUtils.isEmpty(sysUserDetail.getUserName())){
            return (ResponseData) ResponseData.fail("用户信息不存在");
        }
        if(StringUtils.isEmpty(sysUserDetail.getDeptName())){
            return (ResponseData) ResponseData.fail("用户所属厂区信息不存在");
        }
        GasMqttTopic topic = gasMqttTopicService.getTopicByRoomId(sysUserDetail.getDeptName());
        return (ResponseData) ResponseData.ok(topic);
     }

}
