package com.example.demo.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.demo.common.core.controller.BaseController;
import com.example.demo.common.utils.StringUtils;
import com.example.demo.domain.SysOperLog;
import com.example.demo.domain.SysUser;
import com.example.demo.response.RequestBasePageParams;
import com.example.demo.response.ResponseData;
import com.example.demo.response.SysOperLogRequesParams;
import com.example.demo.service.ISysOperLogService;
import com.example.demo.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志
 * 
 * @author ruoyi
 */
@Api(tags = "SysOperLogController")
@RestController
@RequestMapping("/qy/gas")
public class SysOperLogController extends BaseController {

    @Autowired
    private ISysOperLogService iSysOperLogService;


    @Autowired
    private ISysUserService userService;

    //获取操作日志
    @ApiOperation(value = "获取操作日志分页")
    @GetMapping("/system/operlog/list")
    public ResponseData selectList(@Valid SysOperLogRequesParams params){
        SysUser sysUserDetail = userService.getSysUser();
//        System.out.println(sysUserDetail);
        if(sysUserDetail==null){
            return (ResponseData) ResponseData.fail("用户不存在");
        }
//        if(!sysUserDetail.getRoleName().equals("admin")){
//            return (ResponseData) ResponseData.fail("用户权限不够");
//        }
        if(!sysUserDetail.getRoleName().equals("admin")){
            params.setOperName(sysUserDetail.getUserName());//普通用户自动查询自己的日志
        }
        IPage<SysOperLog> logList = iSysOperLogService.selectOperLogList(params);
        return (ResponseData) ResponseData.ok(logList);
    }

}
