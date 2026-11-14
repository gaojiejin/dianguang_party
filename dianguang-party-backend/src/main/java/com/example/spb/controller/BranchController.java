package com.example.spb.controller;


import com.example.spb.entity.po.Branch;
import com.example.spb.entity.vo.BranchManage;
import com.example.spb.entity.po.User;
import com.example.spb.service.BranchService;
import com.example.spb.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zff
 * @since 2024-05-21
 */
@Controller
@Slf4j
@CrossOrigin
@Api("支部管理类")
@RequestMapping("/branch")
public class BranchController {
    @Autowired
    private BranchService branchService;

    @Autowired
    private UserService userService;

    @PostMapping("/addBranch")
    @ApiOperation("添加支部")
    public ResponseEntity<String> addBranch(@RequestParam("branchName") String branchName,
                                            @RequestParam("branchDis") String branchDis,
                                            @RequestParam("department") String department,
                                            @RequestParam("branchType") String branchType) {

        Boolean isExist = branchService.isExistBranch(branchName);
        if (isExist){
            return ResponseEntity.ok().body("支部名称已存在！");
        }
        branchService.addBranch(branchName,branchDis,department, branchType);
        return ResponseEntity.ok("支部添加成功");
    }

    @PostMapping("/deleteBranch")
    @ApiOperation("删除支部")
    public ResponseEntity<String> deleteBranch(@RequestParam("branchName") String branchName){

        Boolean isExist = branchService.isExistBranch(branchName);
        if (!isExist){
            return ResponseEntity.status(HttpStatus.OK).body("支部名称不存在！");
        }
        branchService.deleteBranch(branchName);
        return ResponseEntity.ok("成功删除了该党支部");
    }

    @PostMapping("/updateBranch")
    @ApiOperation("编辑支部信息")
    public ResponseEntity<String> updateBranch(@RequestParam("branch_name") String branchName,
                                               @RequestParam(value = "current_name",required = false) String currentName,
                                               @RequestParam(value = "manager_id",required = false) Integer managerId,
                                               @RequestParam(value = "branch_dis",required = false) String branchDis,
                                               @RequestParam(value = "department",required = false) String department){

        Boolean isExist = branchService.isExistBranch(branchName);
        if (!isExist){
            return ResponseEntity.ok().body("支部名称不存在！");
        }
        Branch branch = branchService.queryBranchByBranchName(branchName);
        int branchId = branch.getBranchId();
        if (managerId!=null){
            if (branchId!=userService.queryUserById(managerId).getBranchId()){
                return ResponseEntity.ok("不能选择非本支部的成员为支部书记");
            }
        }
        String res = branchService.updateBranch(branchName,currentName,managerId,branchDis,department);
        return ResponseEntity.ok(res);
    }

//    @PostMapping("/addBranchManager")
//    @ApiOperation("添加支部管理员")
//    public ResponseEntity<String> addBranchManager(@RequestParam("branchName") String branchName,@RequestParam("jobID") int jobID){
//
//        //先查询是否有该支部
//        Boolean isExist = branchService.isExistBranch(branchName);
//        if (!isExist){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("党支部不存在");
//        }
//        int branchID = branchService.queryBranchIDByBranchName(branchName);
//        //再判断用户是否为该支部的成员
//        int curBranchID = branchService.queryBranchByJobID(jobID);
//        if (branchID != curBranchID){
//            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("设置的用户不是该党支部的成员");
//        }
//        //最后添加用户为支部的管理员
//        int res = branchService.addBranchManager(branchID,jobID);
//        if (res == 1){
//            return ResponseEntity.ok("成功添加了该党支部管理员");
//        }
//        else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("添加管理员过程失败");
//        }
//
//    }
//
//    @PostMapping("/deleteBranchManager")
//    @ApiOperation("删除支部管理员")
//    public ResponseEntity<String> deleteBranchManager(@RequestParam("branchName") String branchName){
//
//        //先查询是否有该支部
//        Boolean isExist = branchService.isExistBranch(branchName);
//        if (!isExist){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("党支部不存在");
//        }
//        int branchID = branchService.queryBranchIDByBranchName(branchName);
//
//        branchService.deleteBranchManager(branchID);
//
//        return ResponseEntity.ok("成功删除了该党支部管理员");
//    }


    @PostMapping("/queryBranchMember")
    @ApiOperation("查看支部成员")
    public ResponseEntity<Object> queryBranchMember(@RequestParam("branchName") String branchName){
        //先查询是否有该支部
        Boolean isExist = branchService.isExistBranch(branchName);
        if (!isExist){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("该支部不存在");
        }
        int branchID = branchService.queryBranchIDByBranchName(branchName);
        ArrayList<User> list;
        list = branchService.queryBranchMember(branchID);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/addActiveBranchManager")
    @ApiOperation("添加积极分子管理员")
    public ResponseEntity<String> addActiveBranchManager(@RequestParam("jobID") int jobID){
        String branchName="积极分子";
        int branchID = 0;
        //先查询是否有积极分子专属党支部
        Boolean isExist = branchService.isExistBranch(branchName);
        if (!isExist){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("积极分子党支部不存在");
        }
        int res = branchService.addBranchManager(branchID,jobID);
        if (res == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("超级管理员不能被设定为党支部书记");
        }
        return ResponseEntity.ok("成功添加了积极分子党支部管理员");
    }

    @PostMapping("/queryBranchIDByJobID")
    @ApiOperation("查看用户所在支部")
    public ResponseEntity<Integer> queryBranchIDByJobID(@RequestParam("jobID") String jobID){
        int branchID = branchService.queryBranchIDByJobID(jobID);
        return ResponseEntity.ok(branchID);
    }

    @GetMapping("/queryIDNameMapping")
    @ApiOperation("查看支部名称和id的映射")
    public ResponseEntity<List<Map<String, Object>>> queryIDNameMapping(){
        List<Map<String, Object>> mapping = branchService.queryIDNameMapping();
        return ResponseEntity.ok(mapping);
    }

    @PostMapping("/getBranches")
    @ApiOperation("获取所有支部")
    public ResponseEntity<List<Branch>> getBranches(){
        return ResponseEntity.ok(branchService.getBranches());
    }

    @PostMapping("/getBranchManage")
    @ApiOperation("获取所有支部信息")
    public ResponseEntity<List<BranchManage>> getBranchManage(){
        return ResponseEntity.ok(branchService.getBranchManage());
    }

    @PostMapping("/getBranchByManagerId")
    @ApiOperation("根据支部管理员获取支部信息")
    public ResponseEntity<BranchManage> getBranchByManagerId(@RequestParam("manager_id") int manager_id){
        return ResponseEntity.ok(branchService.getBranchByManagerId(manager_id));
    }

    @PostMapping("/getUsersByBranchID")
    @ApiOperation("获取指定支部的所有用户")
    public ResponseEntity<List<User>> getUsersByBranchID(@RequestParam("branch_id") int branch_id){
        return ResponseEntity.ok(branchService.getUsersByBranchID(branch_id));
    }

}

