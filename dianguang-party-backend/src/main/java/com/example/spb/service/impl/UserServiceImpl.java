package com.example.spb.service.impl;
import org.apache.poi.ss.usermodel.Cell;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.spb.constants.OssConstants;
import com.example.spb.entity.po.Branch;
import com.example.spb.entity.po.User;
import com.example.spb.mapper.BranchMapper;
import com.example.spb.mapper.UserMapper;
import com.example.spb.service.BranchService;
import com.example.spb.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spb.utils.AliOSSUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
// 引入 MyBatis Plus 提供的 Page 类
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zff
 * @since 2024-04-29
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    AliOSSUtils aliOSSUtils;

    @Autowired
    private BranchService branchService;

    @Autowired
    private BranchMapper branchMapper;


    @Override
    public String addUser(String name, String job_id, int branch_id, String role,String post, String phone, String department, String grade, MultipartFile portrait) throws IOException {
        User user = new User();
        user.setJobId(job_id);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_id", job_id);
        User user1 = userMapper.selectOne(queryWrapper);
        if (user1 == null){
            user.setPassword(job_id);
            user.setName(name);
            user.setBranchId(branch_id);
            user.setPost(post);
            if (role.equals("积极分子"))
                user.setRole("积极分子");
            else {
                user.setRole("党员");
            }
            user.setPhone(phone);
            user.setDepartment(department);
            user.setGrade(grade);

            if (portrait != null){
                String url = aliOSSUtils.upload(portrait);
                user.setPortrait(url);
            }
            userMapper.insert(user);
            return "添加用户成功";
        }
        else {
            return "添加用户失败，学工号已存在";
        }

    }
