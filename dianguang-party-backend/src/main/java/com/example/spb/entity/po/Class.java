package com.example.spb.entity.po;

import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 课程表
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Class implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 课程id
     */
    @TableId(value = "class_id", type = IdType.AUTO)
    private Integer classId;

    /**
     * 创建人
     */
    private String founder;

    /**
     * 创建人学/工号
     */
    private Integer founderId;

    /**
     * 创建时间
     */
    private LocalDateTime uploadTime;

    /**
     * 内容路径
     */
    private String contentPath;

    /**
     * 课程标题
     */
    private String classTitle;

    /**
     * 课程头像
     */
    private String classPortrait;

    /**
     * 课程权限
     */
    private Integer authority;

    /**
     * 课程所属院系
     */
    private String department;


}
