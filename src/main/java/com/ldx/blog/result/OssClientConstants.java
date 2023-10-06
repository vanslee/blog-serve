//package com.ldx.blog.result;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
///**
// * @author Uaena
// * @date 2023/5/20 13:07
// */
//@Component
//public class OssClientConstants {
//
//    /**
//     * 七牛云API的外网域名
//     */
//    public static final String ENDPOINT = "lidengxiang.top";
//
//    /**
//     * 七牛云API的密钥Access Key ID
//     */
//    public static String ACCESS_KEY_ID;
//    /**
//     *七牛云API的密钥Access Key Secret
//     */
//    public static String ACCESS_KEY_SECRET;
//
//    /**
//     * 七牛云API的bucket名称
//     * 在七牛云上自己创建一个bucket
//     */
//    public static final String BACKET_NAME = "lidengxiang";
//
//    /**
//     * 七牛云API的文件夹名称
//     * 在七牛云上自己创建一个文件夹，方便分类管理图片
//     */
//    public static final String FOLDER="img/";
//
//    @Value("${qiniuyun.accessKeyId}")
//    public void setAccessKeyId(String accessKeyId) {
//        ACCESS_KEY_ID = accessKeyId;
//    }
//
//    @Value("${qiniuyun.secret}")
//    public void setAccessKeySecret(String accessKeySecret) {
//        ACCESS_KEY_SECRET = accessKeySecret;
//    }
//}
