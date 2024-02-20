package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.annotation.Log;
import com.example.demo.common.enums.BusinessType;
import com.example.demo.common.utils.StringUtils;
import com.example.demo.domain.DeviceInfo;
import com.example.demo.domain.GasAlarmHistory;
import com.example.demo.domain.SysUser;
import com.example.demo.mapper.GasAlarmHistoryMapper;
import com.example.demo.response.RequestBasePageParams;
import com.example.demo.response.RequestBaseParams;
import com.example.demo.response.ResponseBase;
import com.example.demo.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GasAlarmHistoryService {

    @Autowired
    private GasAlarmHistoryMapper gasAlarmHistoryMapper;

//    @Log(title = "新增告警", businessType = BusinessType.INSERT)
    public void insert(GasAlarmHistory gasAlarmHistory) {
        gasAlarmHistoryMapper.insert(gasAlarmHistory);
    }

    public ResponseBase update(GasAlarmHistory gasAlarmHistory) {
        try{
//            LambdaQueryWrapper<GasAlarmHistory> updateWrapper = new LambdaQueryWrapper<>();
//            updateWrapper.eq(GasAlarmHistory::)
            gasAlarmHistoryMapper.updateById(gasAlarmHistory);
            return  ResponseBase.ok();
        }catch (Exception e){
//            System.out.println(e);
            return  ResponseBase.fail(e.getMessage());
        }
    }

    public ResponseData GetGasAlarmHistoryList(RequestBaseParams params, SysUser sysUser) {

        Page<GasAlarmHistory> page = new Page<>(params.getPage(), params.getSize());  // 创建 Page 对象
        LambdaQueryWrapper<GasAlarmHistory> wrapper = new LambdaQueryWrapper<>();

        if(params.getIsAcked()==0||params.getIsAcked()==1){
            wrapper.eq(GasAlarmHistory::getIsAcked, params.getIsAcked());
        }

        if(params.getStartTime()!=null){
            wrapper.ge(GasAlarmHistory::getAlarmTime, params.getStartTime());
        }
        if(params.getEndTime()!=null){
            wrapper.le(GasAlarmHistory::getAlarmTime, params.getEndTime());
        }

//        if(params.getDeviceId()!=null && !params.getDeviceId().equals("")){
//            wrapper.like(GasAlarmHistory::getDeviceId, params.getDeviceId());
//        }
//        if(params.getPosition()!=null && !params.getPosition().equals("")){
//            wrapper.like(GasAlarmHistory::getPosition, params.getPosition());
//        }
//
//        if(!sysUser.getRoleName().equals("admin")){//普通用户
//            wrapper.eq(GasAlarmHistory::getRoomId,sysUser.getDeptName());
//        }
        //新增
        if(!sysUser.getRoleName().equals("admin")){//普通用户只能查询自己的车间
            wrapper.eq(GasAlarmHistory::getRoomId,sysUser.getDeptName());
        }else{//超级用户可以查询所有的车间
            if(!StringUtils.isEmpty(params.getRoomId())){
                wrapper.like(GasAlarmHistory::getRoomId,params.getRoomId());
            }
        }

        if(!StringUtils.isEmpty(params.getWorkShop())){
            wrapper.and(w->w.like(GasAlarmHistory::getWorkShop, params.getWorkShop()));
        }

        if(!StringUtils.isEmpty(params.getPosition())){
            wrapper.and(w->w.like(GasAlarmHistory::getPosition, params.getPosition()));
        }

        if(!StringUtils.isEmpty(params.getNumber())){
            wrapper.and(w->w.like(GasAlarmHistory::getNumber, params.getNumber()));
        }

        String alarmType = params.getAlarmType();
        if(alarmType!=null&&!alarmType.equals("")){
            if(alarmType.contains(",")){
                String[] alarmTypes = alarmType.split(",");
                if(alarmTypes.length>0){
                    wrapper.in(GasAlarmHistory::getAlarmType, alarmTypes);
                }
            }else{
                wrapper.in(GasAlarmHistory::getAlarmType, alarmType);
            }
        }

        wrapper.orderByDesc(GasAlarmHistory::getId);
        try{
            IPage<GasAlarmHistory> pageResult = gasAlarmHistoryMapper.selectPage(page, wrapper);
            return (ResponseData) ResponseData.ok(pageResult);
        }catch (Exception e){
//            System.out.println(e);
            return (ResponseData) ResponseBase.fail(e.getMessage());
        }
    }

    /**
     * 获取设备同一个类型的告警最近的一次告警时间
     * @return
     */
    public GasAlarmHistory GetGasAlarmHistoryLast(GasAlarmHistory gasAlarmHistory) {
        LambdaQueryWrapper<GasAlarmHistory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GasAlarmHistory::getDeviceId,gasAlarmHistory.getDeviceId())
                .eq(GasAlarmHistory::getPosition,gasAlarmHistory.getPosition())
                .eq(GasAlarmHistory::getRoomId,gasAlarmHistory.getRoomId())
                .eq(GasAlarmHistory::getAlarmType,gasAlarmHistory.getAlarmType())
                .orderByDesc(GasAlarmHistory::getAlarmTime)
                .last("LIMIT 1");;

        try{
            GasAlarmHistory gasAlarmHistoryLast = gasAlarmHistoryMapper.selectOne(wrapper);
            return  gasAlarmHistoryLast;
        }catch (Exception e){
//            System.out.println(e);
            return  null;
        }

    }
    public List<Map> alarmTypeNumber(SysUser sysUser) {
        List<Map> alarmTypeNumber;
        if(sysUser.getRoleName().equals("admin")){
            alarmTypeNumber = gasAlarmHistoryMapper.getAlarmTypeNumber();
        }else{
            alarmTypeNumber = gasAlarmHistoryMapper.getAlarmTypeNumberByRoomId(sysUser.getDeptName());
        }
        return  alarmTypeNumber;
    }

    public List<Map> roomIdNumber() {
        List<Map> roomIdNumber = gasAlarmHistoryMapper.roomIdNumber();
        return  roomIdNumber;
    }

    public List<Map> positionNumber(SysUser sysUser) {
        List<Map> positionNumber;
        if(sysUser.getRoleName().equals("admin")){
             positionNumber = gasAlarmHistoryMapper.positionNumber();
        }else{
             positionNumber = gasAlarmHistoryMapper.positionNumberByRoomId(sysUser.getDeptName());
        }
        return  positionNumber;
    }


    public List<Map> alarmMonthNumber(SysUser sysUser) {
        List<Map> alarmMonthNumber;
        if(sysUser.getRoleName().equals("admin")){
            alarmMonthNumber = gasAlarmHistoryMapper.alarmMonthNumber();
        }else{
            alarmMonthNumber = gasAlarmHistoryMapper.alarmMonthNumberByRoomId(sysUser.getDeptName());
        }
        return  alarmMonthNumber;
    }

    public List<Map> alarmDayNumber(SysUser sysUser) {
        List<Map> alarmDayNumber;
        if(sysUser.getRoleName().equals("admin")){
            alarmDayNumber = gasAlarmHistoryMapper.alarmDayNumber();
        }else{
            alarmDayNumber = gasAlarmHistoryMapper.alarmDayNumberByRoomId(sysUser.getDeptName());
        }
        return  alarmDayNumber;
    }
}
