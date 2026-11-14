package com.example.spb.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.UUID;

/**
 * 阿里云OSS工具类
 */
@Component
public class AliOSSUtils {
    @Value("${storage.local:true}")
    private boolean localStorage;
    @Value("${server.url:http://127.0.0.1:8080}")
    private String serverUrl;
    private String saveLocal(InputStream input, String extension) throws IOException {
        java.nio.file.Path directory = java.nio.file.Paths.get(".local", "uploads");
        java.nio.file.Files.createDirectories(directory);
        String name = UUID.randomUUID().toString() + extension;
        try (InputStream stream = input) {
            java.nio.file.Files.copy(stream, directory.resolve(name));
        }
        return serverUrl + "/uploads/" + name;
    }
    private String endpoint = "https://oss-cn-nanjing.aliyuncs.com";
    @Value("${alioss.accessKey.id}")
    private String accessKeyId;

    @Value("${alioss.accessKey.secret}")
    private String accessKeySecret;

    @Value("${alioss.bucketName}")
    private String bucketName;

    public String upload(MultipartFile multipartFile) throws IOException { if (localStorage) { String name = multipartFile.getOriginalFilename(); return saveLocal(multipartFile.getInputStream(), name != null && name.contains(".") ? name.substring(name.lastIndexOf(".")) : ""); }
        // 获取上传的文件的输入流
        InputStream inputStream = multipartFile.getInputStream();

        // 避免文件覆盖
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        //上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;

        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }
    public String upload(File file) throws IOException { if (localStorage) return saveLocal(new FileInputStream(file), file.getName().contains(".") ? file.getName().substring(file.getName().lastIndexOf(".")) : "");
        // 获取文件输入流
        InputStream inputStream = new FileInputStream(file);

        // 避免文件覆盖，生成唯一的文件名
        String originalFilename = file.getName();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));

        // 创建 OSS 客户端
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        // 上传文件到 OSS
        ossClient.putObject(bucketName, fileName, inputStream);

        // 文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;

        // 关闭 OSS 客户端
        ossClient.shutdown();
        return url; // 返回文件上传到 OSS 的路径

    }
    public String upload(ByteArrayInputStream inputStream) throws IOException { if (localStorage) return saveLocal(inputStream, ".pdf");
// 避免文件覆盖，可以动态生成唯一的文件名
        String fileName = UUID.randomUUID().toString() + ".pdf"; // 设置为 PDF 文件

        // 创建 OSSClient 实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 上传文件到 OSS
            ossClient.putObject(bucketName, fileName, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Failed to upload file to OSS.");
        } finally {
            // 关闭 ossClient
            ossClient.shutdown();
        }

        // 生成文件访问路径
        String url = "https://" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;

        // 返回文件的访问 URL
        return url;
    }

    public String uploadWorkbook(Workbook workbook) throws IOException { if (localStorage) { ByteArrayOutputStream bytes = new ByteArrayOutputStream(); workbook.write(bytes); workbook.close(); return saveLocal(new ByteArrayInputStream(bytes.toByteArray()), ".xlsx"); }
        // 创建一个输出流，将 workbook 写入其中
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        // 将输出流转换为输入流
        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // 生成唯一的文件名，避免覆盖
        String fileName = UUID.randomUUID().toString() + ".xlsx";

        // 上传文件到 OSS
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, fileName, inputStream);

        // 生成文件访问 URL
        String url = endpoint.split("//")[0] + "//" + bucketName + "." + endpoint.split("//")[1] + "/" + fileName;

        // 关闭 OSS 客户端
        ossClient.shutdown();

        // 返回 OSS 上的文件 URL
        return url;
    }


    public void deleteFile(String url){ if (localStorage) return;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            String input = url.split(endpoint.split("//")[1])[1];
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, input.substring(1));
            System.out.println(input.substring(1));
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
