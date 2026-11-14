package com.example.spb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.spb.entity.po.*;
import com.example.spb.entity.po.Class;
import com.example.spb.entity.vo.ClassAndChapter;
import com.example.spb.mapper.ChapterMapper;
import com.example.spb.mapper.ClassLearnInfoMapper;
import com.example.spb.mapper.ClassMapper;
import com.example.spb.mapper.UserMapper;
import com.example.spb.service.ClassLearnInfoService;
import com.example.spb.service.ClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spb.service.LearnRateService;
import com.example.spb.utils.AliOSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.Cacheable;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 课程表 服务实现类
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {
    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Autowired
    private ClassLearnInfoService classLearnInfoService;

    @Autowired
    private LearnRateService learnRateService;

    @Autowired
    private ChapterMapper chapterMapper;

    @Override
    public Integer addClass(int founder_id, String founder, String class_title, String department, int authority, MultipartFile class_portrait) throws IOException {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_title", class_title);
        List<Class> resultList = classMapper.selectList(queryWrapper);
        // 判断结果列表是否为空
        if (resultList == null || resultList.isEmpty()) {
            Class class_  = new Class();
            class_.setFounderId(founder_id);
            class_.setFounder(founder);
            class_.setClassTitle(class_title);
            class_.setDepartment(department);
            class_.setUploadTime(LocalDateTime.now());
            class_.setAuthority(authority);
            if (class_portrait != null){
                String class_portrait_url = aliOSSUtils.upload(class_portrait);
                class_.setClassPortrait(class_portrait_url);
            }
            // 创建新课程
            classMapper.insert(class_);
            return class_.getClassId();
        } else {
            return -1;
        }
    }

    //获取全部课程
    @Override
    public List<Class> getClasses() {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        return classMapper.selectList(queryWrapper);
    }

    @Override
    public List<ClassAndChapter> getClassesAndChapter() {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        List<Class> classes = classMapper.selectList(queryWrapper);
        List<ClassAndChapter> classAndChapters = new ArrayList<>();
        for (Class class_ : classes) {
            ClassAndChapter classAndChapter = new ClassAndChapter();
            classAndChapter.setAClass(class_);
            QueryWrapper<Chapter> queryWrapper1 = new QueryWrapper<>();
            List<Chapter> characters = chapterMapper.selectList(queryWrapper1.eq("class_id", class_.getClassId()));
            classAndChapter.setChapterList(characters);
            classAndChapters.add(classAndChapter);
        }
        return classAndChapters;
    }

    // 根据id获取单个课程
    @Override
    public Class getClassById(int class_id) {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        return classMapper.selectOne(queryWrapper.eq("class_id", class_id));
    }

    // 删除课程
    // todo 后续根据课程外键修改删除
    @Override
    // 事务回滚
    @Transactional
    public String deleteClass(int class_id) {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        String url = classMapper.selectOne(queryWrapper.eq("class_id", class_id)).getClassPortrait();
        // 删除课程前 先删除OSS阿里云存储中的图片
        if (url != null && !Objects.equals(url, "")){
            aliOSSUtils.deleteFile(url);
        }
        classLearnInfoService.deleteByClassId(class_id);
        learnRateService.deleteByClassId(class_id);
        classMapper.delete(queryWrapper.eq("class_id", class_id));
        return "删除课程成功";
    }

    @Override
    public String getNameByClassId(Integer classId) {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id",classId);
        return classMapper.selectOne(queryWrapper).getClassTitle();
    }

    @Override
    public List<String> getPartyClassTitle(int authority) {
        QueryWrapper<Class> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("authority",authority);
        List<Class> classList=  classMapper.selectList(queryWrapper);
        List<String> resList = new ArrayList<>();
        for (Class cla : classList){
            resList.add(cla.getClassTitle());
        }
        return resList;
    }

    @Override
    public String changeClassTime(LocalDateTime dateTime, int class_id){
        UpdateWrapper<Class> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("class_id",class_id).set("upload_time",dateTime);
        int res = classMapper.update(null,updateWrapper);
        if (res!=0){
            return "更新课程时间成功！";
        }else {
            return "更新课程时间失败！";
        }
    }

}
