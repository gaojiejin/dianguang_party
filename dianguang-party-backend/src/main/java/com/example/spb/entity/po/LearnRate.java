package com.example.spb.entity.po;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

/**
 * <p>
 *
 * </p>
 *
 * @author gjj
 * @since 2024-07-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LearnRate implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 学习进度id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 学习人id
     */
    private Integer userId;

    /**
     * 学习课程id
     */
    private Integer classId;

    /**
     * 学习章节id
     */
    private Integer chapterId;

    /**
     * 是否学习完成 0未完成 1完成
     */
    @TableField("ifLearned")
    private Integer ifLearned;

    /**
     * 学习时长
     */
    private Integer learningDuration;

    /**
     * 学习开始时间
     */
    private DateTime learn_date;

}
