package com.example.spb.controller;


import com.example.spb.entity.po.Swiper;
import com.example.spb.entity.po.User;
import com.example.spb.service.SwiperService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gjj
 * @since 2024-09-10
 */
@Controller
@Slf4j
@CrossOrigin
@Api("轮播图")
@RequestMapping("/swiper")
public class SwiperController {
    @Autowired
    private SwiperService swiperService;

    @PostMapping("/getAllSwiper")
    @ApiOperation("获取所有轮播图")
    public ResponseEntity<List<Swiper>> getAllSwiper(){
        return ResponseEntity.ok().body(swiperService.getAllSwiper());
    }

    @PostMapping("/addSwiper")
    @ApiOperation("添加轮播图")
    public ResponseEntity<String> addSwiper(@RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok().body(swiperService.addSwiper(file));
    }

    @PostMapping("/deleteSwiperById")
    @ApiOperation("根据id删除轮播图")
    public ResponseEntity<String> deleteSwiperById(@RequestParam("id") Integer id){
        return ResponseEntity.ok().body(swiperService.deleteSwiperById(id));
    }
}

