package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.domain.DeviceInfo;
import com.example.demo.domain.SysUser;
import com.example.demo.domain.Weather;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeviceInfoMapper extends BaseMapper<DeviceInfo> {
    @Select("SELECT room_id,COUNT(*) as num  FROM `gas_device_info` GROUP BY room_id")
    public  List<Map> distribution();

}
