package com.example.spb.utils;

import com.itextpdf.awt.AsianFontMapper;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
@Component
public class PdfGenerateUtil {
    // 临时文件夹获取java system变量中的临时路径，在web项目中是容器的temp文件夹,如果直接运行是系统临时文件夹.
    private static final String FILE_PATH_TEMPLATE = System.getProperty("java.io.tmpdir") + "/tempdf/%s";
    public File designer(String name,String job_id) {
        // 输出文件
        File outFile = null;
        try {
            //生成临时文件位置，存储到 java.io.tmpdir 下
            outFile = new File(String.format(FILE_PATH_TEMPLATE, UUID.randomUUID() + ".pdf"));
            //如果不存在临时文件夹，则创建文件夹
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            System.out.println("临时文件所在位置：——" + outFile.getPath());
            FileOutputStream outputStream = new FileOutputStream(outFile);
//            Rectangle rectangle = new Rectangle(PageSize.A4);
            Rectangle rectangle = PageSize.A4.rotate();

            //1.创建文档 并设置四周边距
            Document document = new Document(rectangle, 40, 40, 40, 40);
            //2.新建pdf实例 输出到本地文件目录
            //PDFWriter可以将文档存成PDF文件；HtmlWriter可以将文档存成html文件
            PdfWriter.getInstance(document, outputStream);
            //3.打开文档 无论输出到本地还是 页面都要打开文档
            document.open();
            // 设置pdf的基础样式；参数1:(1)使用iText中的字体,字体类行为宋体，（2）使用资源字体classpath：/SIMYOU.TTF （3）使用windows系统字体C:/Windows/Fonts/SIMYOU.TTF
            BaseFont baseFont = BaseFont.createFont(AsianFontMapper.ChineseSimplifiedFont, AsianFontMapper.ChineseSimplifiedEncoding_H, BaseFont.NOT_EMBEDDED);
            // 文档标题对应的样式 参数1：相当于字体（宋体） 参数2：字体大小 参数3：是否加粗，斜体 参数4：字体颜色
            Font headerFont = new Font(baseFont, 30, Font.NORMAL, BaseColor.BLACK);
            // headerParagraph  文档标题
            Paragraph headerParagraph = new Paragraph("培训证明", headerFont);
            ///文字居中
            headerParagraph.setAlignment(Paragraph.ALIGN_CENTER);
            //将添加到文档中
            document.add(headerParagraph);


            // 4. 添加正文内容
            Font contentFont = new Font(baseFont, 20, Font.NORMAL, BaseColor.BLACK);
            String contentText = String.format("入党积极分子 %s，学/工号为 %s，在本次入党积极分子培训中修完所有课程，特此证明。", name, job_id);
            Paragraph contentParagraph = new Paragraph(contentText, contentFont);
            contentParagraph.setSpacingBefore(30); // 设置段落与标题之间的距离
            contentParagraph.setAlignment(Paragraph.ALIGN_LEFT); // 左对齐
            document.add(contentParagraph);

            // 5. 添加学院信息
            Paragraph footerParagraph1 = new Paragraph("南京理工大学电子工程与光电技术学院党委", contentFont);
            footerParagraph1.setSpacingBefore(200); // 设置与正文的距离
            footerParagraph1.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(footerParagraph1);

            // 6. 添加日期（当天日期）
            String currentDate = new SimpleDateFormat("yyyy年MM月dd日").format(new Date());
            Paragraph footerParagraph2 = new Paragraph(currentDate, contentFont);
            footerParagraph2.setSpacingBefore(10);
            footerParagraph2.setAlignment(Paragraph.ALIGN_RIGHT);
            document.add(footerParagraph2);


            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outFile;
    }

    public File fillTemplate(String templatePath, String name, String jobId,String beginYear,String beginMonth,String beginDay, String year, String month, String day, String exportYear, String exportMonth, String exportDay) {
        File outputFile = null;
        try {
            // 1. 读取模板
            PdfReader reader = new PdfReader(templatePath);

            // 2. 创建临时文件
            String tempFileName = "filled_certificate_" + UUID.randomUUID() + ".pdf";
            outputFile = new File(FILE_PATH_TEMPLATE, tempFileName);

            // 创建父目录（如果不存在）
            if (!outputFile.getParentFile().exists()) {
                outputFile.getParentFile().mkdirs();
            }

            // 创建输出流
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            // 3. 创建 PdfStamper 对象
            PdfStamper stamper = new PdfStamper(reader, outputStream);

            // 4. 获取 PdfContentByte 对象
            PdfContentByte canvas = stamper.getOverContent(1); // 修改第一页
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

            // 5. 插入动态内容
            canvas.beginText();
            canvas.setFontAndSize(baseFont, 12);
            canvas.setColorFill(BaseColor.BLACK);

            // 插入姓名
            canvas.setTextMatrix(490, 410); // 坐标 (x, y)，根据实际测量调整
            canvas.showText(name);

            // 插入学号
            canvas.setTextMatrix(680, 410); // 调整位置
            canvas.showText(jobId);

            canvas.setTextMatrix(470, 360); // 调整位置
            canvas.showText(beginYear);

            canvas.setTextMatrix(540, 360); // 调整位置
            canvas.showText(beginMonth);

            canvas.setTextMatrix(600, 360); // 调整位置
            canvas.showText(beginDay);

            canvas.setTextMatrix(670, 360); // 调整位置
            canvas.showText(month);

            canvas.setTextMatrix(720, 360); // 调整位置
            canvas.showText(day);

            canvas.setTextMatrix(680, 320); // 调整位置
            canvas.showText(year);

            canvas.setTextMatrix(620, 85); // 调整位置
            canvas.showText(exportYear.substring(2));

            canvas.setTextMatrix(660, 85); // 调整位置
            canvas.showText(exportMonth);

            canvas.setTextMatrix(690, 85); // 调整位置
            canvas.showText(exportDay);

//            // 插入日期
//            canvas.setTextMatrix(300, 200); // 日期位置
//            String dateText = String.format("二〇%s年%s月%s日", year, month, day);
//            canvas.showText(dateText);

            canvas.endText();

            // 6. 关闭 PdfStamper 和 PdfReader
            stamper.close();
            reader.close();
            outputStream.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            return null; // 返回 null 表示生成失败
        }

        return outputFile; // 返回生成的临时文件对象
    }
}
