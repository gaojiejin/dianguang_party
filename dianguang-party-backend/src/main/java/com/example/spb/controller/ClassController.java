package com.example.spb.controller;


import com.example.spb.entity.po.Class;
import com.example.spb.entity.vo.ClassAndChapter;
import com.example.spb.service.ClassService;
import com.example.spb.utils.AliOSSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 课程表 前端控制器
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
@Controller
@Slf4j
@CrossOrigin
@Api("课程管理类")
@RequestMapping("/class")
public class ClassController {
    @Autowired
    private ClassService classService;

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @PostMapping("/addClass")
    @ApiOperation("添加课程")
    public ResponseEntity<String> addClass(@RequestParam("class_portrait") MultipartFile class_portrait, @RequestParam("founder_id") int founder_id, @RequestParam("founder") String founder, @RequestParam("class_title") String class_title, @RequestParam("department") String department, @RequestParam("authority") int authority) throws IOException {
        int class_id = classService.addClass(founder_id, founder, class_title, department,authority, class_portrait);
        if (class_id!=-1){
//            String res = classService.createClassLearnInfo(class_id,authority);
//            if (res.equals("课程学习信息添加成功")){
                return ResponseEntity.ok("成功创建课程");
//            }
//            return ResponseEntity.ok("课程学习信息添加失败！");
        }
        return ResponseEntity.ok("课程名重复！");
    }

    @PostMapping("/getClasses")
    @ApiOperation("获取全部课程")
    public  ResponseEntity<List<Class>> getClasses() throws IOException {
        return ResponseEntity.ok(classService.getClasses());
    }

    @PostMapping("/getClassesAndChapter")
    @ApiOperation("获取全部课程和章节")
    public  ResponseEntity<List<ClassAndChapter>> getClassesAndChapter() throws IOException {
        return ResponseEntity.ok(classService.getClassesAndChapter());
    }

    @PostMapping("/getClassById")
    @ApiOperation("获取单个课程")
    public  ResponseEntity<Class> getClassById(@RequestParam("class_id") int class_id) throws IOException {
        return ResponseEntity.ok(classService.getClassById(class_id));
    }

    @PostMapping("/deleteClass")
    @ApiOperation("删除课程")
    public  ResponseEntity<String> deleteClass(@RequestParam("class_id") int class_id) throws IOException {
        return ResponseEntity.ok(classService.deleteClass(class_id));
    }


    @PostMapping("/upload")
    @ApiOperation("上传文件")
    public ResponseEntity<String> addFile(@RequestParam("url")  MultipartFile url) throws IOException {
        return ResponseEntity.ok(aliOSSUtils.upload(url));
    }
    @PostMapping("/delete")
    @ApiOperation("删除文件")
    public ResponseEntity<String> deleteFile(@RequestParam("url") String url) throws IOException {
        aliOSSUtils.deleteFile(url);
        return ResponseEntity.ok("删除文件");
    }

    @PostMapping("/changeClassTime")
    @ApiOperation("更新课程时间")
    public ResponseEntity<String> changeClassTime(@RequestParam("class_id") int class_id,   @RequestParam("timestamp") String time){
        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        String res = classService.changeClassTime(dateTime,class_id);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}

