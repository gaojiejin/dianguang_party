package com.example.spb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.spb.entity.dto.UserStatistics;
import com.example.spb.entity.po.*;
import com.example.spb.entity.dto.BranchStatistics;
import com.example.spb.entity.vo.LearnRateStatistics;
import com.example.spb.entity.po.Class;
import com.example.spb.entity.vo.LearnRateBranchStatistics;
import com.example.spb.entity.vo.UserLearnDetail;
import com.example.spb.mapper.*;
import com.example.spb.service.LearnRateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gjj
 * @since 2024-07-25
 */
@Service
public class LearnRateServiceImpl extends ServiceImpl<LearnRateMapper, LearnRate> implements LearnRateService {
    @Autowired
    private LearnRateMapper learnRateMapper;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private ClassLearnInfoMapper classLearnInfoMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private BranchMapper branchMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public String addLearnRate(int class_id, int chapter_id, int user_id) {
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", class_id).eq("chapter_id", chapter_id).eq("user_id", user_id);
        LearnRate learnRate = learnRateMapper.selectOne(queryWrapper);
        if (learnRate == null) {
            LearnRate newLearnRate = new LearnRate();
            newLearnRate.setClassId(class_id);
            newLearnRate.setChapterId(chapter_id);
            newLearnRate.setUserId(user_id);
            newLearnRate.setIfLearned(0);
            newLearnRate.setLearningDuration(0);
            learnRateMapper.insert(newLearnRate);
            return "添加学习进度成功";
        }
        else {
            return "该用户已存在该章节的学习进度";
        }
    }

    @Override
    public String addLearnRate(int class_id, int chapter_id) {
        QueryWrapper<ClassLearnInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id",class_id);
        List<ClassLearnInfo> LearnInfos = classLearnInfoMapper.selectList(queryWrapper);

        for (ClassLearnInfo LearnInfo :LearnInfos) {
            LearnRate newLearnRate = new LearnRate();
            newLearnRate.setClassId(class_id);
            newLearnRate.setChapterId(chapter_id);
            newLearnRate.setUserId(LearnInfo.getUserId());
            newLearnRate.setIfLearned(0);
            newLearnRate.setLearningDuration(0);
            learnRateMapper.insert(newLearnRate);
            return "添加学习进度成功";
        }

        return null;
    }

    @Override
    public List<LearnRate> getAllLearnRate() {
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        return learnRateMapper.selectList(queryWrapper);
    }

    @Override
    public List<LearnRate> getLearnRateByClassId(int class_id) {
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", class_id);
        return learnRateMapper.selectList(queryWrapper);
    }

    @Override
    public List<LearnRate> getLearnRateByUserId(int user_id) {
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        return learnRateMapper.selectList(queryWrapper);
    }

    @Override
    public List<LearnRate> getLearnRateByChapterId(int chapter_id) {
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id", chapter_id);
        return learnRateMapper.selectList(queryWrapper);
    }

    @Override
    public List<LearnRate> getLearnRateByClassIdAndChapterId(int class_id, int chapter_id) {
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", class_id);
        queryWrapper.eq("chapter_id", chapter_id);
        return learnRateMapper.selectList(queryWrapper);
    }

    @Override
    public List<LearnRate> getLearnRateByClassIdAndUserId(int class_id, int user_id) {
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", class_id);
        queryWrapper.eq("user_id", user_id);
        return learnRateMapper.selectList(queryWrapper);
    }

    @Override
    public LearnRate getLearnRateByChapterIdAndUserId(int chapter_id, int user_id) {
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chapter_id", chapter_id);
        queryWrapper.eq("user_id", user_id);
        return learnRateMapper.selectOne(queryWrapper);
    }

    @Override
    public LearnRate getLearnRateByClassIdAndChapterIdAndUserId(int class_id, int chapter_id, int user_id) {
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", class_id);
        queryWrapper.eq("chapter_id", chapter_id);
        queryWrapper.eq("user_id", user_id);
        return learnRateMapper.selectOne(queryWrapper);
    }

