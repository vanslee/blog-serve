package com.ldx.blog.pojo.oath.qq;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Uaena
 * @date 2023/6/10 0:36
 */
@SuppressWarnings("AlibabaClassNamingShouldBeCamel")
@Getter
@Setter
@ToString
public class QQUserInfo {
    /**
     * 返回码
     */
    private String ret;
    /**
     * 如果ret<0，会有相应的错误信息提示，返回数据全部用UTF-8编码
     */
    private String msg;
    /**
     * openid
     */
    private String openid;
    /**
     * 判断是否有数据丢失。如果应用不使用cache，不需要关心此参数。0或者不返回：没有数据丢失，可以缓存。1：有部分数据丢失或错误，不要缓存
     */
    private String isLost;
    /**
     * 用户在QQ空间的昵称。
     */
    private String nickname;
    /**
     * 大小为30×30像素的QQ空间头像URL。
     */
    private String figureurl;
    /**
     * 大小为50×50像素的QQ空间头像URL。
     */
    private String figureurl1;
    /**
     * 大小为100×100像素的QQ空间头像URL。
     */
    private String figureurl2;
    /**
     * 大小为40×40像素的QQ头像URL。
     */
    private String figureurlQq1;
    /**
     * 大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有。
     */
    private String figureurlQq2;
    /**
     * 性别。 如果获取不到则默认返回'男'
     */
    private String gender;
    /**
     * 性别类型。默认返回2
     */
    private String genderType;
    /**
     * 省份
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 年
     */
    private String year;
    /**
     * 星座
     */
    private String constellation;
    /**
     * 标识用户是否为黄钻用户
     */
    private String isYellowVip;
    /**
     * 黄钻等级
     */
    private String yellowVipLevel;
    private String isYellowYearVip;
}
