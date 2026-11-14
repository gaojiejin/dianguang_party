package com.example.spb.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author zff
 * @since 2024-04-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 工号/学号
     */
    @Getter
    private String jobId;

    /**
     * 用户密码
     */
    private String password;

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 用户手机号
     */
    @Getter
    private String phone;

    /**
     * 所属支部
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer branchId;

    /**
     * 姓名
     */
    private String name;


    /**
     * 岗位
     */
    private String post;

    /**
     * 用户头像
     */
    private String portrait;

    /**
     * 用户角色 管理权限 超级管理员 支部管理员 党员 积极分子
     */
    private String role;

    /**
     * 用户年级
     */
    private String grade;

    /**
     * 用户院系
     */
    private String department;


}
