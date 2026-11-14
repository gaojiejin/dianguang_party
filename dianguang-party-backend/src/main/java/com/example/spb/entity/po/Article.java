package com.example.spb.entity.po;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;


import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * <p>
 * 文章表
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Article implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 类型
     */
    private String type;

    /**
     * 上传时间
     */
    @TableField("uploadTime")
    private LocalDateTime uploadTime;

    /**
     * 文章名称
     */
    private String title;

    /**
     * 编辑
     */
    private String editor;

    /**
     * 编辑学/工号
     */
    private String editorId;

    /**
     * 审核人
     */
    private String reviewer;

    /**
     * 审核人学/工号
     */
    private String reviewerId;

    /**
     * 内容
     */
    private String content;

    /**
     * 图片路径
     */
    private String imagePath;


    /**
     * 文章号
     */
    @TableId(value = "article_id", type = IdType.AUTO)
    private Integer articleId;

    /**
     * 审核状态 0待审核 1审核通过 2审核不通过
     */
    private Integer reviewState;

    /**
     * 微课堂视频路径
     */
    private  String videoPath;
    /**
     * 点赞量
     */
    private Integer likes;


}
