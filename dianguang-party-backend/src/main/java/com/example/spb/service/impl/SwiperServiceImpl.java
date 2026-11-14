package com.example.spb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.spb.entity.po.Swiper;
import com.example.spb.mapper.SwiperMapper;
import com.example.spb.service.SwiperService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spb.utils.AliOSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gjj
 * @since 2024-09-10
 */
@Service
public class SwiperServiceImpl extends ServiceImpl<SwiperMapper, Swiper> implements SwiperService {
    @Autowired
    private SwiperMapper swiperMapper;

    @Autowired
    private AliOSSUtils aliOSSUtils;
    @Override
    public List<Swiper> getAllSwiper(){
        QueryWrapper<Swiper> queryWrapper = new QueryWrapper<>();
        return swiperMapper.selectList(queryWrapper);
    }

    @Override
    public String addSwiper(MultipartFile file) throws IOException {
        if (file != null) {
            Swiper swiper = new Swiper();
            String url = aliOSSUtils.upload(file);
            swiper.setUrl(url);
            swiperMapper.insert(swiper);
            return "上传成功";
        }
        return "上传失败";
    }

    @Override
    public String deleteSwiperById(Integer id) {
        QueryWrapper<Swiper> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        Swiper swiper = swiperMapper.selectOne(queryWrapper);
        if (swiper.getUrl() != null) {
            aliOSSUtils.deleteFile(swiper.getUrl());
        }
        swiperMapper.delete(queryWrapper);
        return "删除成功";
    }

}
