package com.example.spb.controller;

import com.example.spb.entity.po.User;
import com.example.spb.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zff
 * @since 2024-04-29
 */
@Controller
@CrossOrigin
@Slf4j
@Api("用户控制类")
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;



    @PostMapping("/addUser")
    @ApiOperation("添加单一用户")
    public ResponseEntity<String> addUser(@RequestParam("name") String name, @RequestParam("job_id") String job_id,
                                          @RequestParam("branch_id") int branch_id, @RequestParam("role") String role,
                                          @RequestParam("post") String post,
                                          @RequestParam("phone") String phone, @RequestParam("department") String department,
                                          @RequestParam("grade") String grade, @RequestParam(value = "portrait", required = false) MultipartFile portrait) throws IOException {
        return ResponseEntity.ok(userService.addUser(name, job_id, branch_id, role, post, phone, department, grade, portrait));
    }

//    添加单个用户
    @PostMapping("/add")
    @ApiOperation("添加单一用户")
    public ResponseEntity<Object> saveAdmin(@RequestBody User user) {
        System.out.print(user);
        int isSuccess = userService.saveUser(user);
        if (isSuccess == 1) {
            return ResponseEntity.ok("用户添加成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用户添加失败");
        }
    }
    @PostMapping("/test")
    @ApiOperation("测试通路")
    public ResponseEntity<String> test(){
        Subject currentUser = SecurityUtils.getSubject();
        System.out.println("当前登录用户：" + currentUser.getPrincipal());
        return ResponseEntity.ok("111");
    }

    //    批量添加用户
    @PostMapping("/uploadAdminFile")
    @ApiOperation("批量添加成员")
    public ResponseEntity<String> uploadUserFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.ok().body("文件不能为空");
        }
        try {
            // 在保存用户信息之前检查文件格式
            boolean isValid = userService.saveUserFromExcel(file);
            if (!isValid) {
                return ResponseEntity.ok().body("文件不规范");
            }
            return ResponseEntity.ok("用户上传成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("用户上传失败");
        }
    }


//    修改用户信息
    @PostMapping("/update")
    @ApiOperation("修改用户信息")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        try {
            String job_id = user.getJobId();
            userService.updateUser(job_id, user);
            return ResponseEntity.ok("User updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error updating user: " + e.getMessage());
        }
    }

//    删除用户信息
    @PostMapping("/delete")
    @ApiOperation("删除用户信息")
    public ResponseEntity<String> deleteUser(@RequestParam("job_id") String job_id) {
        try {
            userService.deleteUser(job_id);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting user: " + e.getMessage());
        }
    }