    @Override
    public String updateLearnRate(int class_id, int chapter_id, int user_id, int ifLearned) {
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", class_id);
        queryWrapper.eq("chapter_id", chapter_id);
        queryWrapper.eq("user_id", user_id);
        LearnRate learnRate = learnRateMapper.selectOne(queryWrapper);
        if (learnRate != null) {
            learnRate.setIfLearned(ifLearned);
            learnRateMapper.updateById(learnRate);
        }
        else {
            LearnRate newLearnRate = new LearnRate();
            newLearnRate.setClassId(class_id);
            newLearnRate.setChapterId(chapter_id);
            newLearnRate.setUserId(user_id);
            newLearnRate.setIfLearned(ifLearned);
            learnRateMapper.insert(newLearnRate);
        }
        return "修改学习进度成功";
    }

    @Override
    public String deleteLearnRate(int class_id, int chapter_id, int user_id) {
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", class_id);
        queryWrapper.eq("chapter_id", chapter_id);
        queryWrapper.eq("user_id", user_id);
        LearnRate learnRate = learnRateMapper.selectOne(queryWrapper);
        if (learnRate != null) {
            learnRateMapper.deleteById(learnRate.getId());
        }
        return "删除学习进度成功";
    }


//    @Transactional
    @Override
    public String deleteAllLearnRate(List<Integer> users_id) {

        for (Integer user_id:users_id){
            QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", user_id);
            List<LearnRate> learnRateList = learnRateMapper.selectList(queryWrapper);
            // 遍历删除列表中的所有 LearnRate
            for (LearnRate learnRate : learnRateList) {
                learnRateMapper.deleteById(learnRate.getId());
            }
        }
        return "删除学习进度成功";
    }

