package com.example.spb.service;

import com.example.spb.entity.po.Chapter;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 章节表 服务类
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */
public interface ChapterService extends IService<Chapter> {
    Integer addChapter(int class_id, int founder_id, String founder,  String title, int type, String chapter_portrait, String content, List<String> chapterImage, String chapterVideo, int duration) throws IOException;

    List<Chapter> getAllChapter();

    List<Chapter> getChapterByClassId(int class_id);

    Chapter getChapterByChapterId(int chapter_id);

    String deleteChapter(int chapter_id);

    Integer getClassDuration(int classId);

    Boolean checkChapterName(String title, int class_id);

    String changeChapterTime(LocalDateTime dateTime, int chapter_id);
}
