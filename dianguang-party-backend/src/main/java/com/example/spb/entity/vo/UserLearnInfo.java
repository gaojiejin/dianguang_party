package com.example.spb.entity.vo;

import com.example.spb.entity.po.Chapter;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class UserLearnInfo {

    private List<String> classLearned; //用户已修课程

    private String annualDuration; //年度学习总时长

    private int shouldLearned;//应修

    private int alreadyLearned; //已修

}
