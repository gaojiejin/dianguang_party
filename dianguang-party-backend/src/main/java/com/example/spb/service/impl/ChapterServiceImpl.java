package com.example.spb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.spb.entity.po.Chapter;
import com.example.spb.entity.po.Class;
import com.example.spb.entity.po.LearnRate;
import com.example.spb.mapper.ChapterMapper;
import com.example.spb.mapper.LearnRateMapper;
import com.example.spb.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.spb.service.ClassLearnInfoService;
import com.example.spb.service.ClassService;
import com.example.spb.utils.AliOSSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 章节表 服务实现类
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
@Service
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private LearnRateMapper learnRateMapper;

    @Autowired
    private AliOSSUtils aliOSSUtils;

    @Override
    public Integer addChapter(int class_id, int founder_id, String founder, String title, int type, String chapter_portrait, String content, List<String> chapterImage, String chapterVideo, int duration) throws IOException {
//        classLearnInfoService.judgeIsLearned(class_id);
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title).eq("class_id", class_id);
        List<Chapter> resultList = chapterMapper.selectList(queryWrapper);

        // 判断结果列表是否为空
        if (resultList == null || resultList.isEmpty()) {
            Chapter chapter = new Chapter();
            chapter.setClassId(class_id);
            chapter.setFounderId(founder_id);
            chapter.setFounder(founder);
            chapter.setTitle(title);
            chapter.setType(type);
            chapter.setUploadTime(LocalDateTime.now());
            chapter.setContent(content);
            if (chapterImage != null) {
                StringBuilder imagesPathBuilder = new StringBuilder();
                for (String imagePath : chapterImage){
                    imagesPathBuilder.append(imagePath).append(",");
                }
                chapter.setChapterImage(imagesPathBuilder.toString());
            }
            if (chapterVideo != null) {
                chapter.setChapterVideo(chapterVideo);
                chapter.setDuration(duration);
            }else {
                chapter.setDuration(300);
            }
            if (chapter_portrait != null) {
                chapter.setChapterPortrait(chapter_portrait);
            }
            chapterMapper.insert(chapter);

            return chapter.getChapterId();
        } else {
            if (chapter_portrait != null && !chapter_portrait.equals("null")){
                // 删除课程前 先删除OSS阿里云头像
                aliOSSUtils.deleteFile(chapter_portrait);
            }

            if (chapterVideo != null && !chapterVideo.equals("null")){
                // 删除课程前 先删除OSS阿里云存储中的视频
                aliOSSUtils.deleteFile(chapterVideo);
            }
            if (chapterImage != null){
                for (String imagePath : chapterImage){
                    aliOSSUtils.deleteFile(imagePath);
                }
            }
            return -1;
        }
    }

    @Override
    public Boolean checkChapterName(String title, int class_id) {
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", title).eq("class_id", class_id);
        List<Chapter> resultList = chapterMapper.selectList(queryWrapper);
        return resultList == null || resultList.isEmpty();
    }

    @Override
    public List<Chapter> getAllChapter() {
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        return chapterMapper.selectList(queryWrapper);
    }

    @Override
    public List<Chapter> getChapterByClassId(int class_id) {
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        return chapterMapper.selectList(queryWrapper.eq("class_id", class_id));
    }

    @Override
    public Chapter getChapterByChapterId(int chapter_id) {
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        return chapterMapper.selectOne(queryWrapper.eq("chapter_id", chapter_id));
    }

    @Override
    @Transactional
    public String deleteChapter(int chapter_id) {
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        Chapter chapter = chapterMapper.selectOne(queryWrapper.eq("chapter_id", chapter_id));

        QueryWrapper<LearnRate> queryWrapper1 = new QueryWrapper<>();
        List<LearnRate> learnRates = learnRateMapper.selectList(queryWrapper1.eq("chapter_id", chapter_id));
        if (learnRates != null){
            learnRateMapper.delete(queryWrapper1.eq("chapter_id", chapter_id));
        }

        String url = chapter.getChapterPortrait();
        if (url != null && !Objects.equals(url, "")){
            // 删除课程前 先删除OSS阿里云头像
            aliOSSUtils.deleteFile(url);
        }

        String videoUrl = chapter.getChapterVideo();
        if (videoUrl != null && !Objects.equals(videoUrl, "")){
            // 删除课程前 先删除OSS阿里云存储中的视频
            aliOSSUtils.deleteFile(videoUrl);
        }
        String imgsUrl = chapter.getChapterImage();
        if (imgsUrl != null && !Objects.equals(imgsUrl, "")){
            String[] imgPaths = imgsUrl.split(",");
            for (String imgPath : imgPaths){
                if (!Objects.equals(imgPath, " "))
                    aliOSSUtils.deleteFile(imgPath);
            }
        }

        chapterMapper.delete(queryWrapper.eq("chapter_id", chapter_id));
        return "删除章节成功";
    }

    @Override
    public Integer getClassDuration(int classId) {
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id",classId);
        List<Chapter> chapterList = chapterMapper.selectList(queryWrapper);
        int res = 0;
        for (Chapter chapter :chapterList){
            res+=chapter.getDuration();
        }
        return res;
    }

    @Override
    public String changeChapterTime(LocalDateTime dateTime, int chapter_id){
        UpdateWrapper<Chapter> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("chapter_id",chapter_id).set("upload_time",dateTime);
        int res = chapterMapper.update(null,updateWrapper);
        if (res!=0){
            return "更新章节时间成功！";
        }else {
            return "更新章节时间失败！";
        }
    }
}
