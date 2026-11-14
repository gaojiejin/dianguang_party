package com.example.spb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.spb.entity.po.Branch;
import com.example.spb.entity.vo.BranchManage;
import com.example.spb.entity.po.User;
import com.example.spb.entity.vo.UserLearnInfo;
import com.example.spb.mapper.BranchMapper;
import com.example.spb.mapper.UserMapper;
import com.example.spb.service.BranchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zff
 * @since 2024-05-21
 */
@Service
public class BranchServiceImpl extends ServiceImpl<BranchMapper, Branch> implements BranchService {
    @Autowired
    private BranchMapper branchMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public int addBranchManager(int branchID, int managerId) {
//        先更新原支部书记的状态
        QueryWrapper<Branch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branch_id",branchID);
        Branch oldBranch = branchMapper.selectOne(queryWrapper);

        // 检查用户是否是超级管理员
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("id",managerId);
        User curUser = userMapper.selectOne(queryWrapper1);
        if (curUser != null ) {
            if (curUser.getRole() == "超级管理员") {
                return 0;
            }
        }

        // 检查是否有旧的支部书记，并且 managerId 不为空
        if (oldBranch != null && oldBranch.getManagerId() != null) {
            int oldManageId = oldBranch.getManagerId();
            this.changeUserStatus(oldManageId, managerId);
        }
        else {
            this.changeUserStatus(managerId, managerId);
        }

        Branch branch = new Branch();
        branch.setBranchId(branchID);
        branch.setManagerId(managerId);
        return branchMapper.updateById(branch);
    }

    @Override
    public BranchManage getBranchByManagerId(int managerId){
        QueryWrapper<Branch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("manager_id",managerId);
        Branch branch = branchMapper.selectOne(queryWrapper);
        BranchManage branchManage = new BranchManage();
        branchManage.setBranch(branch);
        if (branch.getManagerId() != null) {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            User secretary = userMapper.selectOne(userQueryWrapper.eq("id", branch.getManagerId()));
            QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<>();
            List<User> users = userMapper.selectList(userQueryWrapper1.eq("branch_id", branch.getBranchId()).ne("id", branch.getManagerId()));
            branchManage.setSecretary(secretary);
            branchManage.setUsers(users);
            branchManage.setNum(users.size() + 1);
        }else {
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            List<User> users = userMapper.selectList(userQueryWrapper.eq("branch_id", branch.getBranchId()));
            branchManage.setUsers(users);
            branchManage.setNum(users.size());
        }
        return branchManage;
    }

    @Override
    public Boolean isExistBranch(String branchName) {
//        branchID作为主键被查询
        QueryWrapper<Branch> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branch_name",branchName);
        Branch branch = branchMapper.selectOne(queryWrapper);
        return branch != null;
    }

