package com.ldx.blog.service;

import com.ldx.blog.result.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Uaena
 * @date 2023/5/20 12:52
 */
public interface QiniuUploadService {
    Result<Object> saveFileToQiNiu(MultipartFile file);

    Result<Object> saveMultipartFileToQiNiu(MultipartFile[] files);

}
