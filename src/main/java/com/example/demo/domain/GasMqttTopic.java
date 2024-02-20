package com.example.demo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("gas_mqtt_topic")
public class GasMqttTopic {
    private int id;
    private String  roomId;//厂区名称
    private String  mqttTopic;//mqtt主题
}
