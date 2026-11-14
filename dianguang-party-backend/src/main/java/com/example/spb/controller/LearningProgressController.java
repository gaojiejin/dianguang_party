package com.example.spb.controller;
import com.example.spb.service.LearningReports;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @CrossOrigin @RequestMapping("/learning")
public class LearningProgressController {
 private final LearningReports service;
 public LearningProgressController(LearningReports service){this.service=service;}
 @GetMapping("/course-progress") public List<Map<String,Object>> progress(@RequestParam("user_id") int user){return service.progress(user);}
}

