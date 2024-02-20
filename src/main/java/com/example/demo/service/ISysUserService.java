package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.annotation.Log;
import com.example.demo.common.enums.BusinessType;
import com.example.demo.common.utils.StringUtils;
import com.example.demo.common.utils.http.HttpUtils;
import com.example.demo.domain.SysDept;
import com.example.demo.domain.SysUser;
import com.example.demo.mapper.SysUserMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户 业务层
 * 
 * @author ruoyi
 */
@Service
public class ISysUserService{

    @Autowired
    private  SysUserMapper sysUserMapper;

    @Autowired
    private  ISysDeptService iSysDeptService;

    public List<SysUser> selectUserListByRoomId(String roomId){
        try{
            SysDept sysDept = iSysDeptService.selectOne(roomId);
            if(sysDept!=null&&sysDept.getDeptId()!=null&&sysDept.getDeptId()>0){
                LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(SysUser::getDeptId,sysDept.getDeptId());
                List<SysUser> userList = sysUserMapper.selectList(wrapper);
                return  userList;
            }else{
                return  null;
            }
        }catch (Exception e){
//            System.out.println(e);
            return  null;
        }
    }

    /**
     * 获取用户信息
     * @return
     */
    public SysUser getSysUser(){
        SysUser sysUser = HttpUtils.getSysUser();
        try{
            SysUser sysUserDetail = sysUserMapper.getSysUser(sysUser.getUserName());
            if(sysUserDetail!=null){
//                if(StringUtils.isEmpty(sysUser.getRoleName())){
//                    sysUserDetail.setRoleName(sysUserDetail.getRoleKey());
//                }else{
//                    sysUserDetail.setRoleName(sysUser.getRoleName());
//                }
                sysUserDetail.setRoleName(sysUserDetail.getRoleKey());
//                System.out.println(sysUserDetail);
                return sysUserDetail;
            }else{
                return  null;
            }
        }catch (Exception e){
//            System.out.println(e);
            return  null;
        }
    }

    /**
     * 获取用户信息
     * @return
     */
    public SysUser getSysUserDetail(){
        SysUser sysUser = HttpUtils.getSysUser();
        //todo 校验一下传入用户和角色是否正确
        try{
            SysUser sysUserDetail = sysUserMapper.getSysUser(sysUser.getUserName());
            if(sysUserDetail!=null){
                sysUserDetail.setRoleName(sysUser.getRoleName());
//                System.out.println(sysUserDetail);
                return sysUserDetail;
            }else{
                return  null;
            }
        }catch (Exception e){
//            System.out.println(e);
            return  null;
        }
    }

    /**
     * 获取用户信息
     * @return
     */
    public SysUser getSysUserByUserName(String userName){
        SysUser sysUser = HttpUtils.getSysUser();
        try{
            SysUser sysUserDetail = sysUserMapper.getSysUser(userName);
            sysUserDetail.setRoleName(sysUser.getRoleName());
//            System.out.println(sysUserDetail);
            return sysUserDetail;
        }catch (Exception e){
//            System.out.println(e);
            return  null;
        }
    }

    /**
     * 触发车间下所有用户（以及超级用户）的进行告警（告警喇叭和告警灯开启）
     * @param roomId
     * @return
     */
//    @Log(title = "触发用户进行警告", businessType = BusinessType.UPDATE)
    public boolean startAlarmVoiceByRoomId(String roomId){
        try{
            List<SysUser> userList= getAdminUserList();
            List<String> userNameList = userList.stream().map(sysUser -> sysUser.getUserName()).collect(Collectors.toList());
            LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper();
            updateWrapper.set(SysUser::getAlarmVoice, 1)
//                    .eq(SysUser::getDeptId, sysDept.getDeptId())
                    .in(SysUser::getUserName,userNameList);

            SysDept sysDept = iSysDeptService.selectOne(roomId);//获取车间对应的车间id(deptId)
            if(sysDept!=null&&sysDept.getDeptId()>0){
                updateWrapper.or().eq(SysUser::getDeptId, sysDept.getDeptId());
            }

            int updateCount = sysUserMapper.update(null, updateWrapper);
            if(updateCount>0){
                return true;
            }else{
                return false;
            }
//            if(sysDept!=null&&sysDept.getDeptId()>0){
//                LambdaUpdateWrapper<SysUser> updateWrapper = Wrappers.lambdaUpdate(SysUser.class)
//                        .set(SysUser::getAlarmVoice, 1)
//                        .eq(SysUser::getDeptId, sysDept.getDeptId())
//                        .or().in(SysUser::getUserName,userNameList);
//                int updateCount = sysUserMapper.update(null, updateWrapper);
//                if(updateCount>0){
//                    return true;
//                }else{
//                    return false;
//                }
//            }else{
//                return false;
//            }
        }catch (Exception e){
//            System.out.println(e);
            return false;
        }
    }

    @Log(title = "用户消音", businessType = BusinessType.UPDATE)
    public boolean cancelAlarmVoiceBySysuser(SysUser sysUserDetail){
        String userName = sysUserDetail.getUserName();
        try{
            LambdaUpdateWrapper<SysUser> updateWrapper = Wrappers.lambdaUpdate(SysUser.class)
                    .set(SysUser::getAlarmVoice, 0)
                    .eq(SysUser::getUserName, userName);
            int updateCount = sysUserMapper.update(null, updateWrapper);
            if(updateCount>0){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
//            System.out.println(e);
            return false;
        }
    }

    /**
     * 获取超级用户列表
     * @return
     */
    public List<SysUser> getAdminUserList(){
        try{
            try{
                List<SysUser> userList = sysUserMapper.getAdminSysUser();
                if(userList!=null&&userList.size()>0){
                    return userList;
                }else{
                    return  null;
                }
            }catch (Exception e){
//                System.out.println(e);
                return  null;
            }
        }catch (Exception e){
//            System.out.println(e);
            return  null;
        }
    }
//    @Log(title = "喇叭开关", businessType = BusinessType.UPDATE)
    public boolean updateSysUserHornStateBySysuser(SysUser sysUserDetail,int switchState) {
        String userName = sysUserDetail.getUserName();
        try{
            LambdaUpdateWrapper<SysUser> updateWrapper = Wrappers.lambdaUpdate(SysUser.class)
                    .set(SysUser::getHornState, switchState)
                    .eq(SysUser::getUserName, userName);
            int updateCount = sysUserMapper.update(null, updateWrapper);
            if(updateCount>0){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
//            System.out.println(e);
            return false;
        }
    }

}
