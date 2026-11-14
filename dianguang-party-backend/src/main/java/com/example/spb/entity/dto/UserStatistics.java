package com.example.spb.entity.dto;

import com.example.spb.entity.po.User;
import lombok.Data;

@Data
public class UserStatistics {
    private User user;

    private boolean ifLearned;

    private Integer duration = 0;
}
