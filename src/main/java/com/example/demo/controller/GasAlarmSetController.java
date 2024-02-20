package com.example.demo.controller;

import com.example.demo.domain.GasAlarmSet;
import com.example.demo.domain.SysUser;
import com.example.demo.domain.Weather;
import com.example.demo.response.ResponseBase;
import com.example.demo.response.ResponseData;
import com.example.demo.service.GasAlarmSetService;
import com.example.demo.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(tags = "GasAlarmSetController")
@RequestMapping("/qy/gas")
@RestController
public class GasAlarmSetController {

    @Autowired
    private GasAlarmSetService gasAlarmSetService;

    @Autowired
    private ISysUserService userService;

    /**
     *  获取告警设置信息
     * @return
     */
    @ApiOperation(value = "获取告警设置")
    @GetMapping("/gasAlarmSet/get")
    public ResponseData selectOne() {
        GasAlarmSet gasAlarmSet = gasAlarmSetService.selectOne();
        if(gasAlarmSet!=null){
           return (ResponseData) ResponseData.ok(gasAlarmSet);
        }else{
            return (ResponseData) ResponseData.noContent();
        }
    }

    /**
     *    超级用户才有设置阈值的权限
     */
    @ApiOperation(value = "告警设置")
    @PostMapping("/gasAlarmSet/update")
    public ResponseBase update(@RequestBody GasAlarmSet gasAlarmSet) {
        SysUser sysUserDetail = userService.getSysUser();
       // System.out.println(sysUserDetail);
        if(sysUserDetail!=null&&!sysUserDetail.getRoleName().equals("")
                &&sysUserDetail.getRoleName().equals("admin")) {
            return gasAlarmSetService.update(gasAlarmSet);
        }else{
            return ResponseData.fail("用户不存在或者权限不够");
        }
    }

}
