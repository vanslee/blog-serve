package com.ldx.blog.pojo.article;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ldx.blog.pojo.category.Category;
import com.ldx.blog.pojo.user.User;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author Uaena
 * @TableName article
 */
@Data
public class Article implements Serializable {

    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    private Integer collects;
    /**
     *
     */
    private Long userId;
    /**
     * markdown文件
     */
    @TableField(fill = FieldFill.INSERT)
    private String mdUrl;
    /**
     *
     */
    private String originalUrl;
    /**
     *
     */
    @NotBlank(message = "文章标题不能为空")
    private String articleTitle;
    /**
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Long publishDate;
    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateDate;
    /**
     *
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @NotBlank(message = "请上传文章封面")
    private String imgUrl;
    /**
     *
     */
    private Integer views;
    /**
     *
     */
    private Integer likes;
    /**
     * 文章摘要
     */
    private String articleAbstract;
    /**
     *
     */
    private Integer comments;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private boolean isDelete;
    @TableField(exist = false)
    private String authorAvatar;
    @TableField(exist = false)
    private String authorName;
    @TableField(exist = false)
    private User author;
    @TableField(exist = false)
    private long categoryId;
    @TableField(exist = false)
    private Category category;
    @TableField(exist = false)
    @Size(min = 1,message = "至少选择一个标签")
    private List<String> tagNames;
    @TableField(exist = false)
    private List<Tags> tags;
    @TableField(exist = false)
    private String articleContent;
    @TableField(exist = false)
    private String personalBrief;
    @TableField(exist = false)
    private String username;
    @TableField(exist = false)
    private String avatar;
    private int status;
}
