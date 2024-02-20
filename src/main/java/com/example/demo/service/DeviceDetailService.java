package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.utils.StringUtils;
import com.example.demo.domain.*;
import com.example.demo.mapper.DeviceDetailMapper;
import com.example.demo.mapper.DeviceInfoMapper;
import com.example.demo.response.RequestBaseParams;
import com.example.demo.response.RequestDeviceDetailParams;
import com.example.demo.response.ResponseBase;
import com.example.demo.response.ResponseData;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 设备台账服务
 */
@Service
public class DeviceDetailService {

    @Autowired
    private DeviceDetailMapper deviceDetailMapper;
    @Autowired
    private ISysUserService userService;


    public ResponseData selectList(RequestDeviceDetailParams params, SysUser sysUser){
        Page<DeviceDetail> page = new Page<>(params.getPage(), params.getSize());// 创建 Page 对象
        LambdaQueryWrapper<DeviceDetail> wrapper = new LambdaQueryWrapper<>();

        if(!sysUser.getRoleName().equals("admin")){//普通用户只能查询自己的车间
            wrapper.eq(DeviceDetail::getRoomId,sysUser.getDeptName());
        }else{//超级用户可以查询所有的车间
            if(!StringUtils.isEmpty(params.getRoomId())){
                wrapper.like(DeviceDetail::getRoomId,params.getRoomId());
            }
        }
        if(!StringUtils.isEmpty(params.getWorkShop())){
            wrapper.and(w->w.like(DeviceDetail::getWorkShop, params.getWorkShop()));
        }
        if(!StringUtils.isEmpty(params.getDeviceCode())){
            wrapper.and(w->w.like(DeviceDetail::getDeviceCode, params.getDeviceCode()));
        }
        if(!StringUtils.isEmpty(params.getDeviceName())){
            wrapper.and(w->w.like(DeviceDetail::getDeviceName, params.getDeviceName()));
        }
        if(!StringUtils.isEmpty(params.getDeviceType())){
            wrapper.and(w->w.like(DeviceDetail::getDeviceType, params.getDeviceType()));
        }
        if(!StringUtils.isEmpty(params.getPosition())){
            wrapper.and(w->w.like(DeviceDetail::getPosition, params.getPosition()));
        }

        wrapper.orderByAsc(DeviceDetail::getId);
        try{
            IPage<DeviceDetail> pageResult = deviceDetailMapper.selectPage(page, wrapper);
            return (ResponseData) ResponseData.ok(pageResult);
        }catch (Exception e){
            return (ResponseData) ResponseData.fail(e.getMessage());
        }
    }

    public boolean selectExist(DeviceDetail deviceDetail) {
        LambdaQueryWrapper<DeviceDetail> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DeviceDetail::getRoomId,deviceDetail.getRoomId())
                .eq(DeviceDetail::getWorkShop,deviceDetail.getWorkShop())
                .eq(DeviceDetail::getPosition,deviceDetail.getPosition())
                .eq(DeviceDetail::getDeviceCode,deviceDetail.getDeviceCode())
                .eq(DeviceDetail::getDeviceName,deviceDetail.getDeviceName());
        try{
            DeviceDetail device = deviceDetailMapper.selectOne(wrapper);
            if(device!=null&&device.getId()!=null){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
//            System.out.println(e);
            return false;
        }
    }

    /**
     * 更新鉴定时间
     * @param deviceDetail
     * @return
     */
    public ResponseBase update(DeviceDetail deviceDetail) {
        LambdaUpdateWrapper<DeviceDetail> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(DeviceDetail::getAppraisalTime, deviceDetail.getAppraisalTime())
                .eq(DeviceDetail::getRoomId,deviceDetail.getRoomId())
                .eq(DeviceDetail::getWorkShop,deviceDetail.getWorkShop())
                .eq(DeviceDetail::getPosition,deviceDetail.getPosition())
                .eq(DeviceDetail::getDeviceCode,deviceDetail.getDeviceCode())
                .eq(DeviceDetail::getDeviceName,deviceDetail.getDeviceName());
        try{
            int updateNum =deviceDetailMapper.update(null,wrapper);
            if(updateNum>0){
                return  ResponseBase.ok();
            }else{
                return  ResponseBase.fail("更新失败");
            }
        }catch (Exception e){
//            System.out.println(e);
            return  ResponseBase.fail(e.getMessage());
        }
    }
}
