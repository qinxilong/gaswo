package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.domain.DeviceInfo;
import com.example.demo.domain.GasMqttTopic;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GasMqttTopicMapper extends BaseMapper<GasMqttTopic> {

}
