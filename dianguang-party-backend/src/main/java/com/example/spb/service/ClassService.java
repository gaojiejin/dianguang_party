package com.example.spb.service;

import com.example.spb.entity.po.Class;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.spb.entity.vo.ClassAndChapter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 课程表 服务类
 * </p>
 *
 * @author gjj
 * @since 2024-07-24
 */

public interface ClassService extends IService<Class> {
    Integer addClass(int founder_id, String founder, String class_title, String department, int authority, MultipartFile class_portrait) throws IOException;

    List<Class> getClasses();

    List<ClassAndChapter> getClassesAndChapter();

    Class getClassById(int class_id);

    String deleteClass(int class_id);


    String getNameByClassId(Integer classId);

    List<String> getPartyClassTitle(int authority);

    String changeClassTime(LocalDateTime dateTime, int class_id);
}
