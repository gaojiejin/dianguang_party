package com.example.spb.controller;


import com.example.spb.entity.vo.LearnRateStatistics;
import com.example.spb.entity.po.LearnRate;
import com.example.spb.entity.vo.LearnRateBranchStatistics;
import com.example.spb.entity.vo.UserLearnDetail;
import com.example.spb.service.LearnRateService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gjj
 * @since 2024-07-25
 */
@Controller
@Slf4j
@CrossOrigin
@RequestMapping("/learnRate")
public class LearnRateController {
    @Autowired
    private LearnRateService learnRateService;



//    @PostMapping("/addLearnRate")
//    @ApiOperation("添加学习进度")
//    public ResponseEntity<String> addLearnRate(@RequestParam("class_id") int class_id, @RequestParam("chapter_id") int chapter_id, @RequestParam("user_id") int user_id){
//        return ResponseEntity.ok(learnRateService.addLearnRate(class_id,chapter_id,user_id));
//    }

    @PostMapping("/getAllLearnRate")
    @ApiOperation("获取所有学习进度")
    public ResponseEntity<List<LearnRate>> getAllLearnRate(){
        return ResponseEntity.ok(learnRateService.getAllLearnRate());
    }

    @PostMapping("/getLearnRateByClassId")
    @ApiOperation("根据课程ID获取学习进度")
    public ResponseEntity<List<LearnRate>> getLearnRateByClassId(@RequestParam("class_id") int class_id){
        return ResponseEntity.ok(learnRateService.getLearnRateByClassId(class_id));
    }

    @PostMapping("/getLearnRateByChapterId")
    @ApiOperation("根据章节ID获取学习进度")
    public ResponseEntity<List<LearnRate>> getLearnRateByChapterId(@RequestParam("chatper_id") int chatper_id){
        return ResponseEntity.ok(learnRateService.getLearnRateByChapterId(chatper_id));
    }

    @PostMapping("/getLearnRateByUserId")
    @ApiOperation("根据用户ID获取学习进度")
    public ResponseEntity<List<LearnRate>> getLearnRateByUserId(@RequestParam("user_id") int user_id){
        return ResponseEntity.ok(learnRateService.getLearnRateByUserId(user_id));
    }

    @PostMapping("/getLearnRateByClassIdAndUserId")
    @ApiOperation("根据课程ID和用户ID获取学习进度")
    public ResponseEntity<List<LearnRate>> getLearnRateByClassIdAndUserId(@RequestParam("class_id") int class_id, @RequestParam("user_id") int user_id){
        return ResponseEntity.ok(learnRateService.getLearnRateByClassIdAndUserId(class_id, user_id));
    }
    @PostMapping("/getLearnRateByClassIdAndChapterId")
    @ApiOperation("根据课程ID和章节ID获取学习进度")
    public ResponseEntity<List<LearnRate>> getLearnRateByClassIdAndChapterId(@RequestParam("class_id") int class_id, @RequestParam("chapter_id") int chapter_id){
        return ResponseEntity.ok(learnRateService.getLearnRateByClassIdAndChapterId(class_id, chapter_id));
    }

    @PostMapping("/getLearnRateByUserIdAndChapterId")
    @ApiOperation("根据用户ID和章节ID获取学习进度")
    public ResponseEntity<LearnRate> getLearnRateByUserIdAndChapterId(@RequestParam("user_id") int user_id, @RequestParam("chapter_id") int chapter_id){
        return ResponseEntity.ok(learnRateService.getLearnRateByChapterIdAndUserId(chapter_id, user_id));
    }

    @PostMapping("/getLearnRateByClassIdAndChapterIdAndUserId")
    @ApiOperation("根据课程ID、章节ID和用户ID获取学习进度")
    public ResponseEntity<LearnRate> getLearnRateByClassIdAndChapterIdAndUserId(@RequestParam("class_id") int class_id, @RequestParam("chapter_id") int chapter_id, @RequestParam("user_id") int user_id){
        return ResponseEntity.ok(learnRateService.getLearnRateByClassIdAndChapterIdAndUserId(class_id, chapter_id, user_id));
    }

    @PostMapping("/updateLearnRate")
    @ApiOperation("更新学习进度")
    public ResponseEntity<String> updateLearnRate(@RequestParam("class_id") int class_id, @RequestParam("chapter_id") int chapter_id, @RequestParam("user_id") int user_id, @RequestParam("ifLearned") int ifLearned){
        return ResponseEntity.ok(learnRateService.updateLearnRate(class_id, chapter_id, user_id, ifLearned));
    }

    @PostMapping("/deleteLearnRate")
    @ApiOperation("删除学习进度")
    public ResponseEntity<String> deleteLearnRate(@RequestParam("class_id") int class_id, @RequestParam("chapter_id") int chapter_id, @RequestParam("user_id") int user_id){
        return ResponseEntity.ok(learnRateService.deleteLearnRate(class_id, chapter_id, user_id));
    }

    @PostMapping("/getLearnRateStatistics")
    @ApiOperation("获取学习进度统计")
    public ResponseEntity<List<LearnRateStatistics>> getLearnRateStatistics(){
        return ResponseEntity.ok(learnRateService.getLearnRateStatistics());
    }

    @PostMapping("/getLearnRateStatisticsByBranchId")
    @ApiOperation("获取学习进度统计")
    public ResponseEntity<List<LearnRateStatistics>> getLearnRateStatisticsByBranchId(@RequestParam("branch_id") int branch_id){
        return ResponseEntity.ok(learnRateService.getLearnRateStatisticsByBranchId(branch_id));
    }

    @PostMapping("/getLearnRateBranchStatistics")
    @ApiOperation("获取学习进度分支统计")
    public ResponseEntity<LearnRateBranchStatistics> getLearnRateBranchStatistics(@RequestParam("class_id") int class_id, @RequestParam("branch_id") int branch_id){
        return ResponseEntity.ok(learnRateService.getLearnRateBranchStatistics(class_id, branch_id));
    }

    @PostMapping("/getUserLearnDetail")
    @ApiOperation("获取用户学习详情")
    public ResponseEntity<UserLearnDetail> getUserLearnDetail(@RequestParam("class_id") int class_id, @RequestParam("user_id") int user_id){
        return ResponseEntity.ok(learnRateService.getUserLearnDetail(class_id, user_id));
    }




}

