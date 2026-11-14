package com.example.spb.entity.po;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author zff
 * @since 2024-05-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Branch implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 支部序号
     */
    @TableId(value = "branch_id", type = IdType.AUTO)
    private Integer branchId;

    /**
     * 支部描述
     */
    private String branchName;

    /**
     * 支部成立时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 支部管理员id
     */
    private Integer managerId;

    /**
     * 支部简介
     */
    private String branchDis;

    /**
     * 支部院系
     */
    private String department;

    /**
     * 支部权限
     */
    private Integer authority;
}
