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
 * 章节表
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Chapter implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 章节序号
     */
    @TableId(value = "chapter_id", type = IdType.AUTO)
    private Integer chapterId;

    /**
     * 所属课程序号
     */
    private Integer classId;

    /**
     * 发布人id
     */
    private Integer founderId;

    /**
     * 发布人姓名
     */
    private String founder;

    /**
     * 如果是图片 则存储图片地址
     */
    private String chapterImage;

    /**
     * 如果是视频 则存储视频路径
     */
    private String chapterVideo;

    /**
     * 主要内容
     */
    private String content;

    /**
     * 上传时间
     */
    private LocalDateTime uploadTime;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 类型（1图文2视频）
     */
    private Integer type;

    /**
     * 章节头像
     */
    private String chapterPortrait;

    /**
     * 视频时长
     */
    private Integer duration;
}
