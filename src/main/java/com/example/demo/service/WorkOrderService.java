package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.domain.DeviceInfo;
import com.example.demo.domain.GasAlarmHistory;
import com.example.demo.domain.SysUser;
import com.example.demo.domain.WorkOrder;
import com.example.demo.mapper.WorkOrderMapper;
import com.example.demo.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class WorkOrderService {

    @Autowired
    private WorkOrderMapper workOrderMapper;

    public ResponseData selectList(WorkOrderRequestBaseParams params, SysUser sysUser) {

        Page<WorkOrder> page = new Page<>(params.getPage(), params.getSize());  // 创建 Page 对象
        LambdaQueryWrapper<WorkOrder> wrapper = new LambdaQueryWrapper<>();

      /*  if(params.getWorkOrderCode()!=null && !params.getWorkOrderCode().equals("")){
            wrapper.like(WorkOrder::getWorkOrderCode, params.getWorkOrderCode());
        }*/
        if(params.getPosition()!=null && !params.getPosition().equals("")){
            wrapper.like(WorkOrder::getPosition, params.getPosition());
        }
        if(params.getAlarmType()!=null && !params.getAlarmType().equals("")){
            wrapper.like(WorkOrder::getAlarmType, params.getAlarmType());
        }
        if(params.getProcessStatus()!=null && !params.getProcessStatus().equals("")){
            wrapper.like(WorkOrder::getProcessStatus, params.getProcessStatus());
        }
        if(params.getWorkOrderType()!=null && !params.getWorkOrderType().equals("")){
            wrapper.like(WorkOrder::getWorkOrderType, params.getWorkOrderType());
        }

        if(!sysUser.getRoleName().equals("admin")){//普通用户只能看下发给自己的工单
            wrapper.eq(WorkOrder::getProcessPerson,sysUser.getUserName());
        }
        wrapper.orderByDesc(WorkOrder::getAlarmTime);
        try{
            IPage<WorkOrder> pageResult = workOrderMapper.selectPage(page, wrapper);
            return (ResponseData) ResponseData.ok(pageResult);
        }catch (Exception e){
            return (ResponseData) ResponseBase.fail(e.getMessage());
        }
    }

    //新增工单
    public ResponseBase insert(WorkOrder workOrder) {
        try{
            Date now = new Date();
            String workOrderCode = "GD"+ now.getTime();
            workOrder.setWorkOrderCode(workOrderCode);
            workOrderMapper.insert(workOrder);
            return  ResponseBase.ok();
        }catch (Exception e){
//            System.out.println(e);
            return  ResponseBase.fail(e.getMessage());
        }
    }

    //更新工单
    public ResponseBase update(WorkOrder workOrder) {
        try{
            workOrderMapper.updateById(workOrder);
            return  ResponseBase.ok();
        }catch (Exception e){
//            System.out.println(e);
            return  ResponseBase.fail(e.getMessage());
        }
    }

    /**
     * 更新最新工单信息
     */
    public ResponseBase updateWorkOrder(WorkOrder workOrder) {
//        Calendar c = Calendar.getInstance();
//        c.setTime(workOrder.getEndTime());
//        c.add(Calendar.HOUR_OF_DAY, -8);
//        Date newDate = c.getTime();
//        workOrder.setEndTime(newDate);
        try{
            LambdaUpdateWrapper<WorkOrder> updateWrapper = Wrappers.lambdaUpdate(WorkOrder.class)
//                    .set(WorkOrder::getStartTime, workOrder.getStartTime())
                    .set(WorkOrder::getEndTime, workOrder.getEndTime())
                    .set(WorkOrder::getOrderProcessRemark, workOrder.getOrderProcessRemark())
                    .set(WorkOrder::getPictures, workOrder.getPictures())
                    .set(WorkOrder::getProcessStatus, 1)
                    .eq(WorkOrder::getId, workOrder.getId()); // 设置更新条件
//                   .eq(WorkOrder::getWorkOrderCode, workOrder.getWorkOrderCode()); // 设置更新条件

            int updateCount = workOrderMapper.update(null, updateWrapper);
//            System.out.println("更新记录数：" + updateCount);
            if(updateCount>0){
                return  ResponseBase.ok();
            }else {
                return  ResponseBase.fail("工单提交失败");
            }
        }catch (Exception e){
//            System.out.println(e);
            return  ResponseBase.fail(e.getMessage());
        }
    }

    //删除工单
    public void delete(WorkOrder workOrder) {
        workOrderMapper.deleteById(workOrder);
    }

    public boolean selectExist(WorkOrder workOrder) {
        LambdaQueryWrapper<WorkOrder> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(WorkOrder::getWorkOrderCode,workOrder.getWorkOrderCode())
          wrapper.eq(WorkOrder::getDeviceId,workOrder.getDeviceId())
                 .eq(WorkOrder::getRoomId,workOrder.getRoomId())
                 .eq(WorkOrder::getPosition,workOrder.getPosition())
                 .eq(WorkOrder::getAlarmType,workOrder.getAlarmType())
                 .eq(WorkOrder::getProcessStatus,0);
        try{
            List<WorkOrder> workOrderList = workOrderMapper.selectList(wrapper);
            if(workOrderList!=null&&workOrderList.size()>0){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
//            System.out.println(e);
            return false;
        }
    }

    public WorkOrder selectOne(WorkOrderDetailRequesParams params) {
        LambdaQueryWrapper<WorkOrder> wrapper = new LambdaQueryWrapper<>();
        if(params.getWorkOrderCode()!=null && !params.getWorkOrderCode().equals("")){
            wrapper.eq(WorkOrder::getWorkOrderCode, params.getWorkOrderCode());
        }
        if(params.getAlarmType()!=null && !params.getAlarmType().equals("")){
            wrapper.eq(WorkOrder::getAlarmType, params.getAlarmType());
        }
        if(params.getProcessStatus()!=null && !params.getProcessStatus().equals("")){
            wrapper.eq(WorkOrder::getProcessStatus, params.getProcessStatus());
        }
        try{
            WorkOrder workOrderDetail = workOrderMapper.selectOne(wrapper);
            return workOrderDetail;
        }catch (Exception e){
            return null;
        }

    }

    /**
     * 根据设备当前的告警情况获取对应未完成的工单情况
     * @param deviceInfo
     * @return
     */
    public WorkOrder selectWorkOrderLast(DeviceInfo deviceInfo) {
        LambdaQueryWrapper<WorkOrder> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(WorkOrder::getDeviceId,deviceInfo.getDeviceId())
                .eq(WorkOrder::getRoomId,deviceInfo.getRoomId())
                .eq(WorkOrder::getPosition,deviceInfo.getPosition())
                .eq(WorkOrder::getAlarmType,deviceInfo.getAlarmType())
                .eq(WorkOrder::getProcessStatus,0)//筛选已派发（未完成）
                .orderByDesc(WorkOrder::getAlarmTime)
                .last("LIMIT 1");
        try{
//            WorkOrder workOrder = workOrderMapper.selectOne(wrapper);
//            if(workOrder!=null&&workOrder.getProcessStatus()==1){
//                workOrder = null;
//            }
            return  workOrderMapper.selectOne(wrapper);
        }catch (Exception e){
//            System.out.println(e);
            return null;
        }
    }
    public List<Map> positionWorkOrderNumber(SysUser sysUser) {
        List<Map> positionWorkOrderNumber;
        if(sysUser.getRoleName().equals("admin")){
            positionWorkOrderNumber = workOrderMapper.positionWorkOrderNumber();
        }else{
            positionWorkOrderNumber= workOrderMapper.positionWorkOrderNumberByRoomId(sysUser.getDeptName());
        }
        return  positionWorkOrderNumber;
    }

    public List<Map> roomIdWorkOrderNumber() {
        List<Map> roomIdWorkOrderNumber = workOrderMapper.roomIdWorkOrderNumber();
        return  roomIdWorkOrderNumber;
    }

    public List<Map> alarmTypeWorkOrderNumber(SysUser sysUser) {

        List<Map> alarmTypeWorkOrderNumber;
        if(sysUser.getRoleName().equals("admin")){
            alarmTypeWorkOrderNumber = workOrderMapper.alarmTypeWorkOrderNumber();
        }else {
            alarmTypeWorkOrderNumber = workOrderMapper.alarmTypeWorkOrderNumberByRoomId(sysUser.getDeptName());
        }
        return  alarmTypeWorkOrderNumber;
    }
}