    @Override
    public void deleteByClassId(int classId) {
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", classId);
        learnRateMapper.delete(queryWrapper);
    }

//    @Override
//    public List<LearnRateStatistics> getLearnRateStatistics() {
//        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
//        List<Class> classList = classMapper.selectList(queryWrapper);
//        QueryWrapper<Branch> queryWrapper1 = new QueryWrapper<>();
//        List<Branch> branchList = branchMapper.selectList(queryWrapper1);
//        List<LearnRateStatistics> learnRateStatisticsList = new ArrayList<>();
//        for (Class c : classList) {
//            int class_id = c.getClassId();
//            QueryWrapper<Chapter> queryWrapper2 = new QueryWrapper<>();
//            List<Chapter> chapterList = chapterMapper.selectList(queryWrapper2.eq("class_id", class_id));
//            List<BranchStatistics> branchStatisticsList = new ArrayList<>();
//            int learnNum = 0;
//            int learnFinishedNum = 0;
//            for (Branch b : branchList) {
//                if (Objects.equals(c.getAuthority(), b.getAuthority())){
//                    QueryWrapper<User> queryWrapper3 = new QueryWrapper<>();
//                    List<User> userList = userMapper.selectList(queryWrapper3.eq("branch_id", b.getBranchId()));
//                    BranchStatistics branchStatistics = new BranchStatistics();
//                    int finishNum = 0;
//                    for (User u : userList) {
//                        int user_id = u.getId();
//                        boolean ifLearned = true;
//                        for (Chapter ch : chapterList) {
//                            QueryWrapper<LearnRate> queryWrapper4 = new QueryWrapper<>();
//                            LearnRate learnRate = learnRateMapper.selectOne(queryWrapper4.eq("class_id", c.getClassId()).eq("user_id", user_id).eq("chapter_id", ch.getChapterId()));
//                            if (learnRate == null || learnRate.getIfLearned() == 0) {
//                                ifLearned = false;
//                                break;
//                            }
//                        }
//                        if (ifLearned) {
//                            finishNum++;
//                        }
//                    }
//                    branchStatistics.setBranch(b);
//                    branchStatistics.setNum(userList.size());
//                    branchStatistics.setFinishNum(finishNum);
//                    branchStatisticsList.add(branchStatistics);
//                    learnNum += userList.size(); // 计算学习人数
//                    learnFinishedNum += finishNum; // 计算学习完成人数
//                }
//            }
//            LearnRateStatistics learnRateStatistics = new LearnRateStatistics();
//            learnRateStatistics.setLearn_class(c);
//            learnRateStatistics.setBranches(branchStatisticsList);
//            learnRateStatistics.setNum(learnNum);
//            learnRateStatistics.setFinishNum(learnFinishedNum);
//            learnRateStatisticsList.add(learnRateStatistics);
//        }
//        return learnRateStatisticsList;
//    }
    @Override
    public List<LearnRateStatistics> getLearnRateStatistics() {
        // 批量查询所有班级
        List<Class> classList = classMapper.selectList(new QueryWrapper<>());
        // 批量查询所有分支
        List<Branch> branchList = branchMapper.selectList(new QueryWrapper<>());
        // 批量查询所有章节
        // List<Chapter> chapterList = chapterMapper.selectList(new QueryWrapper<>());
        // 批量查询所有用户，避免在循环中多次查询
        List<User> userList = userMapper.selectList(new QueryWrapper<>());
        // 批量查询所有学习进度数据，避免多次查询
        List<LearnRate> learnRateList = learnRateMapper.selectList(new QueryWrapper<>());

        Map<Integer, List<LearnRate>> userLearnRateMap = learnRateList.stream()
                .filter(lr -> lr.getUserId() != null)  // 过滤掉 userId 为空的 LearnRate
                .collect(Collectors.groupingBy(LearnRate::getUserId)); // 按用户ID分组

        Map<Integer, List<User>> branchUserMap = userList.stream()
                .filter(u -> u.getBranchId() != null)  // 过滤掉 branchId 为空的 User
                .collect(Collectors.groupingBy(User::getBranchId)); // 按分支ID分组


        List<LearnRateStatistics> learnRateStatisticsList = new ArrayList<>();
        for (Class c : classList) {
            int learnNum = 0;
            int learnFinishedNum = 0;
            List<BranchStatistics> branchStatisticsList = new ArrayList<>();
            List<Chapter> classChapters = chapterMapper.selectList(new QueryWrapper<Chapter>().eq("class_id", c.getClassId()));
            for (Branch b : branchList) {
                if (Objects.equals(c.getAuthority(), b.getAuthority())) {
                    List<User> branchUsers = branchUserMap.getOrDefault(b.getBranchId(), Collections.emptyList());
                    int finishNum = 0;

                    for (User u : branchUsers) {
                        boolean ifLearned = true;
                        for (Chapter ch : classChapters) {
                            List<LearnRate> userLearnRates = userLearnRateMap.getOrDefault(u.getId(), Collections.emptyList());
                            Optional<LearnRate> learnRate = userLearnRates.stream()
                                    .filter(lr -> Objects.equals(lr.getClassId(), c.getClassId()) && Objects.equals(lr.getChapterId(), ch.getChapterId()))
                                    .findFirst();

                            if (!learnRate.isPresent() || learnRate.get().getIfLearned() == 0) {
                                ifLearned = false;
                                break;
                            }
                        }
                        if (ifLearned) {
                            finishNum++;
                        }
                    }

                    branchStatisticsList.add(new BranchStatistics(b, branchUsers.size(), finishNum));
                    learnNum += branchUsers.size();
                    learnFinishedNum += finishNum;
                }
            }

            learnRateStatisticsList.add(new LearnRateStatistics(c, branchStatisticsList, learnNum, learnFinishedNum));
        }
        return learnRateStatisticsList;
    }

//    @Override
//    public List<LearnRateStatistics> getLearnRateStatisticsByBranchId(int branch_id) {
//        QueryWrapper<Branch> queryWrapper1 = new QueryWrapper<>();
//        queryWrapper1.eq("branch_id", branch_id);
//        List<Branch> branchList = branchMapper.selectList(queryWrapper1);
//        List<LearnRateStatistics> learnRateStatisticsList = new ArrayList<>();
//        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("authority", branchList.get(0).getAuthority());
//        List<Class> classList = classMapper.selectList(queryWrapper);
//        for (Class c : classList) {
//            int class_id = c.getClassId();
//            QueryWrapper<Chapter> queryWrapper2 = new QueryWrapper<>();
//            List<Chapter> chapterList = chapterMapper.selectList(queryWrapper2.eq("class_id", class_id));
//            List<BranchStatistics> branchStatisticsList = new ArrayList<>();
//            int learnNum = 0;
//            int learnFinishedNum = 0;
//            for (Branch b : branchList) {
//                QueryWrapper<User> queryWrapper3 = new QueryWrapper<>();
//                List<User> userList = userMapper.selectList(queryWrapper3.eq("branch_id", b.getBranchId()));
//                BranchStatistics branchStatistics = new BranchStatistics();
//                int finishNum = 0;
//                for (User u : userList) {
//                    int user_id = u.getId();
//                    boolean ifLearned = true;
//                    for (Chapter ch : chapterList) {
//                        QueryWrapper<LearnRate> queryWrapper4 = new QueryWrapper<>();
//                        LearnRate learnRate = learnRateMapper.selectOne(queryWrapper4.eq("class_id", c.getClassId()).eq("user_id", user_id).eq("chapter_id", ch.getChapterId()));
//                        if (learnRate == null || learnRate.getIfLearned() == 0) {
//                            ifLearned = false;
//                            break;
//                        }
//                    }
//                    if (ifLearned) {
//                        finishNum++;
//                    }
//                }
//                branchStatistics.setBranch(b);
//                branchStatistics.setNum(userList.size());
//                branchStatistics.setFinishNum(finishNum);
//                branchStatisticsList.add(branchStatistics);
//                learnNum += userList.size(); // 计算学习人数
//                learnFinishedNum += finishNum; // 计算学习完成人数
//            }
//            LearnRateStatistics learnRateStatistics = new LearnRateStatistics();
//            learnRateStatistics.setLearn_class(c);
//            learnRateStatistics.setBranches(branchStatisticsList);
//            learnRateStatistics.setNum(learnNum);
//            learnRateStatistics.setFinishNum(learnFinishedNum);
//            learnRateStatisticsList.add(learnRateStatistics);
//        }
//        return learnRateStatisticsList;
//    }

