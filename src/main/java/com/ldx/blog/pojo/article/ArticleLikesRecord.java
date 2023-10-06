package com.ldx.blog.pojo.article;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName article_likes_record
 */
@TableName(value ="article_likes_record")
@Data
public class ArticleLikesRecord implements Serializable {
    @TableId
    private Integer id;

    private Long articleid;

    private Integer likerid;

    private String likedate;

    private Integer isread;

    private static final long serialVersionUID = 1L;
}