package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.domain.GasAlarmHistory;
import com.example.demo.domain.Weather;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface GasAlarmHistoryMapper extends BaseMapper<GasAlarmHistory> {
    @Select("SELECT alarm_type ,count(*) as num  from `gas_alarm_history` GROUP BY alarm_type")
    List<Map> getAlarmTypeNumber();

    @Select("SELECT alarm_type ,count(*) as num  from `gas_alarm_history` where room_id= #{deptName} GROUP BY alarm_type")
    List<Map> getAlarmTypeNumberByRoomId(String deptName);

    @Select("SELECT room_id ,count(*) as num  from `gas_alarm_history` GROUP BY room_id ORDER BY num desc")
    List<Map> roomIdNumber();
    @Select("SELECT position ,count(*) as num  from `gas_alarm_history` GROUP BY position ORDER BY num desc")
    List<Map> positionNumber();

    @Select("SELECT position ,count(*) as num  from `gas_alarm_history` where room_id= #{deptName} GROUP BY position ORDER BY num desc")
    List<Map> positionNumberByRoomId(String deptName);
//    @Select("select date_format(alarm_time, '%Y-%m') alarm_time, count(*) num\n" +
//            "from gas_alarm_history \n" +
//            "group by date_format(alarm_time, '%Y-%m') order by alarm_time")
    @Select("SELECT \n" +
            "    dates.month AS alarm_time,\n" +
            "    IFNULL(alarm_counts.alarm_count, 0) AS num\n" +
            "FROM (\n" +
            "    SELECT \n" +
            "        DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL (1 - s.a) MONTH), '%Y-%m') AS month\n" +
            "    FROM\n" +
            "        (SELECT 1 AS a UNION SELECT 2 UNION SELECT 3 UNION SELECT 4\n" +
            "         UNION SELECT 5 UNION SELECT 6) AS s\n" +
            "    ) AS dates\n" +
            "LEFT JOIN (\n" +
            "    SELECT \n" +
            "        DATE_FORMAT(alarm_time, '%Y-%m') AS month,\n" +
            "        COUNT(*) AS alarm_count\n" +
            "    FROM \n" +
            "        gas_alarm_history\n" +
            "    WHERE \n" +
            "        alarm_time >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)\n" +
            "    GROUP BY \n" +
            "        DATE_FORMAT(alarm_time, '%Y-%m')\n" +
            "    ) AS alarm_counts ON dates.month = alarm_counts.month\n" +
            "ORDER BY \n" +
            "    dates.month;")
    List<Map> alarmMonthNumber();//按月统计

//    @Select("select date_format(alarm_time, '%Y-%m') alarm_time, count(*) num\n" +
//            "from gas_alarm_history \n" +
//            "where room_id= #{deptName} group by date_format(alarm_time, '%Y-%m') order by alarm_time")
    @Select("SELECT \n" +
            "    dates.month AS alarm_time,\n" +
            "    IFNULL(alarm_counts.alarm_count, 0) AS num\n" +
            "FROM (\n" +
            "    SELECT \n" +
            "        DATE_FORMAT(DATE_ADD(CURDATE(), INTERVAL (1 - s.a) MONTH), '%Y-%m') AS month\n" +
            "    FROM\n" +
            "        (SELECT 1 AS a UNION SELECT 2 UNION SELECT 3 UNION SELECT 4\n" +
            "         UNION SELECT 5 UNION SELECT 6) AS s\n" +
            "    ) AS dates\n" +
            "LEFT JOIN (\n" +
            "    SELECT \n" +
            "        DATE_FORMAT(alarm_time, '%Y-%m') AS month,\n" +
            "        COUNT(*) AS alarm_count\n" +
            "    FROM \n" +
            "        gas_alarm_history\n" +
            "    WHERE room_id= #{deptName} and " +
            "        alarm_time >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)\n" +
            "    GROUP BY \n" +
            "        DATE_FORMAT(alarm_time, '%Y-%m')\n" +
            "    ) AS alarm_counts ON dates.month = alarm_counts.month\n" +
            "ORDER BY \n" +
            "    dates.month;")
    List<Map> alarmMonthNumberByRoomId(String deptName);

//    @Select("select date_format(alarm_time, '%Y-%m-%d') alarm_time, count(*) num\n" +
//            "from gas_alarm_history\n" +
//            "group by date_format(alarm_time, '%Y-%m-%d') order by alarm_time;")
//    @Select("select date_format(alarm_time, '%Y-%m-%d') alarm_time, count(*) num from gas_alarm_history where DATE_SUB(CURDATE(), INTERVAL 7 DAY) <=  date(alarm_time) group by date_format(alarm_time, '%Y-%m-%d') order by alarm_time")
    @Select("SELECT \n" +
            "    date_table.date AS alarm_time,\n" +
            "    IFNULL(alert_data.alert_count, 0) AS num\n" +
            "FROM \n" +
            "    (\n" +
            "        SELECT CURDATE() - INTERVAL 6 DAY AS date UNION ALL\n" +
            "        SELECT CURDATE() - INTERVAL 5 DAY AS date UNION ALL\n" +
            "        SELECT CURDATE() - INTERVAL 4 DAY AS date UNION ALL\n" +
            "        SELECT CURDATE() - INTERVAL 3 DAY AS date UNION ALL\n" +
            "        SELECT CURDATE() - INTERVAL 2 DAY AS date UNION ALL\n" +
            "        SELECT CURDATE() - INTERVAL 1 DAY AS date UNION ALL\n" +
            "        SELECT CURDATE() AS date\n" +
            "    ) AS date_table\n" +
            "LEFT JOIN \n" +
            "    (\n" +
            "        SELECT \n" +
            "            DATE(alarm_time) AS date,\n" +
            "            COUNT(*) AS alert_count\n" +
            "        FROM \n" +
            "            gas_alarm_history\n" +
            "        WHERE \n" +
            "            alarm_time >= CURDATE() - INTERVAL 6 DAY\n" +
            "        GROUP BY \n" +
            "            DATE(alarm_time)\n" +
            "    ) AS alert_data\n" +
            "ON date_table.date = alert_data.date\n" +
            "ORDER BY \n" +
            "    date_table.date ASC;")
    List<Map> alarmDayNumber();

//    @Select("select date_format(alarm_time, '%Y-%m-%d') alarm_time, count(*) num from gas_alarm_history  where room_id= #{deptName} and DATE_SUB(CURDATE(), INTERVAL 7 DAY) <=  date(alarm_time) group by date_format(alarm_time, '%Y-%m-%d') order by alarm_time")
   @Select("SELECT \n" +
           "    date_table.date AS alarm_time,\n" +
           "    IFNULL(alert_data.alert_count, 0) AS num\n" +
           "FROM \n" +
           "    (\n" +
           "        SELECT CURDATE() - INTERVAL 6 DAY AS date UNION ALL\n" +
           "        SELECT CURDATE() - INTERVAL 5 DAY AS date UNION ALL\n" +
           "        SELECT CURDATE() - INTERVAL 4 DAY AS date UNION ALL\n" +
           "        SELECT CURDATE() - INTERVAL 3 DAY AS date UNION ALL\n" +
           "        SELECT CURDATE() - INTERVAL 2 DAY AS date UNION ALL\n" +
           "        SELECT CURDATE() - INTERVAL 1 DAY AS date UNION ALL\n" +
           "        SELECT CURDATE() AS date\n" +
           "    ) AS date_table\n" +
           "LEFT JOIN \n" +
           "    (\n" +
           "        SELECT \n" +
           "            DATE(alarm_time) AS date,\n" +
           "            COUNT(*) AS alert_count\n" +
           "        FROM \n" +
           "            gas_alarm_history\n" +
           "        WHERE \n" +
           "            room_id= #{deptName} and  alarm_time >= CURDATE() - INTERVAL 6 DAY\n" +
           "        GROUP BY \n" +
           "            DATE(alarm_time)\n" +
           "    ) AS alert_data\n" +
           "ON date_table.date = alert_data.date\n" +
           "ORDER BY \n" +
           "    date_table.date ASC;")
    List<Map> alarmDayNumberByRoomId(String deptName);
}
