package com.example.demo.request;

import com.baomidou.mybatisplus.annotation.TableField;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseRequestParams {

    @TableField(exist = false)
    private String  userName;//用户名

    @TableField(exist = false)
    private String  roleName;//角色
}
