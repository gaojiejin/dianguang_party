package com.example.spb.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.spb.constants.OssConstants;
import com.example.spb.entity.po.Chapter;
import com.example.spb.entity.po.Class;
import com.example.spb.entity.po.ClassLearnInfo;
import com.example.spb.entity.po.LearnRate;
import com.example.spb.entity.po.User;
import com.example.spb.entity.vo.UserLearnInfo;
import com.example.spb.mapper.*;
import com.example.spb.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spb.utils.AliOSSUtils;
import com.example.spb.utils.DateUtils;
import com.example.spb.utils.NumToStringUtil;
import com.example.spb.utils.PdfGenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程学习信息表 服务实现类
 * </p>
 *
 * @author gjj
 * @since 2024-09-12
 */
@Slf4j
@Service
public class ClassLearnInfoServiceImpl extends ServiceImpl<ClassLearnInfoMapper, ClassLearnInfo> implements ClassLearnInfoService {
    @Autowired
    LearnRateMapper learnRateMapper;
    @Autowired
    ChapterMapper chapterMapper;
    @Autowired
    ClassLearnInfoMapper classLearnInfoMapper;
    @Autowired
    ClassMapper classMapper;
    @Autowired
    UserMapper userMapper;

    @Autowired
    @Lazy
    ClassService classService;

    @Autowired
    AliOSSUtils aliOSSUtils;
    @Autowired
    BranchService branchService;
    @Autowired
    UserService userService;
    @Autowired
    LearnRateService learnRateService;
    @Autowired
    ChapterService chapterService;
    @Autowired
    PdfGenerateUtil pdfGenerateUtil;



