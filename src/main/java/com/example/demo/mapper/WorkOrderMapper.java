package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.domain.WorkOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface WorkOrderMapper extends BaseMapper<WorkOrder> {
    @Select("SELECT position,count(*) as num  from `gas_work_order` GROUP BY position ORDER BY num desc")
    List<Map> positionWorkOrderNumber();
    @Select("SELECT position,count(*) as num  from `gas_work_order`  where room_id= #{deptName} GROUP BY position ORDER BY num desc")
    List<Map> positionWorkOrderNumberByRoomId(String deptName);

    @Select("SELECT room_id ,count(*) as num  from `gas_work_order` GROUP BY room_id ORDER BY num desc")
    List<Map> roomIdWorkOrderNumber();

    @Select("SELECT alarm_type,count(*) as num  from `gas_work_order` GROUP BY alarm_type ORDER BY num desc")
    List<Map> alarmTypeWorkOrderNumber();

//    @Select("SELECT position,count(*) as num  from `gas_work_order` where room_id= #{deptName} GROUP BY position ORDER BY num desc")
    @Select("SELECT alarm_type,count(*) as num  from `gas_work_order` where room_id= #{deptName} GROUP BY alarm_type ORDER BY num desc")
    List<Map> alarmTypeWorkOrderNumberByRoomId(String deptName);
}
