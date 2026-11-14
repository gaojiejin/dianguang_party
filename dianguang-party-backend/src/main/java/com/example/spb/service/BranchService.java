package com.example.spb.service;

import com.example.spb.entity.po.Branch;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spb.entity.vo.BranchManage;
import com.example.spb.entity.po.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zff
 * @since 2024-05-21
 */
public interface BranchService extends IService<Branch> {

    int addBranchManager(int branchID, int jobID);

    BranchManage getBranchByManagerId(int managerId);

    Boolean isExistBranch(String branchName);

    int queryBranchByJobID(String jobID);

    int queryBranchIDByBranchName(String branchName);

    void addBranch(String branchName,String branchDis, String department, String branchType);

    void deleteBranch(String branchName);

    String updateBranch(String branchName, String currentName,Integer managerId,String branchDis,String department);

    ArrayList<User> queryBranchMember(int branchID);

    void deleteBranchManager(int branchID);

    int queryBranchIDByJobID(String jobID);

    List<Map<String, Object>> queryIDNameMapping();

    List<BranchManage> getBranchManage();

    List<User> getUsersByBranchID(int branchID);

    List<User> getUsersByBranchName(String branchName);

    void changeUserStatus(Integer oldManageId,Integer curManageId);

    Branch queryBranchByBranchName(String branchName);

    List<Branch> getBranches();
}
