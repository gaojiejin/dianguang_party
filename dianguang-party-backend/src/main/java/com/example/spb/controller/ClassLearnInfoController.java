package com.example.spb.controller;


import com.aliyun.oss.common.utils.HttpHeaders;
import com.example.spb.entity.po.User;
import com.example.spb.entity.vo.UserLearnInfo;
import com.example.spb.service.ChapterService;
import com.example.spb.service.ClassLearnInfoService;
import com.example.spb.service.LearnRateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 课程学习信息表 前端控制器
 * </p>
 *
 * @author gjj
 * @since 2024-09-12
 */
@Controller
@Slf4j
@CrossOrigin(origins = "*")
@Api("学习信息控制器")
@RequestMapping("/classLearnInfo")
public class ClassLearnInfoController {
    @Autowired private com.example.spb.service.LearningReports reportService;

    @Autowired
    private ClassLearnInfoService classLearnInfoService;
    @Autowired
    private ChapterService chapterService;

    @PostMapping("/addLearnDuration")
    @ApiOperation("学习后添加时长")
    public ResponseEntity<String> addLearnDuration(@RequestParam("class_id")int class_id,
                                                   @RequestParam("chapter_id")int chapter_id,
                                                   @RequestParam("user_id")int user_id,
                                                   @RequestParam("if_learned")int if_learned,
                                                   @RequestParam("learn_duration")int learn_duration, @RequestParam(value="report_id",required=false) String reportId){
        if(reportId!=null) return ResponseEntity.ok(reportService.submit(reportId,user_id,class_id,chapter_id,if_learned,learn_duration));
        return ResponseEntity.ok().body(classLearnInfoService.addLearnDuration(class_id,chapter_id,user_id,if_learned,learn_duration));
    }

    @GetMapping("/getOwnLearnInfo")
    @ApiOperation("获取个人学习信息")
    public ResponseEntity<UserLearnInfo> getOwnLearnInfo(@RequestParam("user_id")int user_id){
        return ResponseEntity.ok().body(classLearnInfoService.getOwnLearnInfo(user_id));
    }

    @PostMapping("/exportCertificate")
    @ApiOperation("导出培训证明")
    public ResponseEntity<String> exportCertificate(@RequestParam("job_id") String job_id,
                                                    @RequestParam("start_time") String start_time,
                                                    @RequestParam("completion_time") String completion_time,
                                                    @RequestParam("export_time") String export_time) throws IOException {
        return ResponseEntity.ok(classLearnInfoService.exportCertificate(job_id, start_time, completion_time, export_time));
    }
    @PostMapping("/deleteCertificate")
    @ApiOperation("删除培训证明")
    public ResponseEntity<String> deleteCertificate(@RequestParam("certificate_path")String certificatePath) throws IOException {
        return ResponseEntity.ok(classLearnInfoService.deleteCertificate(certificatePath));
    }
    @PostMapping("/exportAllLearnStats")
    @ApiOperation("导出全部课程学习数据excel")
    public ResponseEntity<String> exportAllLearnStats(@RequestBody List<String> branchList) {
        return ResponseEntity.ok(classLearnInfoService.exportAllLearnStats(branchList));
    }

    @PostMapping("/exportLearnStatsByClass")
    @ApiOperation("导出特定课程学习数据excel")
    public ResponseEntity<String> exportLearnStatsByClass(@RequestParam(value = "class_id") int class_id){
        return ResponseEntity.ok(classLearnInfoService.exportLearnStatsByClass(class_id));
    }

    @PostMapping("/finishTrain")
    @ApiOperation("积极分子结业")
    public ResponseEntity<String> finishTrain(@RequestBody List<Integer> users_id){
        return ResponseEntity.ok(classLearnInfoService.finishTrain(users_id));
    }

    @PostMapping("/changeLearnDurationByBranch")
    @ApiOperation("根据支部修改学习记录")
    public ResponseEntity<String> changeLearnDurationByBranch(@RequestParam("class_id") int classId,
                                                              @RequestParam("branch_id") int branchId,
                                                              @RequestParam("if_learned") int if_learned,
                                                              @RequestParam("min_duration") int min_duration,
                                                              @RequestParam("max_duration") int max_duration){
        return ResponseEntity.ok(classLearnInfoService.changeLearnDurationByBranch(classId, branchId,if_learned, min_duration, max_duration));
    }

    @GetMapping("/getClassDuration")
    @ApiOperation("获取课程学习时长上限")
    public ResponseEntity<Integer> getClassDuration(@RequestParam("class_id") int classId){
        return ResponseEntity.ok(chapterService.getClassDuration(classId));
    }
    @GetMapping("/getRoleLearnedList")
    @ApiOperation("获取某种角色已经学完的用户名单")
    public ResponseEntity<List<User>> getRoleLearnedList(@RequestParam(value = "role", defaultValue = "积极分子") String role){

        return ResponseEntity.ok(classLearnInfoService.getRoleLearnedList(role));
    }
}


