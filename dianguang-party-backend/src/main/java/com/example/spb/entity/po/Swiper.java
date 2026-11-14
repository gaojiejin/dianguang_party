package com.example.spb.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author gjj
 * @since 2024-09-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Swiper implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 轮播图id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 轮播图路径
     */
    private String url;


}
