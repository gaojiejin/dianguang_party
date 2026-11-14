package com.example.spb.entity.vo;

import com.example.spb.entity.po.Chapter;
import com.example.spb.entity.po.Class;
import lombok.Data;

import java.util.List;

@Data
public class ClassAndChapter {
    private Class aClass;
    private List<Chapter> chapterList;
}
