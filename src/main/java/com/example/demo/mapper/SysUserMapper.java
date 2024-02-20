package com.example.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo.domain.DeviceInfo;
import com.example.demo.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
//    @Select("SELECT user.* ,dept.dept_name FROM sys_user user, sys_dept  dept where user.dept_id =dept.dept_id\n" +
//            "and user.user_name=#{userName}")
@Select("SELECT user.*, role.role_id, role.role_name,role.role_key,dept.dept_name from sys_user_role user_role,sys_role role,sys_user user,sys_dept dept\n" +
        "where role.role_id= user_role.role_id\n" +
        "and user.user_id = user_role.user_id\n" +
        "and user.dept_id =dept.dept_id\n" +
        "and user.user_name=#{userName}")
    public SysUser getSysUser(String userName);
   // "SELECT user.* ,dept.dept_name,role.role_name,role.role_key FROM sys_user user, sys_dept  dept, sys_user_role urole, sys_role role where user.dept_id =dept.dept_idand user.user_id=urole.user_id and urole.role_id=role.role_id and user.user_name='qinxiyun'"
    //"SELECT user.*, role.role_id, role.role_name,role.role_key from sys_user_role user_role,sys_role role,sys_user user
    //where role.role_id= user_role.role_id
    //and user.user_id = user_role.user_id"




    @Select("SELECT user.*, role.role_id,role.role_name FROM `sys_user` user, sys_user_role user_role,sys_role role  where user_role.user_id= user.user_id and user_role.role_id =  role.role_id and role.role_id=1")
    public List<SysUser> getAdminSysUser();
}
