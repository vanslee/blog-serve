package com.ldx.blog.controller;

import com.ldx.blog.result.Result;
import com.ldx.blog.service.impl.QiniuUploadServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Uaena
 * @date 2023/5/20 12:50
 */
@RestController
@Profile("!test")
@RequestMapping("/upload")
public class UploadController {
    @Autowired
    private QiniuUploadServiceImpl uploadService;



    /**
     * 所有文件上传
     *
     * @param file
     * @return
     */
    @PostMapping(  "/upload_file")
    public Result<Object> uploadFile(MultipartFile file) {
        return uploadService.saveFileToQiNiu(file);
    }

    @PostMapping("/upload_files")
    public Result<Object> uploadFiles(MultipartFile[] file) {
        return uploadService.saveMultipartFileToQiNiu(file);
    }
}
