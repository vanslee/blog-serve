package com.ldx.blog.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @author Uaena
 * @date 2023/5/20 13:10
 */
@Slf4j
public class FileUtil {
    /**
     * 将文本内容变成md文件
     */
    public static File generateMarkdown(String fileName, String content) {
        try {
            Path path = Paths.get(System.getProperty("user.dir"), "file", fileName.concat(".md"));
            Files.createDirectories(path.getParent());
            try (FileWriter writer = new FileWriter(path.toFile(), true)) {
                writer.write(content);
                return path.toFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * base64字符转换成file
     *
     * @param destPath 保存的文件路径
     * @param base64   图片字符串
     * @param fileName 保存的文件名
     * @return file
     */
    public File base64ToFile(String destPath, String base64, String fileName) {
        File file = null;
        //创建文件目录
        String filePath = destPath;
        File dir = new File(filePath);
        if (!dir.exists() && !dir.isDirectory()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            file = new File(filePath + "/" + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 将file转换成base64字符串
     *
     * @param path
     * @return
     */
    public String fileToBase64(String path) {
        String base64 = null;
        InputStream in = null;
        try {
            File file = new File(path);
            in = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            in.read(bytes);
            base64 = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return base64;
    }

    /**
     * MultipartFile类型文件转File
     *
     * @return File类型文件
     */
    public static File multipartFileToFile(MultipartFile multipartFile) {
        File f = null;
        if (StringUtil.isEmpty(multipartFile.getName()) || multipartFile.getSize() <= 0) {
            log.error("用户上传了空文件:{}", System.currentTimeMillis());
            return null;
        } else {
            try {
                String fileName = multipartFile.getOriginalFilename();
                Path path = Paths.get(System.getProperty("user.dir"), "files", fileName);
                Files.createDirectories(path.getParent());
                File file = convertMultipartFileToFile(multipartFile,path.toFile().getPath() );
                return file;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static File convertMultipartFileToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File file = new File(fileName);
        multipartFile.transferTo(file);
        return file;
    }

}
