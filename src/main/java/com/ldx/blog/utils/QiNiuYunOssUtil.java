package com.ldx.blog.utils;

/**
 * @author Uaena
 * @date 2023/5/20 13:06
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @description: 七牛云对象存储OSS工具类
 * @author: liuchuanfeng
 * @time: 2020/12/14 17:00
 */
@Slf4j
@Component
public class QiNiuYunOssUtil {

    @Value("${qiniuyun.end_point}")
    private  String ENDPOINT;
    @Value("${qiniuyun.access_key}")
    private  String ACCESS_KEY;
    @Value("${qiniuyun.secret}")
    private  String SECRET;
    @Value("${qiniuyun.bucket_name}")
    private  String BUCKET_NAME;
    @Value("${qiniuyun.img_folder}")
    private  String IMG_FOLDER;
    @Value("${qiniuyun.md_folder}")
    private  String MD_FOLDER;
    @Value("${qiniuyun.file_folder}")
    private  String FILE_FOLDER;

    /**
     * 上传MarkDown至OSS
     *
     * @param file 上传文件（文件全路径如：D:\\image\\cake.jpg）
     *             //     * @param subCatalog 模拟文件夹名 如"img/"
     * @param key 用户id/
     * @return String 文件名
     */
    public  String uploadMarkdown(File file,String key) {

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String uri = FILE_FOLDER.concat(key).concat(file.getName());
        return createUploadManager(file,  uri);
    }
    public  String uploadFile(File file,String key) {

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String uri = FILE_FOLDER.concat(key).concat(file.getName());
        return createUploadManager(file,  uri);
    }
    /**
     * 上传图片至OSS
     *
     * @param file 上传文件（文件全路径如：D:\\image\\cake.jpg）
     *             //     * @param subCatalog 模拟文件夹名 如"img/"
     * @param key 用户id/
     * @return String 文件名
     */
    public  String uploadImg(File file,String key) {
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String uri = IMG_FOLDER.concat(key).concat(file.getName());
        return createUploadManager(file, uri);
    }
    private String createUploadManager(File file, String uri) {
        String resultStr = null;
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huadongZheJiang2());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        log.info("qiniuyun key: [{}]", uri);
        try {
            try {
                Auth auth = Auth.create(ACCESS_KEY, SECRET);
                String upToken = auth.uploadToken(BUCKET_NAME, uri);
                log.info("qiniuyun  filePath: [{}] , fileSize: [{}]MB",file.getPath(), file.length() >> 20);
                Response response = uploadManager.put(file, uri, upToken);
                //解析上传成功的结果(将字符串解析成Java对象)
                String bodyString = response.bodyString();
                Object parse = JSON.parse(bodyString);
                String s = parse.toString();
                DefaultPutRet putRet = JSONObject.parseObject(s, DefaultPutRet.class);
                log.info("qiniuyun putRet.key: [{}]", putRet.key);
                resultStr = putRet.key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                log.error(r.toString());
            }
        } catch (Exception ex) {
            log.error("qiniuyunoss uploadFile2OSS" + ex);
        }
        return resultStr;
    }



}
