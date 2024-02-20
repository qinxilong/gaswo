package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.annotation.Log;
import com.example.demo.common.enums.BusinessType;
import com.example.demo.common.utils.http.HttpUtils;
import com.example.demo.domain.DeviceInfo;
import com.example.demo.domain.GasMqttTopic;
import com.example.demo.domain.SysDept;
import com.example.demo.domain.SysUser;
import com.example.demo.mapper.GasMqttTopicMapper;
import com.example.demo.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * mqtt 主题
 */
@Service
public class GasMqttTopicService {

    @Autowired
    private GasMqttTopicMapper gasMqttTopicMapper;

    /**
     * 厂区主题
     * @return
     */
    public GasMqttTopic getTopicByRoomId(String roomId){
        SysUser sysUser = HttpUtils.getSysUser();
        try{
            LambdaQueryWrapper<GasMqttTopic> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GasMqttTopic::getRoomId,roomId);
            GasMqttTopic topic = gasMqttTopicMapper.selectOne(wrapper);
//            System.out.println(topic);
            return topic;
        }catch (Exception e){
//            System.out.println(e);
            return  null;
        }
    }

}
