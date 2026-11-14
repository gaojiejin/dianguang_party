package com.example.spb.entity.vo;

import com.example.spb.entity.dto.BranchStatistics;
import com.example.spb.entity.po.Class;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class LearnRateStatistics {
    private Class learn_class;

    private List<BranchStatistics> branches;

    private int num = 0;

    private int finishNum = 0;

    public LearnRateStatistics() {
    }

    public LearnRateStatistics(Class c, List<BranchStatistics> branchStatisticsList, int learnNum, int learnFinishedNum) {
        this.learn_class = c;
        this.branches = branchStatisticsList;
        this.num = learnNum;
        this.finishNum = learnFinishedNum;
    }
}
