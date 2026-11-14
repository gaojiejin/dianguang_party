package com.example.spb.entity.vo;

import com.example.spb.entity.po.Branch;
import com.example.spb.entity.po.Class;
import com.example.spb.entity.dto.UserStatistics;
import lombok.Data;

import java.util.List;

@Data
public class LearnRateBranchStatistics {

    private Class learn_class;

    private Branch branch;

    private List<UserStatistics> userStatistics;

    private int num = 0;

    private int finishNum = 0;
}
