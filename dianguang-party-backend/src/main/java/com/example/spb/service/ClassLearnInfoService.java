package com.example.spb.service;

import com.example.spb.entity.po.ClassLearnInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spb.entity.po.User;
import com.example.spb.entity.vo.UserLearnInfo;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 课程学习信息表 服务类
 * </p>
 *
 * @author gjj
 * @since 2024-09-12
 */
public interface ClassLearnInfoService extends IService<ClassLearnInfo> {
    String exportAllLearnStats(List<String> branchList);

    String addLearnDuration(int classId, int chapterId, int jobId, int if_learned,int learnDuration);

    String changeLearnDuration(int classId, int userId, int if_learned,int learn_duration);

    String changeLearnDurationByBranch(int classId, int branch_id, int if_learned,int min_duration, int max_duration);

    String updateClassLearnDuration(int classId,int userId,int if_learned,int classDurationInc);

    String createClassLearnInfo(int classId, int user_id ,int if_learned,int learnDuration);

    UserLearnInfo getOwnLearnInfo(int userId);

    void judgeIsLearned(int classId);

    String exportLearnStatsByClass(int classId);

    String exportCertificate(String job_id,String start_time, String completion_time, String export_time) throws IOException;

//    String exportPartyCertificate(String job_id) throws IOException;

    String finishTrain(List<Integer> usersId);

    String deleteAllClassLearnInfo(List<Integer> users_id);

    List<User> getRoleLearnedList(String role);
    int deleteByClassId(int classId);

    String deleteCertificate(String certificatePath);
}