    @Override
    public String exportAllLearnStats(List<String> branchList) {
        // 创建一个Excel工作簿
        Workbook workbook = new XSSFWorkbook();
        List<String> partyClassNameList;
        List<String> activistClassNameList;
        partyClassNameList = classService.getPartyClassTitle(0);
        activistClassNameList = classService.getPartyClassTitle(1);
        int index = 0;

        try {
            List<String> classList;
            // 为每个branch创建一个sheet
            for (String branch : branchList) {
                if (branch.equals("积极分子部门")){
                    classList = activistClassNameList;
                }else{
                    classList = partyClassNameList;
                }
                Sheet sheet = workbook.createSheet(branch);
                Row headerRow = sheet.createRow(0);
                // 第零列为空
                headerRow.createCell(0);


                Cell firstCell = headerRow.createCell(0);
                firstCell.setCellValue("姓名");
                // 填充第一到第X列为classList的元素
                for (int i = 0; i < classList.size(); i++) {
                    Cell cell = headerRow.createCell(i + 1); // 列从1开始
                    cell.setCellValue(classList.get(i));
                }
                // 填充第X+1列为"学习时长"
                Cell lastCell = headerRow.createCell(classList.size() + 1);
                lastCell.setCellValue("年度学习时长");

                List<User> userList = branchService.getUsersByBranchID(branchService.queryBranchIDByBranchName(branch));
                for (int i = 0;i < userList.size() ;i++){
                    User user = userList.get(i);
                    UserLearnInfo userLearnInfo = this.getOwnLearnInfo(user.getId());
                    Row row = sheet.createRow(i+1);
                    Cell cell = row.createCell(0); // 列从1开始
                    cell.setCellValue(user.getName());

                    for (int j = 0;j < classList.size();j++){
                        if (userLearnInfo.getClassLearned().contains(classList.get(j))){
                            Cell cell1 = row.createCell(j+1);
                            cell1.setCellValue("已完成");
                        }else {
                            Cell cell1 = row.createCell(j+1);

                            // 创建一个 CellStyle 用于设置单元格样式
                            CellStyle style = workbook.createCellStyle();
                            // 创建一个 Font 对象用于设置字体样式
                            Font font = workbook.createFont();
                            font.setColor(IndexedColors.RED.getIndex());  // 设置字体颜色为红色
                            // 将字体应用到样式上
                            style.setFont(font);
                            // 将样式应用到单元格
                            cell1.setCellStyle(style);

                            cell1.setCellValue("未完成");
                        }
                    }
                    Cell cell2 = row.createCell(classList.size()+1); // 列从1开始
                    cell2.setCellValue(userLearnInfo.getAnnualDuration());
                }
                index++;
            }

            // 输出Excel文件路径
//            String filePath = "src/main/resources/static/xlsx/learn_stats.xlsx";
//            FileOutputStream outputStream = new FileOutputStream(filePath);
//
//            // 将workbook写入输出流
//            workbook.write(outputStream);
//            // 关闭输出流和workbook
//            outputStream.close();
//            workbook.close();

            String res = aliOSSUtils.uploadWorkbook(workbook);

            // 返回文件路径
            return res;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String addLearnDuration(int classId, int chapterId, int userId, int if_learned,int learnDuration) {
//        获取视频总长
        QueryWrapper<Chapter> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("class_id",classId).eq("chapter_id",chapterId);
        Chapter curChapter = chapterMapper.selectOne(queryWrapper2);
        int curChapterDuration = curChapter.getDuration();
//        定义学习时长增加值
        int classDurationInc = 0;
        //初始设置为1，如果有没学完，就设置为0
        int tag = 1;
        int count = 0;
        int isAllLearned = 0;
//        <----------------------------------------------------------------------------------------->
//        查询有没有该章节的学习记录
        QueryWrapper<LearnRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id",classId).eq("chapter_id",chapterId).eq("user_id",userId);
        LearnRate learnRate = learnRateMapper.selectOne(queryWrapper);
//        没有则新建`
        if (learnRate == null){
            LearnRate newLearnRate = new LearnRate();
            newLearnRate.setClassId(classId);
            newLearnRate.setChapterId(chapterId);
            newLearnRate.setUserId(userId);
            newLearnRate.setIfLearned(if_learned);
            newLearnRate.setLearningDuration(0);
            newLearnRate.setLearn_date(DateTime.now());
            learnRateMapper.insert(newLearnRate);

            learnRate = newLearnRate;
        }
//        <----------------------------------------------------------------------------------------->
//          再看有没有该课程的学习记录，没有则新建
            this.createClassLearnInfo(classId, userId, 0, 0);
//        <----------------------------------------------------------------------------------------->
//          此时已有章节和课程记录，计算本次学习时长增加值
            if (curChapterDuration >= learnDuration + learnRate.getLearningDuration()) {
                classDurationInc = learnDuration;
                learnDuration = learnDuration + learnRate.getLearningDuration();
            } else {
                learnDuration = curChapterDuration;
                classDurationInc = curChapterDuration - learnRate.getLearningDuration();
            }

//          更新章节学习情况
            UpdateWrapper<LearnRate> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("class_id", classId).eq("chapter_id", chapterId).eq("user_id", userId).set("learning_duration", learnDuration).set("ifLearned", if_learned).set("learn_date", LocalDateTime.now());
            learnRateMapper.update(null, updateWrapper);

//          更新课程学习情况，只有在学完一个章节时，才进行判断更新
            if (if_learned == 1){

                QueryWrapper<Chapter> queryWrapper0 = new QueryWrapper<>();
                queryWrapper0.eq("class_id",classId);
                List<Chapter> chapterList = chapterMapper.selectList(queryWrapper0);

                QueryWrapper<LearnRate> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("user_id",userId).eq("class_id",classId);
                List<LearnRate> resList = learnRateMapper.selectList(queryWrapper1);
                for (LearnRate res :resList){
                    if (res.getIfLearned() == 0)
                        tag=0;
                    else count++;
                }
                if (count == chapterList.size())
                isAllLearned = tag==0 ? 0:1;
            }
            this.updateClassLearnDuration(classId, userId, isAllLearned, classDurationInc);
            log.info("--------课程学习时长更新--------");


        return "更新了章节学习时长";
    }

    @Override
    public String changeLearnDuration(int classId,int userId, int if_learned,int learn_duration) {
        UpdateWrapper<ClassLearnInfo>   updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("class_id",classId).eq("user_id",userId).set("if_learned",if_learned).set("learning_duration",learn_duration);
        int updatedRows = classLearnInfoMapper.update(null,updateWrapper);
        // 检查是否有classlearninfo
        if (updatedRows == 0) {
//            若没有，则新建
            ClassLearnInfo classLearnInfo = new ClassLearnInfo();
            classLearnInfo.setClassId(classId);
            classLearnInfo.setIfLearned(if_learned);
            classLearnInfo.setUserId(userId);
            classLearnInfo.setLearningDuration(learn_duration);
            classLearnInfoMapper.insert(classLearnInfo);
        }
        QueryWrapper<Chapter> queryWrapper0 = new QueryWrapper<>();
        queryWrapper0.eq("class_id",classId);
        List<Chapter> chapterList = chapterMapper.selectList(queryWrapper0);

        QueryWrapper<LearnRate> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("class_id",classId).eq("user_id",userId);
        List<LearnRate> learnRates = learnRateMapper.selectList(queryWrapper1);
        List<Integer> chapterIdList = new ArrayList<>();
//        若没有先新建学习记录
        for (LearnRate learnRate:learnRates){
            chapterIdList.add(learnRate.getChapterId());
        }
        for (Chapter chapter:chapterList){
            if (chapterIdList.contains(chapter.getChapterId())){
//                do nothing
            }else {
                LearnRate newLearnRate = new LearnRate();
                newLearnRate.setClassId(classId);
                newLearnRate.setChapterId(chapter.getChapterId());
                newLearnRate.setUserId(userId);
                newLearnRate.setIfLearned(if_learned);
                newLearnRate.setLearningDuration(0);
                newLearnRate.setLearn_date(DateTime.now());
                learnRateMapper.insert(newLearnRate);
                learnRates.add(0,newLearnRate);
            }
        }

        Random random = new Random();
        int remainingDuration = learn_duration; // 剩余的学习总时长
        int distributeDuration = 0;
            for (int i = 0;i<learnRates.size();i++){
                if (if_learned == 1){
                    learnRates.get(i).setIfLearned(1);
                }
                Chapter curChapter = chapterService.getChapterByChapterId(learnRates.get(i).getChapterId());
                if (i == learnRates.size()-1){
                    distributeDuration = remainingDuration;
                    learnRates.get(i).setLearningDuration(distributeDuration);
                    learnRates.get(i).setIfLearned(if_learned);
                    learnRateMapper.updateById(learnRates.get(i));
                    break;
                }else {
                    distributeDuration = curChapter.getDuration()>remainingDuration?remainingDuration:curChapter.getDuration();
                    learnRates.get(i).setLearningDuration(distributeDuration);
                    remainingDuration = remainingDuration-distributeDuration;
                    learnRateMapper.updateById(learnRates.get(i));
                }
            }

        return "修改用户" + userId + "的学习信息";
    }

    @Override
    public String changeLearnDurationByBranch(int classId, int branch_id, int if_learned, int min_duration, int max_duration ) {
        int learnDuration = 0;
        List<User> users = branchService.queryBranchMember(branch_id);
        for (User user :users){
            Random random = new Random();
            if (max_duration == min_duration){
                learnDuration=max_duration;
            }
            else {
                learnDuration= random.nextInt(max_duration - min_duration) + min_duration;
            }
            this.changeLearnDuration(classId,user.getId(),if_learned,learnDuration);
        }
        return "修改成功";
    }


    @Override
    public String updateClassLearnDuration(int classId,int userId,int if_learned,int classDurationInc){
        UpdateWrapper<ClassLearnInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("class_id",classId)
                .eq("user_id",userId)
                .set("if_learned",if_learned)
                .setSql("learning_duration = learning_duration + " + classDurationInc);
        classLearnInfoMapper.update(null,updateWrapper);
        log.info("--------课程学习时长更新--------");
        return "更新了课程学习时长";
    }

    @Override
    public String createClassLearnInfo(int classId, int userId ,int if_learned,int learnDuration) {
        QueryWrapper<ClassLearnInfo>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId).eq("class_id",classId);
        ClassLearnInfo res = classLearnInfoMapper.selectOne(queryWrapper);
        if (res == null){
            ClassLearnInfo classLearnInfo = new ClassLearnInfo();
            classLearnInfo.setUserId(userId);
            classLearnInfo.setIfLearned(if_learned);
            classLearnInfo.setLearningDuration(learnDuration);
            classLearnInfo.setClassId(classId);
            classLearnInfoMapper.insert(classLearnInfo);
        }
        return "课程学习信息添加成功";

    }
    @Override
    public UserLearnInfo getOwnLearnInfo(int userId) {

        UserLearnInfo userLearnInfo = new UserLearnInfo();
        List<String> classLearned = new ArrayList<>();
        int seconds = 0;
        List<ClassLearnInfo> classIdLearned ;
        User curUser = userService.getById(userId);

        //      个人应修的全部课程：对应党员/积极分子
        QueryWrapper<Class> queryWrapperClass = new QueryWrapper<>();
        queryWrapperClass.eq("authority",curUser.getRole().equals("积极分子")?1:0);
        List<Class> allClasses = classMapper.selectList(queryWrapperClass);
        int should_learned = allClasses.size();

        // 查询所有在学习中的课程学习记录,看已经完成的课classIdLearned
        QueryWrapper<ClassLearnInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        List<ClassLearnInfo> allLearnedClasses = classLearnInfoMapper.selectList(queryWrapper);
        classIdLearned = allLearnedClasses.stream()
                .filter(info -> info.getIfLearned() == 1)
                .collect(Collectors.toList());

        int alreadyLearned = classIdLearned.size();
        for (ClassLearnInfo classLearnInfo :allLearnedClasses){
            String className = classService.getNameByClassId(classLearnInfo.getClassId());
            if (classLearnInfo.getIfLearned() == 1) classLearned.add(className);
            seconds += classLearnInfo.getLearningDuration();
        }
        userLearnInfo.setClassLearned(classLearned);
        userLearnInfo.setShouldLearned(should_learned);
        userLearnInfo.setAlreadyLearned(alreadyLearned);
        String annual_duration = NumToStringUtil.convertNumToDate(seconds);
        userLearnInfo.setAnnualDuration(annual_duration);

        return userLearnInfo;

}
//新建章节后，必须把课程设定为未学完
    @Override
    public void judgeIsLearned(int classId) {
        UpdateWrapper<ClassLearnInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("class_id",classId).set("if_learned",0);
        classLearnInfoMapper.update(null,updateWrapper);
    }

    @Override
    public String exportLearnStatsByClass(int classId) {
        return null;
    }

    @Override
    public String exportCertificate(String job_id,String start_time, String completion_time, String export_time) throws IOException {
//         判断是否学完了
        User user = userService.findByJobID(job_id);
        String user_name = user.getName();
        UserLearnInfo ownLearnInfo = this.getOwnLearnInfo(user.getId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date completionDate = null;
        Date exportDate = null;
        try {
            startDate = dateFormat.parse(start_time);
            completionDate = dateFormat.parse(completion_time);
            exportDate = dateFormat.parse(export_time);
        } catch (ParseException e) {
            return "完成时间格式错误";
        }
        // 处理开始时间（startDate）
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        int startYear = startCalendar.get(Calendar.YEAR);
        int startMonth = startCalendar.get(Calendar.MONTH) + 1;  // 月份从0开始，所以要加1
        int startDay = startCalendar.get(Calendar.DAY_OF_MONTH) + 1;
        // 处理完成时间（completionDate）
        Calendar completionCalendar = Calendar.getInstance();
        completionCalendar.setTime(completionDate);
        int completionYear = completionCalendar.get(Calendar.YEAR);
        int completionMonth = completionCalendar.get(Calendar.MONTH) + 1;  // 月份从0开始，所以要加1
        int completionDay = completionCalendar.get(Calendar.DAY_OF_MONTH) + 1;

        // 处理导出时间（exportDate）
        Calendar exportCalendar = Calendar.getInstance();
        exportCalendar.setTime(exportDate);
        int exportYear = exportCalendar.get(Calendar.YEAR);
        int exportMonth = exportCalendar.get(Calendar.MONTH) + 1;  // 月份从0开始，所以要加1
        int exportDay = exportCalendar.get(Calendar.DAY_OF_MONTH) + 1;



        if (ownLearnInfo.getAlreadyLearned() != ownLearnInfo.getShouldLearned()){
            return "你尚未完成全部课程学习，不能导出";
        }
//        File res = pdfGenerateUtil.designer(user_name,job_id);
        File res = pdfGenerateUtil.fillTemplate(
                new ClassPathResource(OssConstants.LEARNED_MOBAN_PATH).getFile().getAbsolutePath(), // 模板路径
                user_name,                       // 姓名
                job_id,                          // 学号
                String.valueOf(startYear),
                String.valueOf(startMonth),
                String.valueOf(startDay),
                String.valueOf(completionYear),
                String.valueOf(completionMonth),
                String.valueOf(completionDay),
                String.valueOf(exportYear),
                String.valueOf(exportMonth),
                String.valueOf(exportDay)
                );                               // 日期
        return aliOSSUtils.upload(res);

    }

//    @Override
//    public String exportPartyCertificate(String job_id) throws IOException {
////         判断是否学完了
//        User user = userService.findByJobID(job_id);
//        String user_name = user.getName();
//        UserLearnInfo ownLearnInfo = this.getOwnLearnInfo(user.getId());
//        if (ownLearnInfo.getAlreadyLearned() != ownLearnInfo.getShouldLearned()){
//            return "你尚未完成全部课程学习，不能导出";
//        }
////        File res = pdfGenerateUtil.designer(user_name,job_id);
//        File res = pdfGenerateUtil.fillTemplate(
//                new ClassPathResource(OssConstants.PARTY_LEARNED_MOBAN_PATH).getFile().getAbsolutePath(), // 模板路径
//                user_name,                       // 姓名
//                job_id,                          // 学号
//                DateUtils.getCurrentYear(),
//                DateUtils.getCurrentMonth(),
//                DateUtils.getCurrentDay(),
//                DateUtils.getCurrentYear(),
//                DateUtils.getCurrentMonth(),
//                DateUtils.getCurrentDay()
//        );                               // 日期
//        return aliOSSUtils.upload(res);
//
//    }
//    @Transactional
    @Override
    public String finishTrain(List<Integer> usersId) {
        for (int userId : usersId){
            UserLearnInfo ownLearnInfo = this.getOwnLearnInfo(userId);
            if (ownLearnInfo.getAlreadyLearned() != ownLearnInfo.getShouldLearned()){
                return "选择中包含了中未完成培训的积极分子！";
            }
            userService.deleteUserById(userId);
            this.deleteAllClassLearnInfo(usersId);
            learnRateService.deleteAllLearnRate(usersId);
        }
        return "已完成结业，积极分子账号已删除";
    }

//    @Transactional
    @Override
    public String deleteAllClassLearnInfo(List<Integer> users_id) {

        for (Integer user_id:users_id){
            QueryWrapper<ClassLearnInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", user_id);
            List<ClassLearnInfo> classLearnInfoList = classLearnInfoMapper.selectList(queryWrapper);
            // 遍历删除列表中的所有 classLearnInfo
            for (ClassLearnInfo classLearnInfo: classLearnInfoList) {
                classLearnInfoMapper.deleteById(classLearnInfo.getId());
            }
        }
        return "删除学习进度成功";
    }

    @Override
    public int deleteByClassId(int classId) {
        QueryWrapper<ClassLearnInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", classId);
        int res = classLearnInfoMapper.delete(queryWrapper);
        return res;
    }

    @Override
    public String deleteCertificate(String certificatePath) {
        aliOSSUtils.deleteFile(certificatePath);
        return "成功删除了培训证明";
    }

    public List<User> getRoleLearnedList(String role){
        QueryWrapper<User> queryWrapper = new QueryWrapper();
        queryWrapper.eq("role",role);
        List<User> activistList = userMapper.selectList(queryWrapper);
        List<User> resList = new ArrayList<>();
        for (User activist:activistList){
            UserLearnInfo userLearnInfo = this.getOwnLearnInfo(activist.getId());
            if (userLearnInfo.getAlreadyLearned() == userLearnInfo.getShouldLearned()){
                resList.add(activist);
            }
        }
        return resList;
    }


}
