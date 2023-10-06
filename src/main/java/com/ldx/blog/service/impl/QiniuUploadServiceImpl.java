package com.ldx.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSONArray;
import com.ldx.blog.result.Result;
import com.ldx.blog.result.ResultCodeEnum;
import com.ldx.blog.service.QiniuUploadService;
import com.ldx.blog.utils.FileUtil;
import com.ldx.blog.utils.QiNiuYunOssUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Uaena
 * @date 2023/5/20 12:51
 */
@Service
public class QiniuUploadServiceImpl implements QiniuUploadService {
    @Resource
    private QiNiuYunOssUtil ossUtil;
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAX_POOL_SIZE = 16;
    private static final int QUEUE_CAPACITY = 100;
    private static final Long KEEP_ALIVE_TIME = 1L;
    private static final ArrayBlockingQueue ARRAY_BLOCKING_QUEUE = new ArrayBlockingQueue(QUEUE_CAPACITY);

    @Value("${website.config.cdn}")
    public String qiniuyun;

    @Override
    public Result<Object> saveFileToQiNiu(MultipartFile file) {
        if (Objects.isNull(file)) {
            return Result.fail(ResultCodeEnum.UPLOAD_FILE_ERROR);
        }
        // 文件名
        String originalFilename = file.getOriginalFilename();
        // 文件后缀名
        int i = originalFilename.lastIndexOf('.');
        String suffixName = "";
        if (i != -1) {
            //有后缀取后缀名
            suffixName = originalFilename.substring(i);
        }
        String img_regex = "^.*\\.(?i:png|jpe?g|gif|bmp)$";
        Pattern pattern = Pattern.compile(img_regex);
        Matcher matcher = pattern.matcher(originalFilename);
        Object loginId = StpUtil.getLoginId();
        String url = "";
        if (matcher.matches()) {
            File uploadFile = FileUtil.multipartFileToFile(file);
            if (Objects.isNull(uploadFile)) {
                return Result.fail(ResultCodeEnum.UPLOAD_FILE_ERROR);
            }
            url = qiniuyun + ossUtil.uploadImg(uploadFile, loginId + "/");
        } else if (suffixName.equals("md")) {
            File uploadFile = FileUtil.multipartFileToFile(file);
            if (Objects.isNull(uploadFile)) {
                return Result.fail(ResultCodeEnum.UPLOAD_FILE_ERROR);
            }
            url = qiniuyun + ossUtil.uploadMarkdown(uploadFile, loginId + "/");
        } else {
            File uploadFile = FileUtil.multipartFileToFile(file);
            if (Objects.isNull(uploadFile)) {
                return Result.fail(ResultCodeEnum.UPLOAD_FILE_ERROR);
            }
            url = qiniuyun + ossUtil.uploadFile(uploadFile, loginId + "/");
        }


        // 文件全路径名
        Map<String, String> returnFileName = new HashMap<>(3);
        returnFileName.put("name", originalFilename);
        returnFileName.put("suffix", suffixName);
        returnFileName.put("url", url);
        if (Objects.isNull(url) || "".equals(url)) {
            return Result.fail(ResultCodeEnum.UPLOAD_FILE_ERROR);
        }
        return Result.success(ResultCodeEnum.UPLOAD_FILE_SUCCESS, returnFileName);
    }

    @Override
    public Result<Object> saveMultipartFileToQiNiu(MultipartFile[] files) {
        JSONArray jsonArray = new JSONArray();
        for (MultipartFile file : files) {
            Result<Object> objectResult = saveFileToQiNiu(file);
            jsonArray.add(objectResult.getData());

        }
        if (jsonArray.size() < 1) {
            return Result.fail(ResultCodeEnum.UPLOAD_FILE_ERROR);
        }
        return Result.success(ResultCodeEnum.UPLOAD_FILE_SUCCESS, jsonArray);
    }
}

