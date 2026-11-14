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
 * 文章审核表
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ArticleReview implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 审核表id
     */
    @TableId(value = "article_id")
    private Integer article_id;

    /**
     * 审核人id
     */
    private String reviewerId;

    /**
     * 审核人
     */
    private String reviewerName;

    /**
     * 审核状态
     */
    private Integer reviewState;

    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;

    /**
     * 审核意见
     */
    private String advice;


}
