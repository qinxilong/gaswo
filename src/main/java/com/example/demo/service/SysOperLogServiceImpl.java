package com.example.demo.service;

//import com.example.demo.system.domain.SysOperLog;
//import com.example.demo.system.mapper.SysOperLogMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.domain.GasAlarmHistory;
import com.example.demo.domain.SysOperLog;
import com.example.demo.mapper.SysOperLogMapper;
import com.example.demo.response.RequestBasePageParams;
import com.example.demo.response.SysOperLogRequesParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysOperLogServiceImpl implements ISysOperLogService
{
    @Autowired
    private SysOperLogMapper operLogMapper;

    /**
     * 新增操作日志
     * 
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysOperLog operLog)
    {
        operLogMapper.insert(operLog);
    }

    /**
     * 查询系统操作日志集合
     * 
     * @return 操作日志集合
     */
    @Override
    public IPage<SysOperLog> selectOperLogList(SysOperLogRequesParams params)
    {
        IPage<SysOperLog> ipage = new Page(params.getPage(),params.getSize());
        LambdaQueryWrapper<SysOperLog> wrapper = new LambdaQueryWrapper<>();
        if(params.getStartTime()!=null){
            wrapper.ge(SysOperLog::getOperTime, params.getStartTime());
        }
        if(params.getEndTime()!=null) {
            wrapper.le(SysOperLog::getOperTime, params.getEndTime());
        }
        if(params.getOperName()!=null && !params.getOperName().equals("")){
            wrapper.eq(SysOperLog::getOperName, params.getOperName());
        }
        wrapper.orderByDesc(SysOperLog::getOperTime);
        IPage<SysOperLog> logList = operLogMapper.selectPage(ipage, wrapper);
        return logList;
    }

    /**
     * 批量删除系统操作日志
     * 
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds)
    {
//        return operLogMapper.deleteOperLogByIds(operIds);
        return 0;
    }

    /**
     * 查询操作日志详细
     * 
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperLog selectOperLogById(Long operId)
    {
       // return operLogMapper.selectOperLogById(operId);
        return null;
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog()
    {
        //operLogMapper.cleanOperLog();
    }
}
