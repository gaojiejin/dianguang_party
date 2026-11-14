package com.example.spb.utils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


@Component
public class StringToFileUtil {

    /**
     * 将字符串内容写入到指定路径的文件中
     *
     * @param content  要写入的字符串内容
     * @param filePath 文件的路径，包括文件名
     * @return 写入内容后的文件对象
     * @throws IOException 如果在写入过程中发生错误
     */
    public static  File convertStringToFile(String content, String filePath) throws IOException {
        File file = new File(filePath);

        // 创建文件及其父目录（如果不存在）
        file.getParentFile().mkdirs();

        // 使用 FileWriter 将字符串内容写入文件
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }

        return file;
    }
}