    @Override
    public int queryBranchByJobID(String jobID) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("job_id",jobID);
        return userMapper.selectOne(wrapper).getBranchId();
    }

    @Override
    public int queryBranchIDByBranchName(String branchName) {
        QueryWrapper<Branch> wrapper= new QueryWrapper<>();
        wrapper.eq("branch_name",branchName);
        return branchMapper.selectOne(wrapper).getBranchId();
    }

    @Override
    public void addBranch(String branchName,String branchDis, String department, String branchType) {
        Branch branch = new Branch();
        branch.setBranchName(branchName);
        branch.setBranchDis(branchDis);
        branch.setDepartment(department);
        if (branchType.equals("积极分子支部")){
            branch.setAuthority(1);
        }else {
            branch.setAuthority(0);
        }
        branch.setCreateTime(LocalDateTime.now());
        branchMapper.insert(branch);
    }

    @Transactional
    @Override
    public void deleteBranch(String branchName) {
        QueryWrapper<Branch> wrapper = new QueryWrapper<>();
        wrapper.eq("branch_name",branchName);
        int branchId = branchMapper.selectOne(wrapper).getBranchId();
        QueryWrapper<User> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("branch_id",branchId);
        List<User> users = userMapper.selectList(wrapper1);
        for (User user : users) {
            user.setBranchId(null);
            if (Objects.equals(user.getRole(), "支部管理员")){
                user.setRole("党员");
            }
            userMapper.updateById(user);
        }
        branchMapper.delete(wrapper);
    }

    public Branch queryBranchByBranchName(String branchName) {
        QueryWrapper<Branch> wrapper = new QueryWrapper<>();
        wrapper.eq("branch_name",branchName);
        return branchMapper.selectOne(wrapper);
    }


    @Override
    public String updateBranch(String branchName, String currentName,Integer managerId,String branchDis,String department) {
        if (managerId != null){
            int res = this.addBranchManager(this.queryBranchIDByBranchName(branchName),managerId);
            if (res == 0) {
                return "党支部管理员不能是超级管理员";
            }
        }
        UpdateWrapper<Branch> wrapper = new UpdateWrapper<>();
        wrapper.eq("branch_name", branchName);

        if (currentName != null) {
            wrapper.set("branch_name", currentName);
        }

        if (branchDis != null) {
            wrapper.set("branch_dis", branchDis);
        }

        if (department != null) {
            wrapper.set("department", department);
        }
        branchMapper.update(null,wrapper);
        return "编辑支部信息成功";
    }

    @Override
    public ArrayList<User> queryBranchMember(int branchID) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        ArrayList<User> list;
        wrapper.eq("branch_id",branchID);
        list = (ArrayList) userMapper.selectList(wrapper);
        return list;
    }

    @Override
    public void deleteBranchManager(int branchID) {

        QueryWrapper<Branch> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("branch_id",branchID);
        int oldManageId = branchMapper.selectOne(wrapper1).getManagerId();
        this.changeUserStatus(oldManageId,null);

        UpdateWrapper<Branch> wrapper = new UpdateWrapper<>();
        wrapper.eq("branch_id",branchID).set("manager_id",null);
        branchMapper.update(null,wrapper);
    }

    @Override
    public int queryBranchIDByJobID(String jobID) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("job_id",jobID);
        return userMapper.selectOne(wrapper).getBranchId();
    }

    @Override
    public List<Map<String, Object>> queryIDNameMapping() {
        QueryWrapper<Branch> wrapper = new QueryWrapper<>();
        wrapper.select("branch_id","branch_name");
        return branchMapper.selectMaps(wrapper);
    }

    @Override
    public List<Branch> getBranches() {
        QueryWrapper<Branch> wrapper = new QueryWrapper<>();
        return branchMapper.selectList(wrapper);
    }

    @Override
    public List<BranchManage> getBranchManage() {
        QueryWrapper<Branch> branchQueryWrapper = new QueryWrapper<>();
        List<Branch> branchList = branchMapper.selectList(branchQueryWrapper);
        List<BranchManage> branchManageList = new ArrayList<>();
        for (Branch branch : branchList) {
            BranchManage branchManage = new BranchManage();
            branchManage.setBranch(branch);
            if (branch.getManagerId() != null) {
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                User secretary = userMapper.selectOne(userQueryWrapper.eq("id", branch.getManagerId()));
                QueryWrapper<User> userQueryWrapper1 = new QueryWrapper<>();
                List<User> users = userMapper.selectList(userQueryWrapper1.eq("branch_id", branch.getBranchId()).ne("id", branch.getManagerId()));
                branchManage.setSecretary(secretary);
                branchManage.setUsers(users);
                branchManage.setNum(users.size() + 1);
            }else {
                QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
                List<User> users = userMapper.selectList(userQueryWrapper.eq("branch_id", branch.getBranchId()));
                branchManage.setUsers(users);
                branchManage.setNum(users.size());
            }
           branchManageList.add(branchManage);
        }
        return branchManageList;
    }

    @Override
    public List<User> getUsersByBranchID(int branchID) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        return userMapper.selectList(queryWrapper.eq("branch_id",branchID));
    }

    @Override
    public List<User> getUsersByBranchName(String branchName) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        return userMapper.selectList(queryWrapper.eq("branch_name",branchName));
    }

    @Override
    public void changeUserStatus(Integer oldManageId,Integer curManageId) {
        if (oldManageId != null) {
            UpdateWrapper<User> updateWrapperOld = new UpdateWrapper<>();
            updateWrapperOld.eq("id", oldManageId).set("role", "党员");
            userMapper.update(null, updateWrapperOld);
        }

        if (curManageId != null ) {
            UpdateWrapper<User> updateWrapperCur = new UpdateWrapper<>();
            updateWrapperCur.eq("id", curManageId).set("role", "支部管理员");
            userMapper.update(null, updateWrapperCur);
        }
    }
}
