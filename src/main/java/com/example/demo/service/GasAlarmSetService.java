package com.example.demo.service;

import com.example.demo.domain.GasAlarmSet;
import com.example.demo.mapper.GasAlarmSetMapper;
import com.example.demo.response.ResponseBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GasAlarmSetService {

    @Autowired
    private GasAlarmSetMapper gasAlarmSetMapper;

    public ResponseBase update(GasAlarmSet gasAlarmSet){
        try{
            gasAlarmSetMapper.updateById(gasAlarmSet);
            return  ResponseBase.ok();
        }catch (Exception e){
//            System.out.println(e);
            return  ResponseBase.fail(e.getMessage());
        }
    }

    public GasAlarmSet selectOne(){
        GasAlarmSet gasAlarmSet= gasAlarmSetMapper.selectById(1);
        return gasAlarmSet;
    }
}
