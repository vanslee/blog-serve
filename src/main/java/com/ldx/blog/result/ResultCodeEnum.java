package com.ldx.blog.result;

import lombok.Getter;

/**
 * @author LoneWalker
 */

@Getter
public enum ResultCodeEnum {

    /**
     * success
     */
    SUCCESS(200, "操作成功"),
    UPLOAD_FILE_SUCCESS(200, "上传成功"),
    PUBLISH_SUCCESS(200, "发布文章成功"),
    /**
     * fail
     */
    FAIL(-1, "操作失败"),
    NOT_LOGIN(5000, "身份已过期"),
    UNKNOWN_ERROR(301, "未知错误"),
    UPLOAD_FILE_ERROR(505, "上传失败"),
    PUBLISH_FAIL(506, "发布文章失败"),
    /**
     * 参数错误：1001-1999
     */
    PARAM_IS_INVALID(1001, "参数无效"),
    PARAM_TYPE_ERROR(1002, "参数类型错误"),

    LOGIN_PARAM_NULL(500, "缺少用户名或密码"),
    LOGIN_ERROR(500, "用户名或密码错误"),
    LOGIN_SUCCESS(200, "登录成功"), LOGOUT_SUCCESS(200, "退出成功"),
    USER_HAS_EXIST(503, "用户已存在,去登录"), REGISTRY_SUCCESS(200, "注册成功"), REGISTRY_ERROR(504, "注册失败"),
    OAUTH_FAIL(501, "第三方认证失败"), OAUTH_SUCCESS(211, "第三方认证成功"),
    CODE_INVALID(504, "验证码错误或已过期"),
    DELETE_ARTICLE_FAIL(500, "删除文章失败"), UPDATE_USER_ERROR(500, "用户信息更新失败"), USERNAME_HAS_EXIST(500, "用户名已存在"),
    HAS_SEND_EMAIL(505, "请勿重复发送邮件"), EMAIL_SEND_SUCCESS(200, "邮件发送成功"),
    PUBLISH_COMMENT_FAIL(506, "发送评论失败"), PUBLISH_COMMENT_SUCCESS(200, "发送评论成功"),
    COLLECT_ARTICLE_SUCCESS(200, "收藏文章成功"),
    COLLECT_ARTICLE_FAIL(507, "收藏文章失败"), REFRESH_TOKEN_FAIL(500, "刷新token失败");


    /**
     * 状态码
     */
    private final int code;

    /**
     * 提示信息
     */
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