//    添加用户
    @Override
    public int saveUser(User user){
        return userMapper.insert(user);
    }
    //    批量导入用户
    @Override
    public boolean saveUserFromExcel(MultipartFile file) throws IOException {
        // 定义表格中必须包含的字段
        List<String> requiredFields = Arrays.asList("姓名", "工号/学号", "所属支部", "角色", "岗位", "手机号", "院系", "年级");
        Set<String> existingJobIds = new HashSet<>(); // 用于检查重复的学工号

        try (InputStream in = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(in)) {
            Sheet sheet = workbook.getSheetAt(0);


            List<User> users = new ArrayList<>();
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum() - 1; rowIndex++) {
                System.out.println(sheet.getLastRowNum());
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                User user = new User();

                // excel表姓名
                String name = row.getCell(0).getStringCellValue();
                user.setName(name);
                System.out.println(name);

                String id_job = "";

                if (row.getCell(1) != null) {
                    switch (row.getCell(1).getCellType()) {
                        case STRING:
                            id_job = row.getCell(1).getStringCellValue(); // 处理字符串
                            break;
                        case NUMERIC:
                            id_job = String.valueOf((int) row.getCell(1).getNumericCellValue()); // 处理数值并转换为字符串
                            break;
                        case FORMULA:
                            // 如果需要处理公式，可以根据具体需求进行处理
                            id_job = row.getCell(1).getCellFormula();
                            break;
                        default:
                            id_job = ""; // 处理其他类型（例如：空白或布尔型）
                    }
                } else {
                    id_job = ""; // 单元格为空
                }

                //要求不重复
                if (existingJobIds.contains(id_job)) {
                    System.out.println("学工号重复: " + id_job);
                    continue; // 跳过重复的学工号
                }

                existingJobIds.add(id_job); // 添加到已处理的集合中

                user.setJobId(id_job);
                user.setPassword(id_job);
                System.out.println(id_job);

                // 使用传递的 party 来查询并设置支部 ID
                int branch_id = branchService.queryBranchIDByBranchName(row.getCell(2).getStringCellValue());
                user.setBranchId(branch_id);
                System.out.println(branch_id);

                // excel表中的角色
                String role = row.getCell(3).getStringCellValue();
                user.setRole(role);
                System.out.println(role);

                String post = row.getCell(4).getStringCellValue();
                user.setPost(post);
                System.out.println(post);

                // excel表手机号
                String phoneNumber = "";
                if (row.getCell(5) != null) {
                    switch (row.getCell(5).getCellType()) {
                        case STRING:
                            phoneNumber = row.getCell(5).getStringCellValue(); // 处理字符串
                            break;
                        case NUMERIC:
                            phoneNumber = String.valueOf((long) row.getCell(5).getNumericCellValue()); // 处理数值并转换为字符串
                            break;
                        case FORMULA:
                            // 如果需要处理公式，可以根据具体需求进行处理
                            phoneNumber = row.getCell(5).getCellFormula();
                            break;
                        default:
                            phoneNumber = ""; // 处理其他类型（例如：空白或布尔型）
                    }
                } else {
                    phoneNumber = ""; // 单元格为空
                }
                user.setPhone(phoneNumber);
                System.out.println(phoneNumber);

                // excel表中的部门
                String department = row.getCell(6).getStringCellValue();
                user.setDepartment(department);
                System.out.println(department);

                // excel中的年级
                String grade = row.getCell(7).getStringCellValue();
                user.setGrade(grade);
                System.out.println(grade);

                System.out.println(user);
                users.add(user);
            }
            return this.saveBatch(users); // 使用 saveBatch 来提高性能
        }
    }


    @Override
    public void updateUser(String job_id,User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("job_id",job_id);
        userMapper.update(user,wrapper);
    }

    @Override
    public void deleteUser(String job_id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("job_id",job_id);
        User user = userMapper.selectOne(wrapper);
        String url = user.getPortrait();
        if (url != null){
            aliOSSUtils.deleteFile(url);
        }
        userMapper.delete(wrapper);
    }

    @Override
    public void deleteUserById(int id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        User user = userMapper.selectOne(wrapper);
        String url = user.getPortrait();
        if (url != null){
            aliOSSUtils.deleteFile(url);
        }
        userMapper.delete(wrapper);
    }

    @Override
    public User findByJobID(String job_id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("job_id",job_id);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public int updatePasswordByJobID(String jobID, String newPwd) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("job_id",jobID).set("password", newPwd);
        return userMapper.update(null, updateWrapper);
    }

    @Override
    public String findPwdByJobID(String job_id) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("job_id",job_id);
        return userMapper.selectOne(wrapper).getPassword();
    }

    @Override
    public int updateAvatarByJobID(String jobID, MultipartFile file) throws IOException {
        String url = aliOSSUtils.upload(file);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("job_id",jobID);
        User user = userMapper.selectOne(queryWrapper);
        if (user.getPortrait() != null){
            aliOSSUtils.deleteFile(user.getPortrait());
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("job_id",jobID);
        updateWrapper.eq("job_id",jobID).set("portrait", url);
        return userMapper.update(null, updateWrapper);
    }

//    @Override
//    public int updateAvatarByJobID(String jobID, MultipartFile file) throws IOException {
//        String filename = file.getOriginalFilename();
//        String path = uploadDir + filename;
//        System.out.println(path);
//        File destfile = new File(path);
////                 如果目录不存在，创建目录
//        if (!destfile.getParentFile().exists()) {
//            destfile.getParentFile().mkdirs();
//        }
//        file.transferTo(destfile.getAbsoluteFile());
//
//        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//        updateWrapper.eq("job_id",jobID).set("portrait", path);
//        return userMapper.update(null, updateWrapper);
//    }

    @Override
    public User queryUserByPhone(String phone) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",phone);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public User queryUserById(int id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public Boolean isPhoneBounded(String jobID) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("job_id",jobID);
        return userMapper.selectOne(wrapper).getPhone() != null;
    }

    @Override
    public List<User> findAllUser() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        return userMapper.selectList(wrapper);
     }


     @Override
    public Page<User> getUserPage(int pageNum, int pageSize) {
        // 创建分页对象，指定页码和每页条数
        Page<User> page = new Page<>(pageNum, pageSize);

        // 构建查询条件
        QueryWrapper<User> wrapper = new QueryWrapper<>();

        // 执行分页查询，返回 Page 对象
        return userMapper.selectPage(page, wrapper);
    }


    @Override
    public void deleteActivists() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("role","积极分子");
        List<User> users = userMapper.selectList(wrapper);
        for (User user: users){
            String url = user.getPortrait();
            if (url != null){
                aliOSSUtils.deleteFile(url);
            }
        }
        userMapper.delete(wrapper);
    }


    @Override
    public String getPortraitByJobId(String job_id) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("job_id",job_id);
        return userMapper.selectOne(wrapper).getPortrait();
    }

    public void boundPhoneNumber(String job_id, String phone) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("job_id",job_id).set("phone",phone);
        userMapper.update(null,wrapper);
    }

        @Override
    public boolean is_valid(MultipartFile file) throws IOException {
        // 定义表格中必须包含的字段
        List<String> requiredFields = Arrays.asList("姓名", "工号/学号", "支部名称", "身份", "手机号", "院系", "年级");

        try (InputStream in = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(in)) {
            Sheet sheet = workbook.getSheetAt(0);

            // 检查第一行表头是否符合要求
            Row headerRow = sheet.getRow(0);
            for (int i = 0; i < requiredFields.size(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell == null || !requiredFields.get(i).equals(cell.getStringCellValue().trim())) {
                    // 表头字段不符合要求
                    return false;
                }
            }
            return true;
    }


}

    @Override
    public String getMoban() {
        return OssConstants.MOBAN_PATH;
    }

    @Override
    public List<User> getPartyUser(){
        List<User> users = new ArrayList<>();
        QueryWrapper<Branch> branchQueryWrapper = new QueryWrapper<>();
        branchQueryWrapper.eq("authority", 0);
        List<Branch> branches = branchMapper.selectList(branchQueryWrapper);
        for (Branch branch: branches){
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("branch_id",branch.getBranchId());
            users.addAll(userMapper.selectList(userQueryWrapper));
        }
        return users;
    }
    @Override
    public List<User> getActivistsUser(){
        List<User> users = new ArrayList<>();
        QueryWrapper<Branch> branchQueryWrapper = new QueryWrapper<>();
        branchQueryWrapper.eq("authority", 1);
        List<Branch> branches = branchMapper.selectList(branchQueryWrapper);
        for (Branch branch: branches){
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("branch_id",branch.getBranchId());
            users.addAll(userMapper.selectList(userQueryWrapper));
        }
        return users;
    }
}
