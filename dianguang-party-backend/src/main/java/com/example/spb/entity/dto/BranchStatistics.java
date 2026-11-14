package com.example.spb.entity.dto;

import com.example.spb.entity.po.Branch;
import lombok.Data;

@Data
public class BranchStatistics {
    private Branch branch;
    private int num = 0;
    private int finishNum = 0;

    public BranchStatistics(){

    }

    public BranchStatistics(Branch b, int size, int finishNum) {
        this.branch = b;
        this.num = size;
        this.finishNum = finishNum;
    }
}
