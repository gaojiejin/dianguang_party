package com.example.spb.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.spb.entity.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zff
 * @since 2024-04-29
 */
public interface UserService extends IService<User> {

    String getPortraitByJobId(String job_id);

    void boundPhoneNumber(String job_id, String phone) ;

    String addUser(String name, String job_id, int branch_id, String role,String post, String phone, String department, String grade, MultipartFile portrait) throws IOException;

    int saveUser(User user);

    boolean saveUserFromExcel(MultipartFile file) throws IOException;

    void updateUser(String job_id, User user);

    void deleteUser(String job_id);

    void deleteUserById(int id);

    User findByJobID(String job_id);

    int updatePasswordByJobID(String jobID, String newPwd);

    String findPwdByJobID(String jobId);

    Page<User> getUserPage(int pageNum, int pageSize);

    int updateAvatarByJobID(String jobID, MultipartFile file) throws IOException;


    User queryUserByPhone(String phone);

    User queryUserById(int id);
    Boolean isPhoneBounded(String jobID);

    List<User> findAllUser();

    void deleteActivists();


    boolean is_valid(MultipartFile file) throws IOException;

    String getMoban();

    List<User> getPartyUser();

    List<User> getActivistsUser();
}
