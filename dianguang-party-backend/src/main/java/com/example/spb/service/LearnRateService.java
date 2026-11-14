package com.example.spb.service;

import com.example.spb.entity.po.LearnRate;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spb.entity.po.Swiper;
import com.example.spb.entity.vo.LearnRateBranchStatistics;
import com.example.spb.entity.vo.LearnRateStatistics;
import com.example.spb.entity.vo.UserLearnDetail;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gjj
 * @since 2024-07-25
 */
public interface LearnRateService extends IService<LearnRate> {
    String addLearnRate(int class_id, int chapter_id, int user_id);

    String addLearnRate(int class_id, int chapter_id);
    List<LearnRate> getAllLearnRate();

    List<LearnRate> getLearnRateByClassId(int class_id);

    List<LearnRate> getLearnRateByUserId(int user_id);

    List<LearnRate> getLearnRateByChapterId(int chapter_id);

    List<LearnRate> getLearnRateByClassIdAndChapterId(int class_id, int chapter_id);

    List<LearnRate> getLearnRateByClassIdAndUserId(int class_id, int user_id);

    LearnRate getLearnRateByChapterIdAndUserId(int chapter_id, int user_id);

    LearnRate getLearnRateByClassIdAndChapterIdAndUserId(int class_id, int chapter_id, int user_id);

    String updateLearnRate(int class_id, int chapter_id, int user_id, int ifLearned);

    String deleteLearnRate(int class_id, int chapter_id, int user_id);

    List<LearnRateStatistics> getLearnRateStatistics();

    List<LearnRateStatistics> getLearnRateStatisticsByBranchId(int branch_id);

    LearnRateBranchStatistics getLearnRateBranchStatistics(int class_id, int branch_id);

    UserLearnDetail getUserLearnDetail(int class_id, int user_id);

    String deleteAllLearnRate(List<Integer> usersId);

    void deleteByClassId(int classId);
}
