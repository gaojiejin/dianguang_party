package com.example.spb.entity.vo;

import com.example.spb.entity.po.Chapter;
import lombok.Data;

import java.util.List;

@Data
public class UserLearnDetail {
    private List<Chapter> chapterList; //章节列表

    private List<Boolean> chapterIfLearnedList; //章节是否学习完成列表

    private int num = 0;

    private int finishNum = 0;

}
