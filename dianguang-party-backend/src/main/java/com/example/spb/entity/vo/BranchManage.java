package com.example.spb.entity.vo;

import com.example.spb.entity.po.Branch;
import com.example.spb.entity.po.User;
import lombok.Data;

import java.util.List;

@Data
public class BranchManage {
    private Branch branch;

    private User secretary;

    private List<User> users;

    private int num = 0;
}
