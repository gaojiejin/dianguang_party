package com.example.spb.controller;


import com.example.spb.entity.po.Chapter;
import com.example.spb.service.ChapterService;
import com.example.spb.service.LearnRateService;
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
 * 章节表 前端控制器
 * </p>
 *
 * @author zff
 * @since 2024-07-24
 */
@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/chapter")
public class ChapterController {

    @Autowired
    private ChapterService chapterService;
    @Autowired
    private LearnRateService learnRateService;
    @PostMapping("/addChapter")
    @ApiOperation("添加章节")
    public ResponseEntity<String> addChapter(@RequestParam(value = "chapter_portrait", required = false) String chapter_portrait,
                                             @RequestParam("class_id") int class_id,
                                             @RequestParam("founder_id") int founder_id,
                                             @RequestParam("founder") String founder,
                                             @RequestParam("content") String content,
                                             @RequestParam(value = "chapterImage", required = false) List<String> chapterImage,
                                             @RequestParam(value = "chapterVideo", required = false) String chapterVideo,
                                             @RequestParam(value = "duration", required = false) Integer duration,
                                             @RequestParam("title") String title,
                                             @RequestParam("type") int type) throws IOException {
        int chapter_id = chapterService.addChapter(class_id, founder_id, founder, title, type, chapter_portrait, content, chapterImage, chapterVideo, duration);
        if (chapter_id != -1 ){
//            learnRateService.addLearnRate(class_id,chapter_id);
            return ResponseEntity.ok("成功创建章节");
        }else{
            return ResponseEntity.ok("章节名已存在");
        }
    }

    @PostMapping("/checkChapterName")
    @ApiOperation("查看章节名字是否重复")
    public ResponseEntity<Boolean> checkChapterName(@RequestParam("class_id") int class_id, @RequestParam("title") String title) throws IOException {
        return ResponseEntity.ok(chapterService.checkChapterName(title, class_id));
    }

    @PostMapping("/getAllChapter")
    @ApiOperation("获取所有的章节")
    public ResponseEntity<List<Chapter>> getAllChapter(){
        return ResponseEntity.ok(chapterService.getAllChapter());
    }

    @PostMapping("/getChapterByClassId")
    @ApiOperation("根据课程ID获取章节")
    public ResponseEntity<List<Chapter>> getChapterByClassId(@RequestParam("class_id") int class_id){
        return ResponseEntity.ok(chapterService.getChapterByClassId(class_id));
    }

    @PostMapping("/getChapterByChapterId")
    @ApiOperation("根据章节ID获取章节")
    public ResponseEntity<Chapter> getChapterByChapterId(@RequestParam("chapter_id") int chapter_id){
        return ResponseEntity.ok(chapterService.getChapterByChapterId(chapter_id));
    }

    @PostMapping("/deleteChapter")
    @ApiOperation("删除章节")
    public ResponseEntity<String> deleteChapter(@RequestParam("chapter_id") int chapter_id){
        return ResponseEntity.ok(chapterService.deleteChapter(chapter_id));
    }

    @PostMapping("/changeChapterTime")
    @ApiOperation("更新课程时间")
    public ResponseEntity<String> changeClassTime(@RequestParam("chapter_id") int chapter_id,   @RequestParam("timestamp") String time){
        LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        String res = chapterService.changeChapterTime(dateTime,chapter_id);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}

