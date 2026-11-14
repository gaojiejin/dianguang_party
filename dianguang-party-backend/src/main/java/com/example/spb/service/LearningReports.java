package com.example.spb.service;
import java.util.List;
import java.util.Map;
public interface LearningReports {
 String submit(String reportId,int user,int course,int chapter,int completed,int seconds);
 List<Map<String,Object>> progress(int user);
}