//    修改密码
    @PostMapping("/updatePasswordByPrePwd")
    @ApiOperation("通过原密码修改密码")
    public ResponseEntity<String> updatePasswordByPrePwd(@RequestParam("inputPwd") String inputPwd, @RequestParam("newPwd") String newPwd, @RequestParam("job_id") String job_id) {
        try {
            String prePwd = userService.findPwdByJobID(job_id);
            if (!inputPwd.equals(prePwd)){
                return ResponseEntity.ok().body("原密码错误");
            }
            else {
                userService.updatePasswordByJobID(job_id,newPwd);
                return ResponseEntity.ok("密码修改成功");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error update pwd: " + e.getMessage());
        }
    }

    //    找回密码
    @PostMapping("/setNewPwd")
    @ApiOperation("忘记密码后设置新密码")
    public ResponseEntity<String> setNewPwd(@RequestParam("newPwd") String newPwd,@RequestBody User user) {
        try {
                String JobID = user.getJobId();
                userService.updatePasswordByJobID(JobID,newPwd);
                return ResponseEntity.ok("Password updated successfully");
            } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @PostMapping("/isBoundedPhone")
    @ApiOperation("判断当前用户是否绑定了手机号")
    public ResponseEntity<Boolean> isBoundedPhone(@RequestParam("jobID") String jobID){
        try {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userService.isPhoneBounded(jobID));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(!userService.isPhoneBounded(jobID));
        }
    }

    //    绑定手机号
    @PostMapping("/boundPhoneNumber")
    @ApiOperation("绑定手机号")
    public ResponseEntity<String> boundPhoneNumber(@RequestParam("phone") String phone, @RequestParam("jobID") String jobID) {
        try {
            userService.boundPhoneNumber(jobID,phone);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error bound phone " + e.getMessage());
        }
        return ResponseEntity.ok(" phone bounded successfully");
    }

    //    修改密码
    @PostMapping("/updatePasswordByphone")
    @ApiOperation("通过手机验证码修改密码")
    public ResponseEntity<String> updatePasswordByPhone(@RequestParam("phone") String phone, @RequestParam("newPwd") String newPwd) {
        try {
            log.warn("日志测试");
            User user = userService.queryUserByPhone(phone);
            String job_id = user.getJobId();
            userService.updatePasswordByJobID(job_id,newPwd);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error update pwd: " + e.getMessage());
        }
        return ResponseEntity.ok("Password updated successfully");
    }

//    上传/更新头像
//    @RequiresRoles("党员")
    @PostMapping("/uploadAvatar")
    @ApiOperation("上传/更新头像")
    public ResponseEntity<String> updateAvatar(@RequestParam("avatar") MultipartFile file,@RequestParam("job_id") String job_id) throws IOException {
        if (file.isEmpty()){
            return ResponseEntity.badRequest().body("请选择上传的文件");
        }
        else {
            userService.updateAvatarByJobID(job_id,file);
            return ResponseEntity.ok("文件上传成功");
        }
    }
//    查询用户信息
    // @RequiresRoles("党员")
    @GetMapping("/queryByJobID")
    @ApiOperation("查询用户信息")
    public ResponseEntity<Object> queryUserByJobID(@RequestParam("job_id") String job_id){
        User user = userService.findByJobID(job_id);
        if (user != null){
            return ResponseEntity.ok(user);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    //    查询全体用户信息
//    @RequiresPermissions("支部书记:manage")
    // @RequiresRoles("支部书记")
    @GetMapping("/queryAllUser")
    @ApiOperation("查询全体用户信息")
    public ResponseEntity<List<User>> queryAllUser(){
        List<User> allUser = userService.findAllUser();
        if (allUser != null){
            return ResponseEntity.ok(allUser);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/queryUsersByPage")
    @ApiOperation("查询全体用户信息")
    public ResponseEntity<Page<User>> queryUsersByPage(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize") int pageSize){
        Page<User> users = userService.getUserPage(pageNum, pageSize);
        if (users != null){
            return ResponseEntity.ok(users);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    //    查询用户信息
    @GetMapping("/queryByPhone")
    @ApiOperation("查询用户信息")
    public ResponseEntity<Object> queryUserByPhone(@RequestParam("phone") String phone){
        User user = userService.queryUserByPhone(phone);
        if (user != null){
            return ResponseEntity.ok(user);
        }else {
            return ResponseEntity.notFound().build();
        }
    }
    //    显示头像
    @GetMapping("/queryAvatar")
    @ApiOperation("显示头像")
    public ResponseEntity<Object> queryAvatar(@RequestParam("job_id") String job_id) throws IOException {
        User user = userService.findByJobID(job_id);
        String avatar = userService.getPortraitByJobId(job_id);

        if (avatar != null){
            return ResponseEntity.ok().body(avatar);
//            byte[] avatarData = Files.readAllBytes(Paths.get(avatar));
//            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(avatarData);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    //    删除用户信息
    @PostMapping("/deleteActivists")
    @ApiOperation("删除积极分子账号")
    public ResponseEntity<String> deleteActivists() {
        try {
            userService.deleteActivists();
            return ResponseEntity.ok("Activists deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error deleting user: " + e.getMessage());
        }
    }

    @PostMapping("/getMoban")
    @ApiOperation("返回批量添加模板")
    public ResponseEntity<String> getMoban() {
        String moban = userService.getMoban();
        return ResponseEntity.status(HttpStatus.OK).body(moban);
    }

    @PostMapping("/getPartyUser")
    @ApiOperation("获取所有党员")
    public ResponseEntity<List<User>> getPartyUser() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getPartyUser());
    }

    @PostMapping("/getActivistsUser")
    @ApiOperation("获取所有积极分子")
    public ResponseEntity<List<User>> getActivistsUser() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getActivistsUser());
    }


}