    @Override
    public List<LearnRateStatistics> getLearnRateStatisticsByBranchId(int branch_id) {
        // 批量查询所有分支
        QueryWrapper<Branch> branchQuery = new QueryWrapper<>();
        branchQuery.eq("branch_id", branch_id);
        List<Branch> branchList = branchMapper.selectList(branchQuery);
        if (branchList.isEmpty()) {
            return Collections.emptyList(); // 如果没有找到对应的分支，返回空列表
        }

        // 获取分支的 authority
        String authority = String.valueOf(branchList.get(0).getAuthority());

        // 批量查询所有班级，这里假设authority在Class表中是唯一的
        QueryWrapper<Class> classQuery = new QueryWrapper<>();
        classQuery.eq("authority", authority);
        List<Class> classList = classMapper.selectList(classQuery);

        // 批量查询所有章节
        Map<Integer, List<Chapter>> classChaptersMap = new HashMap<>();
        for (Class c : classList) {
            QueryWrapper<Chapter> chapterQuery = new QueryWrapper<>();
            chapterQuery.eq("class_id", c.getClassId());
            List<Chapter> chapterList = chapterMapper.selectList(chapterQuery);
            classChaptersMap.put(c.getClassId(), chapterList);
        }

        // 批量查询所有用户
        Map<Integer, List<User>> branchUserMap = new HashMap<>();
        for (Branch b : branchList) {
            QueryWrapper<User> userQuery = new QueryWrapper<>();
            userQuery.eq("branch_id", b.getBranchId());
            List<User> userList = userMapper.selectList(userQuery);
            branchUserMap.put(b.getBranchId(), userList);
        }

        // 批量查询所有学习进度
        List<LearnRate> learnRateList = learnRateMapper.selectList(new QueryWrapper<>());

        // 将学习进度按 userId 和 classId 分组，便于快速查找
        Map<Integer, Map<Integer, LearnRate>> userLearnRateMap = new HashMap<>();
        for (LearnRate lr : learnRateList) {
            userLearnRateMap
                    .computeIfAbsent(lr.getUserId(), k -> new HashMap<>())
                    .put(lr.getClassId(), lr);
        }

        List<LearnRateStatistics> learnRateStatisticsList = new ArrayList<>();

        // 遍历所有班级计算学习情况
        for (Class c : classList) {
            int class_id = c.getClassId();
            List<Chapter> chapterList = classChaptersMap.get(class_id);

            List<BranchStatistics> branchStatisticsList = new ArrayList<>();
            int learnNum = 0;
            int learnFinishedNum = 0;

            for (Branch b : branchList) {
                List<User> userList = branchUserMap.get(b.getBranchId());
                int finishNum = 0;

                for (User u : userList) {
                    boolean ifLearned = true;

                    // 判断该用户是否学习了所有章节
                    for (Chapter ch : chapterList) {
                        LearnRate learnRate = userLearnRateMap
                                .getOrDefault(u.getId(), Collections.emptyMap())
                                .get(ch.getClassId());

                        if (learnRate == null || learnRate.getIfLearned() == 0) {
                            ifLearned = false;
                            break;
                        }
                    }

                    if (ifLearned) {
                        finishNum++;
                    }
                }

                BranchStatistics branchStatistics = new BranchStatistics();
                branchStatistics.setBranch(b);
                branchStatistics.setNum(userList.size());
                branchStatistics.setFinishNum(finishNum);
                branchStatisticsList.add(branchStatistics);

                learnNum += userList.size();
                learnFinishedNum += finishNum;
            }

            LearnRateStatistics learnRateStatistics = new LearnRateStatistics();
            learnRateStatistics.setLearn_class(c);
            learnRateStatistics.setBranches(branchStatisticsList);
            learnRateStatistics.setNum(learnNum);
            learnRateStatistics.setFinishNum(learnFinishedNum);

            learnRateStatisticsList.add(learnRateStatistics);
        }

        return learnRateStatisticsList;
    }


