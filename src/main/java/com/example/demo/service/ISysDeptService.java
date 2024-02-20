package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.domain.DeviceInfo;
import com.example.demo.domain.SysDept;
import com.example.demo.domain.SysUser;
import com.example.demo.mapper.SysDeptMapper;
import com.example.demo.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户 业务层
 * 
 * @author ruoyi
 */
@Service
public class ISysDeptService {

    @Autowired
    private SysDeptMapper sysDeptMapper;

    public SysDept selectOne (String roomId){
        LambdaQueryWrapper<SysDept> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDept::getDeptName,roomId);
        try{
            SysDept sysDept= sysDeptMapper.selectOne(wrapper);
            return sysDept;
        }catch (Exception e){
//            System.out.println(e);
            return null;
        }
    }


}
