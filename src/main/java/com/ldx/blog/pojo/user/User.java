package com.ldx.blog.pojo.user;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ldx.blog.pojo.category.Category;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author Uaena
 * @TableName user
 */
@TableName(value = "user")
@Getter
@Setter
@Accessors(chain = true)
public class User implements Serializable {
    private Integer id;


    private String phone;
    @Size(min = 3, max = 20, message = "用户名必须在3~8之间")
    private String username;
    @Size(min = 8, max = 20, message = "密码必须在6~20之间")
    private String password;
    @TableField(fill = FieldFill.INSERT)
    private boolean gender;

    private String nick;

    private String birthday;

    private String email;
    private Integer fans;
    private String personalBrief;
    @TableField(exist = false)
    private Long articleCount;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String avatarImgUrl;
    @TableField(exist = false)
    private List<Category> categories;
    @TableField(fill = FieldFill.INSERT)
    private boolean isDelete;
    private String ip;
    private String unionId;
    private String loginWay;
    @TableField(fill = FieldFill.INSERT)
    private Long createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long recentlyTime;
    @TableField(exist = false, fill = FieldFill.UPDATE)
    private String newPassword;
    @TableField(fill = FieldFill.UPDATE)
    private Long updateTime;
    private static final long serialVersionUID = 1L;
}