    @Override
    public LearnRateBranchStatistics getLearnRateBranchStatistics(int class_id, int branch_id) {
        QueryWrapper<Class> classQueryWrapper = new QueryWrapper<>();
        Class c = classMapper.selectOne(classQueryWrapper.eq("class_id", class_id));
        QueryWrapper<Branch> branchQueryWrapper = new QueryWrapper<>();
        Branch b = branchMapper.selectOne(branchQueryWrapper.eq("branch_id", branch_id));
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        List<Chapter> chapterList = chapterMapper.selectList(chapterQueryWrapper.eq("class_id", class_id));
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(userQueryWrapper.eq("branch_id", branch_id));
        List<UserStatistics> userStatisticsList = new ArrayList<>();
        int finishNum = 0;
        for (User user : userList) {
            UserStatistics userStatistics = new UserStatistics();
            boolean ifLearned = true;
            for (Chapter ch : chapterList) {
                QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
                LearnRate learnRate = learnRateMapper.selectOne(queryWrapper.eq("class_id", c.getClassId()).eq("user_id", user.getId()).eq("chapter_id", ch.getChapterId()));
                if (learnRate == null || learnRate.getIfLearned() == 0) {
                    ifLearned = false;
                    break;
                }
            }
            if (ifLearned) {
                finishNum++;
            }
            QueryWrapper<ClassLearnInfo> classLearnInfoQueryWrapper = new QueryWrapper<>();
            ClassLearnInfo classLearnInfo = classLearnInfoMapper.selectOne(classLearnInfoQueryWrapper.eq("class_id", class_id).eq("user_id", user.getId()));
            if (classLearnInfo != null){
                userStatistics.setDuration(classLearnInfo.getLearningDuration());
            }
            userStatistics.setIfLearned(ifLearned);
            userStatistics.setUser(user);
            userStatisticsList.add(userStatistics);
        }
        LearnRateBranchStatistics learnRateBranchStatistics = new LearnRateBranchStatistics();
        learnRateBranchStatistics.setLearn_class(c);
        learnRateBranchStatistics.setBranch(b);
        learnRateBranchStatistics.setUserStatistics(userStatisticsList);
        learnRateBranchStatistics.setNum(userList.size());
        learnRateBranchStatistics.setFinishNum(finishNum);
        return learnRateBranchStatistics;
    }

    @Override
    public UserLearnDetail getUserLearnDetail(int class_id, int user_id) {
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        List<Chapter> chapterList = chapterMapper.selectList(chapterQueryWrapper.eq("class_id", class_id));
        List<Boolean> chapterIfLearnedList = new ArrayList<>();
        int finishNum = 0;
        for (Chapter ch : chapterList) {
            QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
            LearnRate learnRate = learnRateMapper.selectOne(queryWrapper.eq("class_id", class_id).eq("user_id", user_id).eq("chapter_id", ch.getChapterId()));
            if (learnRate != null && learnRate.getIfLearned() == 1) {
                finishNum++;
                chapterIfLearnedList.add(true);
            }
            else {
                chapterIfLearnedList.add(false);
            }
        }
        UserLearnDetail userLearnDetail = new UserLearnDetail();
        userLearnDetail.setChapterList(chapterList);
        userLearnDetail.setChapterIfLearnedList(chapterIfLearnedList);
        userLearnDetail.setFinishNum(finishNum);
        userLearnDetail.setNum(chapterList.size());
        return userLearnDetail;
    }


    }


