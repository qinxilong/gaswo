package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.utils.StringUtils;
import com.example.demo.domain.DeviceDetail;
import com.example.demo.domain.DeviceInfo;
import com.example.demo.domain.Equipment;
import com.example.demo.domain.SysUser;
import com.example.demo.mapper.DeviceDetailMapper;
import com.example.demo.mapper.EquipmentMapper;
import com.example.demo.response.RequestDeviceDetailParams;
import com.example.demo.response.ResponseBase;
import com.example.demo.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 主机、网关设备服务
 */
@Service
public class EquipmentService {

    @Autowired
    private EquipmentMapper equipmentMapper;

//    @Autowired
//    private ISysUserService userService;

    /**
     * 更新鉴定时间
     * @param equipment
     * @return
     */
    public ResponseBase update(Equipment equipment) {
        boolean equipmentExist = selectExist(equipment);
        if(equipmentExist){
            LambdaUpdateWrapper<Equipment> wrapper = new LambdaUpdateWrapper<>();
            wrapper.set(Equipment::getStatus, equipment.getStatus())
                    .eq(Equipment::getRoomId,equipment.getRoomId())
//                    .eq(Equipment::getDeviceName,equipment.getDeviceName())
                    .eq(Equipment::getDeviceType,equipment.getDeviceType())
                    .eq(Equipment::getDeviceId,equipment.getDeviceId());
            try{
                int updateNum = equipmentMapper.update(null,wrapper);
                if(updateNum>0){
                    return  ResponseBase.ok();
                }else{
                    return  ResponseBase.fail("主机、或者网关信息更新失败");
                }
            }catch (Exception e){
//                System.out.println(e);
                return  ResponseBase.fail(e.getMessage());
            }
        }else{
            try {
                return insert(equipment);
            }catch (Exception e){
                return  ResponseBase.fail("新增主机或网关设备失败");
            }
        }
    }

    /**
     * 查询设备是否存在
     * @param equipment
     */
    public boolean selectExist(Equipment equipment) {
        LambdaQueryWrapper<Equipment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Equipment::getRoomId,equipment.getRoomId())
                .eq(Equipment::getDeviceId,equipment.getDeviceId())
//                .eq(Equipment::getDeviceName,equipment.getDeviceName())
                .eq(Equipment::getDeviceType,equipment.getDeviceType());
        try{
            Equipment eq = equipmentMapper.selectOne(wrapper);
            if(eq!=null&&eq.getId()!=null){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
//            System.out.println(e);
            return false;
        }
    }

    public ResponseBase insert(Equipment equipment) {
        int num = equipmentMapper.insert(equipment);
        if(num>0){
            return  ResponseBase.ok();
        }else{
            return  ResponseBase.fail("新增主机或网关设备失败");
        }
    }

    /**
     * 查询网关、主机设备
     * @param sysUser
     * @return
     */
    public  List<Equipment>  selectList(SysUser sysUser) {
        LambdaQueryWrapper<Equipment> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Equipment::getId);
        if(!sysUser.getRoleName().equals("admin")){//普通用户
            wrapper.eq(Equipment::getRoomId,sysUser.getDeptName());
        }
        try{
            List<Equipment> equipmentList = equipmentMapper.selectList(wrapper);
            return equipmentList;
        }catch (Exception e){
//            System.out.println(e);
            return null;
        }
    }


}
