package com.example.spb.service;

import com.example.spb.entity.po.Swiper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gjj
 * @since 2024-09-10
 */
public interface SwiperService extends IService<Swiper> {
    public List<Swiper> getAllSwiper();

    public String deleteSwiperById(Integer id);

    public String addSwiper(MultipartFile file) throws IOException;
}
